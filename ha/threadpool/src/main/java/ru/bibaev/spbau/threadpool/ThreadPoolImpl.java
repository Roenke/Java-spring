package ru.bibaev.spbau.threadpool;

import ru.bibaev.spbau.threadpool.utils.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public class ThreadPoolImpl implements ThreadPool {
    public ThreadPoolImpl(int count) {
        for (int i = 0; i < count; i++) {
            Thread worker = new Worker();
            worker.start();
            executors.add(worker);
        }
    }

    @Override
    public void shutdown() throws InterruptedException {
        synchronized (tasks) {
            for (Pair<Runnable, LightFuture> task : tasks) {
                synchronized (task.getSecond()) {
                    ((LightFutureImpl) task.getSecond())
                            .exception(new LightExecutionException("Thread pool is over."));
                    task.getSecond().notifyAll();
                }
            }

            tasks.clear();
        }

        for (Thread worker : executors) {
            worker.interrupt();
            worker.join();
        }
    }

    @Override
    public <R> LightFuture<R> add(Supplier<R> supplier) {
        LightFutureImpl<R> lightFuture = new LightFutureImpl<>(this);
        Runnable task = () -> {
            try {
                R res = supplier.get();
                lightFuture.result(res);
            } catch (Exception e) {
                lightFuture.exception(new LightExecutionException(e));
            }

            synchronized (lightFuture) {
                lightFuture.notifyAll();
            }
        };

        synchronized (tasks) {
            tasks.add(new Pair<>(task, lightFuture));
            tasks.notifyAll();
        }

        return lightFuture;
    }

    private final Queue<Pair<Runnable, LightFuture>> tasks = new LinkedList<>();
    private final Collection<Thread> executors = new ArrayList<>();

    private class Worker extends Thread {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Runnable task;
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }

                        task = tasks.poll().getFirst();
                        tasks.notifyAll();
                    }

                    task.run();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
