package ru.spbau.mit.bibaev.functional.func;

public interface Function2<ARG1, ARG2, RES> {
    RES apply(ARG1 arg1, ARG2 arg2);

    default <R> Function2<ARG1, ARG2, R> compose(Function1<? super RES, R> g) {
        return (arg1, arg2) -> g.apply(apply(arg1, arg2));
    }

    default Function1<ARG2, RES> bind1(ARG1 arg1) {
        return arg2 -> apply(arg1, arg2);
    }

    default Function1<ARG1, RES> bind2(ARG2 arg2) {
        return arg1 -> apply(arg1, arg2);
    }

    default Function1<ARG1, Function1<ARG2, RES>> curry() {
        return arg1 -> arg2 -> apply(arg1, arg2);
    }
}
