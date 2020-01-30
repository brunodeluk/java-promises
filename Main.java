import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        Executor executor = Executors.newSingleThreadExecutor();
        Promise<Integer> promise = new Promise<>();
        promise.fulfillAsync(() -> {
            return 1 + 1;
        }, executor)
            .thenApply((res) -> {
                System.out.println("Result " + res);
                return res;
            });

        promise.get();
    }
}
