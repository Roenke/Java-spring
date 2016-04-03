package ru.spbau.mit.bibaev.functional;

import ru.spbau.mit.bibaev.functional.func.Function1;
import ru.spbau.mit.bibaev.functional.func.Function2;
import ru.spbau.mit.bibaev.functional.func.Predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static <T, R> List<R> map(Function1<? super T, R> f, Iterable<T> collection) {
        List<R> result = new ArrayList<>();
        for (T elem : collection) {
            result.add(f.apply(elem));
        }

        return result;
    }

    public static <T> List<T> filter(Predicate<? super T> predicate, Iterable<T> collection){
        List<T> result = new ArrayList<>();
        for (T elem : collection) {
            if(predicate.apply(elem)) {
                result.add(elem);
            }
        }

        return result;
    }

    public static <T> List<T> takeWhile(Predicate<? super T> predicate, Iterable<T> collection) {
        List<T> result = new ArrayList<>();
        for (T elem : collection) {
            if (!predicate.apply(elem)){
                break;
            }

            result.add(elem);
        }

        return result;
    }

    public static <T> List<T> takeUnless(Predicate<? super T> predicate, Iterable<T> collection) {
        return takeWhile(x -> !predicate.apply(x), collection);
    }

    public static <T, R> R foldl(Function2<R, ? super T, R> foldFunction, R startValue, Iterable<T> collection) {
        R result = startValue;
        for(T elem : collection) {
            result = foldFunction.apply(result, elem);
        }

        return result;
    }

    public static <T, R> R foldr(Function2<? super T, R, R> function, R ini, Iterable<T> collection) {
        return foldrInternal(function, ini, collection.iterator());
    }

    private static <T, R> R foldrInternal(Function2<? super T, R, R> function, R ini, Iterator<T> iterator) {
        if (!iterator.hasNext()) {
            return ini;
        }

        T next = iterator.next();
        return function.apply(next, foldrInternal(function, ini, iterator));
    }
}
