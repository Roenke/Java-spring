package ru.bibaev.spbau.threadpool;

import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public interface LightFuture<R> {
    boolean isReady();

    R get() throws LightExecutionException, InterruptedException;

    <T> LightFuture<T> thenApply(Function<? super R, T> function);
}
