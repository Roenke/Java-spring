@SuppressWarnings("unused")
public class ThreadPoolImpl implements  ThreadPool {
    @Override
    public void shutdown() {

    }

    @Override
    public boolean add(LightFuture<?> task) {
        return false;
    }
}
