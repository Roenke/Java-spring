package ru.spbau.mit.bibaev.functional.func;

public interface Predicate<ARG> extends Function<ARG, Boolean> {
    default Predicate<ARG> or(Predicate<? super ARG> other) {
        return arg -> apply(arg) || other.apply(arg);
    }

    default Predicate<ARG> and(Predicate<? super ARG> other) {
        return arg -> apply(arg) && other.apply(arg);
    }

    default Predicate<ARG> not() {
        return arg -> !apply(arg);
    }

    Predicate<Object> ALWAYS_TRUE = arg -> true;
    Predicate<Object> ALWAYS_FALSE = arg -> false;
}
