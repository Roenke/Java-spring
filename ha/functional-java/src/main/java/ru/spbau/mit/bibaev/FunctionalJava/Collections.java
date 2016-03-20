package ru.spbau.mit.bibaev.FunctionalJava;

import java.util.ArrayList;
import java.util.Iterator;

public class Collections {
    public static <T, R> Iterable<R> map(Function1<T, R> f, Iterable<T> collection) {
        ArrayList<R> result = new ArrayList<>();
        for (T elem : collection) {
            result.add(f.apply(elem));
        }

        return result;
    }

    public static <T> Iterable<T> filter(Predicate<T> predicate, Iterable<T> collection){
        ArrayList<T> result = new ArrayList<>();
        for (T elem : collection) {
            if(predicate.apply(elem)) {
                result.add(elem);
            }
        }

        return result;
    }

    public static <T> Iterable<T> takeWhile(Predicate<T> predicate, Iterable<T> collection) {
        ArrayList<T> result = new ArrayList<>();
        for (T elem : collection) {
            if (!predicate.apply(elem)){
                break;
            }

            result.add(elem);
        }

        return result;
    }

    public static <T> Iterable<T> takeUnless(Predicate<T> predicate, Iterable<T> collection) {
        ArrayList<T> result = new ArrayList<>();
        for (T elem : collection) {
            if (predicate.apply(elem)){
                break;
            }

            result.add(elem);
        }

        return result;
    }

    public static <T, R> R foldl(Function2<R, T, R> foldFunction, R startValue, Iterable<T> collection) {
        R result = startValue;
        for(T elem : collection) {
            result = foldFunction.apply(result, elem);
        }

        return result;
    }

    public static <T, R> R foldr(Function2<T, R, R> function, R ini, Iterable<T> collection) {
        return foldrInternal(function, ini, collection.iterator());
    }

    private static <T, R> R foldrInternal(Function2<T, R, R> function, R ini, Iterator<T> iterator) {
        if (!iterator.hasNext()) {
            return ini;
        }

        T next = iterator.next();
        return function.apply(next, foldrInternal(function, ini, iterator));
    }
}
