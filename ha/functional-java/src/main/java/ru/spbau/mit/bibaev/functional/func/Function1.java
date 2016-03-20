package ru.spbau.mit.bibaev.functional.func;

public interface Function1<ARG, RES> extends Function<ARG, RES> {
    default <R> Function1<ARG, R> compose(Function1<? super RES, R> g) {
        return arg -> g.apply(apply(arg));
    }
}
