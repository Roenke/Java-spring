package ru.bibaev.spbau.threadpool;

import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public interface ThreadPool {
    void shutdown() throws InterruptedException;

    <R> LightFuture<R> add(Supplier<R> task);
}
