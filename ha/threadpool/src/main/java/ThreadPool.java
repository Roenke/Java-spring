public interface ThreadPool {
    void shutdown();
    boolean add(LightFuture<?> task);
}
