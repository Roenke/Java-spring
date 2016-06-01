package ru.bibaev.spbau.threadpool;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class ThreadPoolImpl implements ThreadPool {
    public ThreadPoolImpl(int count) {
        Stream.iterate(0, x -> x + 1)
                .limit(count)
                .map(x -> new Worker())
                .peek(Thread::start)
                .forEach(executors::add);
    }

    @Override
    public void shutdown() throws InterruptedException {
        synchronized (tasks) {
            for (Task task : tasks) {
                task.future.setException(new LightExecutionException("Thread pool is over."));
            }

            tasks.clear();
        }

        executors.forEach(Thread::interrupt);
    }

    @Override
    public <R> LightFuture<R> add(Supplier<R> supplier) {
        Task<R> task = new Task<>(supplier);
        addTask(task);
        return task.getLightFuture();
    }

    private <R> void addTask(Task<R> task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notifyAll();
        }
    }

    private class Task<R> implements Runnable {
        public Task(Supplier<R> s) {
            supplier = s;
        }

        public LightFuture<R> getLightFuture() {
            return future;
        }

        public <T> LightFuture<T> addDependency(Function<? super R, T> function) {
            Task<T> task = new Task<>(() -> function.apply(future.result));
            synchronized (depends) {
                synchronized (future) {
                    if (future.result != null) {
                        addTask(task);
                    } else if (future.exception != null) {
                        task.future.setException(new LightExecutionException(future.exception));
                    } else {
                        depends.add(task);
                    }
                }
            }

            return task.getLightFuture();
        }

        @Override
        public void run() {
            Consumer<Task> action;
            try {
                R res = supplier.get();
                future.setResult(res);
                action = ThreadPoolImpl.this::addTask;
            } catch (Exception e) {
                LightExecutionException exception = new LightExecutionException(e);
                future.setException(exception);
                action = (task) -> task.future.setException(new LightExecutionException(e));
            }

            synchronized (depends) {
                depends.forEach(action);
                depends.clear();
            }
        }

        private final Future<R> future = new Future<>(this);
        private final Supplier<R> supplier;
        private final List<Task> depends = new ArrayList<>();
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            Runnable task;
            try {
                while (!Thread.interrupted()) {
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }

                        task = tasks.poll();
                        tasks.notify();
                    }

                    task.run();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static class Future<R> implements LightFuture<R> {
        public Future(Task<R> parentTask) {
            task = parentTask;
            isCompleted = false;
        }

        public synchronized void setResult(R res) {
            result = res;
            isCompleted = true;
            notifyAll();
        }

        public synchronized void setException(LightExecutionException ex) {
            exception = ex;
            isCompleted = true;
            notifyAll();
        }

        @Override
        public synchronized boolean isReady() {
            return isCompleted;
        }

        @Override
        public synchronized R get() throws LightExecutionException, InterruptedException {
            while (!isCompleted) {
                wait();
            }

            if (exception != null) {
                throw exception;
            }

            return result;
        }

        @Override
        public <T> LightFuture<T> thenApply(Function<? super R, T> function) {
            return task.addDependency(function);
        }

        private volatile LightExecutionException exception;
        private volatile R result;
        private boolean isCompleted;
        private final Task<R> task;
    }

    private final Queue<Task> tasks = new LinkedList<>();
    private final Collection<Thread> executors = new ArrayList<>();
}
