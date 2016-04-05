package ru.spbau.mit.bibaev.functional;

import org.junit.Test;
import ru.spbau.mit.bibaev.functional.func.Function1;
import ru.spbau.mit.bibaev.functional.func.Function2;
import ru.spbau.mit.bibaev.functional.func.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollectionsTest {

    @Test
    public void map() {
        List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));

        Function1<Integer, Integer> square = value -> value * value;

        Collection<Integer> result1 = Collections.map(square, intList);
        assertEquals(Arrays.asList(1, 4, 9), result1);

        intList.add(null);
        Collection<Boolean> result2 = Collections.map(NOT_NULL_PREDICATE, intList);
        assertEquals(Arrays.asList(true, true, true, false), result2);
    }

    @Test
    public void filter() {
        List<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Predicate<Integer> even = x -> x % 2 == 0;

        Collection<Integer> result1 = Collections.filter(even, numberList);
        assertEquals(Arrays.asList(2, 4, 6), result1);

        Collection<Integer> result2 = Collections.filter(IS_NULL_PREDICATE, numberList);
        assertEquals(emptyList(), result2);
    }

    @Test
    public void takeWhile() {
        List<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate<Integer> lessThenFive = x -> x < 5;

        Collection<Integer> result = Collections.takeWhile(lessThenFive, numberList);
        assertEquals(Arrays.asList(1, 2, 3, 4), result);

        assertEquals(emptyList(), Collections.takeWhile(IS_NULL_PREDICATE, numberList));
        assertEquals(numberList, Collections.takeWhile(NOT_NULL_PREDICATE, numberList));
    }

    @Test
    public void takeUnless() {
        List<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate<Integer> positive = x -> x > 0;

        Collection<Integer> result = Collections.takeUnless(positive, numberList);
        assertEquals(emptyList(), result);

        assertEquals(numberList, Collections.takeUnless(IS_NULL_PREDICATE, numberList));
        assertEquals(emptyList(), Collections.takeUnless(NOT_NULL_PREDICATE, numberList));
    }

    @Test
    public void foldl() {
        List<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 5));
        Function2<Double, Integer, Double> diff = (l, r) -> l - r;

        assertEquals(-11, Collections.foldl(diff, 0.0, numberList).intValue());

        assertTrue(Collections.foldl(NOT_NULL_FOLD, true, numberList));
        numberList.add(null);
        assertFalse(Collections.foldl(NOT_NULL_FOLD, true, numberList));
    }

    @Test
    public void foldr() {
        List<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 5));
        Function2<Integer, Double, Double> diff = (l, r) -> l - r;
        assertEquals(-3, Collections.foldr(diff, 0.0, numberList).intValue());

        assertTrue(Collections.foldl(NOT_NULL_FOLD, true, numberList));
        numberList.add(null);
        assertFalse(Collections.foldl(NOT_NULL_FOLD, true, numberList));
    }

    private static final Predicate<Object> IS_NULL_PREDICATE = x -> x == null;
    private static final Predicate<Object> NOT_NULL_PREDICATE = IS_NULL_PREDICATE.not();

    private static final Function2<Object, Object, Boolean> NOT_NULL_FOLD = (l, r) -> l != null && r != null;
}
