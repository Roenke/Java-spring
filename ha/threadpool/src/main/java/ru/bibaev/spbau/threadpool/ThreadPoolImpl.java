package ru.bibaev.spbau.threadpool;

import java.util.*;
import java.util.function.Function;
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
            for (Task task : tasks) {
                synchronized (task.getLightFuture()) {
                    ((Task.Future) task.getLightFuture())
                            .setException(new LightExecutionException("Thread pool is over."));
                    task.getLightFuture().notifyAll();
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
        Task<R> task = new Task<>(supplier);
        synchronized (tasks) {
            tasks.add(task);
            tasks.notifyAll();
        }
        return task.getLightFuture();
    }

    private final Queue<Task> tasks = new LinkedList<>();
    private final Collection<Thread> executors = new ArrayList<>();

    private class Task<R> implements Runnable {
        private class Future implements LightFuture<R> {
            public Future(Task parentTask) {
                task = parentTask;
            }

            public void setResult(R res) {
                result = res;
            }

            public void setException(LightExecutionException ex) {
                exception = ex;
            }

            @Override
            public boolean isReady() {
                return result != null || exception != null;
            }

            @Override
            public synchronized R get() throws LightExecutionException {
                if (exception != null) {
                    throw exception;
                }

                try {
                    while (!isReady()) {
                        wait();
                    }
                } catch (InterruptedException ignored) {
                }

                return result;
            }

            @Override
            public synchronized <T> LightFuture<T> thenApply(Function<R, T> function) {
                Supplier<T> newSupplier = () -> function.apply(result);
                Task<T> newTask = new Task<>(newSupplier);
                if (!isReady()) {
                    task.addDep(newTask);
                } else {
                    if (exception == null) {
                        synchronized (tasks) {
                            tasks.add(newTask);
                            tasks.notifyAll();
                        }
                    } else {
                        newTask.fail();
                    }
                }


                return newTask.getLightFuture();
            }

            private LightExecutionException exception;
            private R result;
            private final Task task;
        }

        public Task(Supplier<R> sup) {
            supplier = sup;
        }

        public LightFuture<R> getLightFuture() {
            return future;
        }

        public void fail() {
            synchronized (future) {
                future.setException(new LightExecutionException("Source future not completed."));
                future.notifyAll();
            }
        }

        @Override
        public void run() {
            try {
                R res = supplier.get();
                synchronized (future) {
                    future.setResult(res);
                    future.notifyAll();
                }

                synchronized (tasks) {
                    tasks.addAll(depends);
                    tasks.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
                synchronized (future) {
                    future.setException(new LightExecutionException(e));
                    future.notifyAll();
                }
                depends.forEach(Task::fail);
            }
        }

        public void addDep(Task task) {
            depends.add(task);
        }

        private final Future future = new Future(this);
        private final Supplier<R> supplier;
        private final List<Task> depends = new ArrayList<>();
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
                    synchronized (task.getLightFuture()) {
                        task.getLightFuture().notifyAll();
                    }
                }
            } catch (InterruptedException ignored) {
                if (task != null) {
                    synchronized (task.getLightFuture()) {
                        ((Task.Future) task.getLightFuture()).setException(new LightExecutionException(ignored));
                        task.getLightFuture().notifyAll();
                    }
                }
            }
        }
    }
}
