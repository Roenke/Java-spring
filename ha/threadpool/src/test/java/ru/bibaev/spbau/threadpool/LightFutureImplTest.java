package ru.bibaev.spbau.threadpool;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class LightFutureImplTest {
    @Test
    public void isReady() throws Exception {
        LightFutureImpl<Integer> future = new LightFutureImpl<>(threadPool);
        assertFalse(future.isReady());
        future.result(10);
        assertTrue(future.isReady());

        future = new LightFutureImpl<>(threadPool);
        assertFalse(future.isReady());
        future.exception(new LightExecutionException("FATAL ERROR"));
        assertTrue(future.isReady());
    }

    @Test
    public void get() throws Exception {
        LightFutureImpl<Integer> future = new LightFutureImpl<>(threadPool);
        future.result(10);
        assertEquals(10, future.get().intValue());
    }

    @Test(expected = LightExecutionException.class)
    public void getEmpty() throws InterruptedException {
        LightFutureImpl<Integer> future = new LightFutureImpl<>(threadPool);
        future.exception(new LightExecutionException("FATAL ERROR"));
        future.get();
    }

    @Test
    public void thenApply() throws Exception {
        LightFutureImpl<Integer> future = new LightFutureImpl<>(threadPool);
        future.thenApply(x -> x * 2);
        when(threadPool.add(any())).thenReturn(new LightFutureImpl<>(threadPool));
        verify(threadPool).add(any());
    }

    private ThreadPool threadPool = mock(ThreadPool.class);
}