package ru.bibaev.spbau.threadpool;

@SuppressWarnings("WeakerAccess")
public class LightExecutionException extends Exception {
    public LightExecutionException(Exception e) {
        super(e);
    }

    public LightExecutionException(String m) {
        super(m);
    }
}
