import java.util.function.Function;

public class TransformAction<V, T> implements Runnable {

    private final Promise<T> src;
    private final Promise<V> dest;
    private final Function<? super T, V> func;

    public TransformAction(Promise<T> src, Promise<V> dest, Function<? super T, V> func) {
        this.src = src;
        this.dest = dest;
        this.func = func;
    }

    @Override
    public void run() {
        try {
            dest.fulfill(func.apply(src.get()));
        }
        catch (Throwable throwable) {
            dest.fulfillExceptionally((Exception )throwable.getCause());
        }
    }

}