import java.util.function.Consumer;

public class ConsumeAction<T> implements Runnable {

    private final Promise<T> src;
    private final Promise<Void> dest;
    private final Consumer<? super T> action;

    public ConsumeAction(Promise<T> src, Promise<Void> dest, Consumer<? super T> action) {
      this.src = src;
      this.dest = dest;
      this.action = action;
    }

    @Override
    public void run() {
        try {
            action.accept(src.get());
            dest.fulfill(null);
        }
        catch (Throwable throwable) {
            dest.fulfillExceptionally((Exception) throwable.getCause());
        }
    }

}