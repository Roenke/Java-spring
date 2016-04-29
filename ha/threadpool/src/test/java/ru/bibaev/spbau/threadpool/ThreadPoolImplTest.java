package ru.bibaev.spbau.threadpool;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ThreadPoolImplTest {
    @Test(expected = LightExecutionException.class)
    public void shutdown() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(3);
        int taskCount = 10000;
        AtomicInteger callCounter = new AtomicInteger(0);
        Collection<LightFuture> futures = new ArrayList<>(taskCount);
        for (int i = 0; i < taskCount; ++i) {
            LightFuture future = pool.add(callCounter::incrementAndGet);
            futures.add(future);
        }

        pool.shutdown();

        // Check that all future receive some result
        for (LightFuture future : futures) {
            assertTrue(future.isReady());
        }

        // Check that not all tasks fully completed
        for (LightFuture future : futures) {
            future.get();
        }
    }

    @Test
    public void addOneTask() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(3);
        LightFuture future = pool.add(() -> true);

        assertTrue((boolean) future.get());
    }

    @Test
    public void simpleThenApply() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(1);
        LightFuture<Integer> future = pool.add(() -> 100);

        LightFuture<Integer> sqrtFuture = future.thenApply(x -> x - 1);
        assertEquals(99, sqrtFuture.get().intValue());
    }

    @Test
    public void thenApplyWaitOldTask() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(1);
        LightFuture<Integer> future = pool.add(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            return 9;
        });

        LightFuture<Double> sqrtFuture = future.thenApply((Math::sqrt));
        assertEquals(3.0, sqrtFuture.get(), 0.01);
    }

    @Test
    public void isReadyTest() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(4);
        LightFuture<Integer> future = pool.add(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 3;
        });

        assertFalse(future.isReady());
        assertEquals(3, future.get().intValue());
    }

    @Test
    public void threadCountLess() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(50);
        for (int i = 0; i < 50; i++) {
            pool.add(() -> {
                try {
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException ignored) {
                }
                return 1;
            });
        }

        LightFuture fastFuture = pool.add(() -> 1);
        Thread.sleep(50);
        assertFalse(fastFuture.isReady());
    }

    @Test(timeout = 399)
    public void threadCountGreater() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(10);
        List<LightFuture<Integer>> futures = new ArrayList<>(15);
        for (int i = 0; i < 15; i++) {
            LightFuture<Integer> future = pool.add(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException ignored) {
                }
                return 1;
            });

            futures.add(future);
        }

        for(int i = 0; i < 10; i++) {
            assertEquals(1, futures.get(i).get().intValue());
        }
    }
}
