package ru.spbau.mit.bibaev.functional.func;

import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {

    private final Predicate<Object> wrongPredicate = x -> x.equals(3);

    @Test
    public void or() {
        Predicate<Object> truePredicate = Predicate.ALWAYS_TRUE;
        Predicate<Object> falsePredicate = Predicate.ALWAYS_FALSE;

        assertTrue(truePredicate.or(falsePredicate).apply(10));
        assertTrue(falsePredicate.or(truePredicate).apply(10));
        assertTrue(truePredicate.or(truePredicate).apply(10));
        assertFalse(falsePredicate.or(falsePredicate).apply(10));

        assertTrue(truePredicate.or(wrongPredicate).apply(null));

        Predicate<String> equals10 = "10"::equals;
        Predicate<Object> equals12 = "12"::equals;
        assertFalse(equals10.or(equals12).apply("14"));
    }

    @Test
    public void and() {
        Predicate<Object> truePredicate = Predicate.ALWAYS_TRUE;
        Predicate<Object> falsePredicate = Predicate.ALWAYS_FALSE;

        assertFalse(truePredicate.and(falsePredicate).apply(10));
        assertFalse(falsePredicate.and(truePredicate).apply(10));
        assertTrue(truePredicate.and(truePredicate).apply(10));
        assertFalse(falsePredicate.and(falsePredicate).apply(10));

        assertFalse(falsePredicate.and(wrongPredicate).apply(null));

        Predicate<Object> even = x -> (int) x % 2 == 0;
        Predicate<Integer> notNull = x -> x != null;
        assertTrue(notNull.and(even).apply(10));
        assertFalse(notNull.and(even).apply(null));
        assertFalse(notNull.and(even).apply(11));
    }

    @Test
    public void not() {
        Predicate<Integer> equals10 = x -> x == 10;

        assertTrue(equals10.apply(10));
        assertFalse(equals10.not().apply(10));

        assertFalse(equals10.apply(11));
        assertTrue(equals10.not().apply(11));
    }

    @Test
    public void compose() {
        Predicate<Integer> even = x -> x % 2 == 0;
        Function1<Integer, Boolean> odd = even.compose(x -> !x);
        assertTrue(even.apply(10));
        assertFalse(odd.apply(10));
        assertTrue(odd.apply(1));

        Function1<Object, Integer> booleanToInteger = x -> (boolean)x ? 1 : 0;
        Function1<Integer, Integer> integerPredicate = even.compose(booleanToInteger);
        assertEquals(1, (int) integerPredicate.apply(10));
        assertEquals(0, (int) integerPredicate.apply(11));
    }
}
