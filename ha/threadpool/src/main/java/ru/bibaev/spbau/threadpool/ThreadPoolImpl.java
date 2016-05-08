package ru.bibaev.spbau.threadpool;

import java.util.*;
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

        for (Thread worker : executors) {
            worker.interrupt();
            worker.join();
        }
    }

    @Override
    public <R> LightFuture<R> add(Supplier<R> supplier) {
        Task<R> task = new Task<>(supplier, tasks);
        addTask(task);
        return task.getLightFuture();
    }

    private <R> void addTask(Task<R> task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notifyAll();
        }
    }

    private final Queue<Task> tasks = new LinkedList<>();
    private final Collection<Thread> executors = new ArrayList<>();

    private static class Task<R> implements Runnable {
        public Task(Supplier<R> s, Queue<Task> queue) {
            taskQueue = queue;
            supplier = s;
        }

        public LightFuture<R> getLightFuture() {
            return future;
        }

        public <T> LightFuture<T> addDependency(Function<R, T> function) {
            Task<T> task = new Task<>(() -> function.apply(future.result), taskQueue);
            synchronized (depends) {
                if (future.result != null) {
                    addTask(task);
                } else if (future.exception != null) {
                    task.future.setException(new LightExecutionException(future.exception));
                } else {
                    depends.add(task);
                }
            }

            return task.getLightFuture();
        }

        @Override
        public void run() {
            try {
                R res = supplier.get();
                future.setResult(res);

                synchronized (taskQueue) {
                    synchronized (depends) {
                        taskQueue.addAll(depends);
                        depends.clear();
                        depends.notifyAll();
                    }
                    taskQueue.notifyAll();
                }
            } catch (Exception e) {
                LightExecutionException exception = new LightExecutionException(e);
                future.setException(exception);

                synchronized (depends) {
                    for (Task t : depends) {
                        t.future.setException(new LightExecutionException(exception));
                    }

                    depends.clear();
                    depends.notifyAll();
                }
            }
        }

        private <T> void addTask(Task<T> task) {
            synchronized (taskQueue) {
                taskQueue.add(task);
                taskQueue.notifyAll();
            }
        }

        private final Future<R> future = new Future<>(this);
        private final Supplier<R> supplier;
        private final List<Task> depends = new ArrayList<>();
        private final Queue<Task> taskQueue;
    }

    private static class Future<R> implements LightFuture<R> {
        public Future(Task<R> parentTask) {
            task = parentTask;
        }

        public synchronized void setResult(R res) {
            result = res;
            notifyAll();
        }

        public synchronized void setException(LightExecutionException ex) {
            exception = ex;
            notifyAll();
        }

        @Override
        public synchronized boolean isReady() {
            return result != null || exception != null;
        }

        @Override
        public synchronized R get() throws LightExecutionException, InterruptedException {
            while (!isReady()) {
                wait();
            }

            if (exception != null) {
                throw exception;
            }

            return result;
        }

        @Override
        public synchronized <T> LightFuture<T> thenApply(Function<R, T> function) {
            return task.addDependency(function);
        }

        private volatile LightExecutionException exception;
        private volatile R result;
        private final Task<R> task;
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            Task task = null;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }

                        task = tasks.poll();
                        tasks.notifyAll();
                    }

                    task.run();
                }
            } catch (InterruptedException ignored) {
                if (task != null) {
                    synchronized (task.getLightFuture()) {
                        ((Future) task.getLightFuture()).setException(new LightExecutionException(ignored));
                        task.getLightFuture().notifyAll();
                    }
                }
            }
        }
    }


}
