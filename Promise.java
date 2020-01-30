import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class Promise<T> extends PromiseSupport<T> {

    private Runnable fulfillmentAction;
    private Consumer<? super Throwable> exceptionHandler;

    public  Promise() {

    }

    public void fulfill(T value) {
        super.fulfill(value);
        postFulfillment();
    }

    public void fulfillExceptionally(Exception e) {
        super.fulfillExceptionally(e);
        handleException(e);
        postFulfillment();
    }

    public void postFulfillment() {
        if (fulfillmentAction == null) {
            return;
        }

        fulfillmentAction.run();
    }

    private void handleException(Exception e) {
        if (this.exceptionHandler == null) {
            return;
        }

        // visitor for exceptions
        this.exceptionHandler.accept(e);
    }

    public Promise<T> fulfillAsync(final Callable<T> task, Executor executor) {
        executor.execute(() -> {
            try {
                fulfill(task.call());
            }
            catch (Exception e) {
                fulfillExceptionally(e);
            }
        });
        return this;
    }

    public Promise<Void> thenAccept(Consumer<? super T> action) {
        final Promise<Void> dest = new Promise<Void>();
        fulfillmentAction = new ConsumeAction<T>(this, dest, action);
        return dest;
    }

    public Promise<T> onError(Consumer<? super Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public <V> Promise<V> thenApply(Function<? super T,V> func) {
        Promise<V> dest = new Promise<>();
        fulfillmentAction = new TransformAction<V, T>(this, dest, func);
        return dest;
    }

}
