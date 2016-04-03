package ru.spbau.mit.bibaev.functional;

import org.junit.Test;
import ru.spbau.mit.bibaev.functional.func.Function1;
import ru.spbau.mit.bibaev.functional.func.Function2;
import ru.spbau.mit.bibaev.functional.func.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollectionsTest {

    @Test
    public void map() {
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));

        Function1<Integer, Integer> square = value -> value * value;

        Collection<Integer> result1 = Collections.map(square, intList);
        assertEquals(Arrays.asList(1, 4, 9), result1);

        intList.add(null);
        Collection<Boolean> result2 = Collections.map(notNull, intList);
        assertEquals(Arrays.asList(true, true, true, false), result2);
    }

    @Test
    public void filter() {
        ArrayList<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Predicate<Integer> even = x -> x % 2 == 0;

        Collection<Integer> result1 = Collections.filter(even, numberList);
        assertEquals(Arrays.asList(2, 4, 6), result1);

        Collection<Integer> result2 = Collections.filter(isNull, numberList);
        assertEquals(emptyList(), result2);
    }

    @Test
    public void takeWhile() {
        ArrayList<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate<Integer> lessThenFive = x -> x < 5;

        Collection<Integer> result = Collections.takeWhile(lessThenFive, numberList);
        assertEquals(Arrays.asList(1, 2, 3, 4), result);

        assertEquals(emptyList(), Collections.takeWhile(isNull, numberList));
        assertEquals(numberList, Collections.takeWhile(notNull, numberList));
    }

    @Test
    public void takeUnless() {
        ArrayList<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate<Integer> positive = x -> x > 0;

        Collection<Integer> result = Collections.takeUnless(positive, numberList);
        assertEquals(emptyList(), result);

        assertEquals(numberList, Collections.takeUnless(isNull, numberList));
        assertEquals(emptyList(), Collections.takeUnless(notNull, numberList));
    }

    @Test
    public void foldl() {
        ArrayList<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 5));
        Function2<Double, Integer, Double> diff = (l, r) -> l - r;

        assertEquals(-11, Collections.foldl(diff, 0.0, numberList).intValue());

        assertTrue(Collections.foldl(notNullFold, true, numberList));
        numberList.add(null);
        assertFalse(Collections.foldl(notNullFold, true, numberList));
    }

    @Test
    public void foldr() {
        ArrayList<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 5));
        Function2<Integer, Double, Double> diff = (l, r) -> l - r;
        assertEquals(-3, Collections.foldr(diff, 0.0, numberList).intValue());

        assertTrue(Collections.foldl(notNullFold, true, numberList));
        numberList.add(null);
        assertFalse(Collections.foldl(notNullFold, true, numberList));
    }

    private static final Predicate<Object> isNull = x -> x == null;
    private static final Predicate<Object> notNull = isNull.not();

    private static final Function2<Object, Object, Boolean> notNullFold = (l, r) -> l != null && r != null;
}
