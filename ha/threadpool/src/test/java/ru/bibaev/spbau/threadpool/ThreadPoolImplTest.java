package ru.bibaev.spbau.threadpool;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ThreadPoolImplTest {
    @Test(expected = LightExecutionException.class)
    public void shutdown() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(3);
        int taskCount = 10000;
        Collection<LightFuture> futures = new ArrayList<>(taskCount);
        for (int i = 0; i < taskCount; ++i) {
            LightFuture future = pool.add(() -> {
                try {
                    TimeUnit.DAYS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                return 1;
            });
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
    public void thenApplyAfterParentTaskComplete() throws InterruptedException {
        ThreadPool pool = new ThreadPoolImpl(2);
        LightFuture<Integer> future = pool.add(() -> 10);

        assertEquals(10, future.get().intValue());
        LightFuture<Integer> applyingFuture = future.thenApply(x -> x + 2);
        assertEquals(12, applyingFuture.get().intValue());
    }

    @Test
    public void thenApplyParentTaskCompleteFail() throws InterruptedException {
        int count = 0;
        ThreadPool pool = new ThreadPoolImpl(2);
        for(int i = 0; i < 1000; i++){
            Integer a = 0;
            LightFuture<Integer> future = pool.add(() -> 100500 / a);

            LightFuture<Integer> newFuture = future.thenApply(x -> x + 2);
            try{
                newFuture.get();
            }
            catch (LightExecutionException e) {
                count++;
            }
        }

        assertEquals(count, 1000);
    }

    @Test
    public void thenApplyBeforeParentTaskComplete() throws InterruptedException {
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
