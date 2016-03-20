package ru.spbau.mit.bibaev.FunctionalJava;

public interface Predicate<ARG> extends Function1<ARG, Boolean> {
    default Predicate<ARG> or(Predicate<ARG> other) {
        return arg -> apply(arg) || other.apply(arg);
    }

    default Predicate<ARG> and(Predicate<ARG> other) {
        return arg -> apply(arg) && other.apply(arg);
    }

    default Predicate<ARG> not() {
        return arg -> !apply(arg);
    }

    Predicate<Object> ALWAYS_TRUE = (arg -> true);
    Predicate<Object> ALWAYS_FALSE = (arg -> false);
}
