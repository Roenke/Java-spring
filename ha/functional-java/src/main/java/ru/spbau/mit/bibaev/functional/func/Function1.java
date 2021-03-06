package ru.spbau.mit.bibaev.functional.func;

public interface Function1<ARG, RES> {
    RES apply(ARG arg);

    default <R> Function1<ARG, R> compose(Function1<? super RES, R> g) {
        return arg -> g.apply(apply(arg));
    }
}
