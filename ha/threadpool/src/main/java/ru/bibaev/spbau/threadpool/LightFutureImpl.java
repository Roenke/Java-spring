package ru.bibaev.spbau.threadpool;

import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class LightFutureImpl<R> implements LightFuture<R> {
    public LightFutureImpl(ThreadPool pool) {
        threadPool = pool;
    }

    public void result(R res) {
        result = res;
    }

    public void exception(LightExecutionException e) {
        exception = e;
    }

    @Override
    public boolean isReady() {
        return result != null || exception != null;
    }

    @Override
    public synchronized R get() throws LightExecutionException, InterruptedException {
        while (!isReady()) {
            LightFutureImpl.this.wait();
        }

        if (exception != null) {
            throw exception;
        }

        return result;
    }

    @Override
    public <T> LightFuture<T> thenApply(Function<R, T> function) {
        return threadPool.add(() -> {
            try {
                while (!isReady()) {
                    wait();
                }

                if (exception != null) {
                    throw exception;
                }

                return function.apply(result);
            } catch (InterruptedException e) {
                return null;
            }
        });
    }

    private R result = null;
    private LightExecutionException exception;
    private final ThreadPool threadPool;
}
