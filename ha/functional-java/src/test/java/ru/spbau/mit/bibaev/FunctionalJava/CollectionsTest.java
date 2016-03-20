package ru.spbau.mit.bibaev.FunctionalJava;

import org.junit.Test;
import ru.spbau.mit.bibaev.FunctionalJava.Collections;
import ru.spbau.mit.bibaev.FunctionalJava.Function1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class CollectionsTest {

    @Test
    public void map() throws Exception {
        ArrayList<Integer> intList = new ArrayList<>();
        intList.add(1);
        intList.add(2);
        intList.add(3);

        Function1<Integer, Integer> square = value -> value * value;

        Collection<Integer> result = iterableToCollection(Collections.map(square, intList));
        Integer[] expected = new Integer[]{1, 4, 9};

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected)));
    }

    @Test
    public void filter() throws Exception {

    }

    @Test
    public void takeWhile() throws Exception {

    }

    @Test
    public void takeUnless() throws Exception {

    }

    @Test
    public void foldl() throws Exception {

    }

    @Test
    public void foldr() throws Exception {

    }

    private static <T> Collection<T> iterableToCollection(Iterable<T> iterable) {
        Collection<T> collection = new ArrayList<>();
        for (T elem : iterable) {
            collection.add(elem);
        }

        return collection;
    }
}