import java.util.function.Function;

public interface LightFuture<R> {
    boolean isReady();
    R get();
    <T> T thenApply(Function<R, T> function);
}
