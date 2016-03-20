package ru.spbau.mit.bibaev.FunctionalJava;

import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {

    @Test
    public void or() throws Exception {
        Predicate<Integer> truePredicate = x -> true;
        Predicate<Integer> falsePredicate = x -> false;

        assertTrue(truePredicate.or(falsePredicate).apply(10));
        assertTrue(falsePredicate.or(truePredicate).apply(10));
        assertTrue(truePredicate.or(truePredicate).apply(10));
        assertFalse(falsePredicate.or(falsePredicate).apply(10));
    }

    @Test
    public void and() throws Exception {
        Predicate<Integer> truePredicate = x -> true;
        Predicate<Integer> falsePredicate = x -> false;

        assertFalse(truePredicate.and(falsePredicate).apply(10));
        assertFalse(falsePredicate.and(truePredicate).apply(10));
        assertTrue(truePredicate.and(truePredicate).apply(10));
        assertFalse(falsePredicate.and(falsePredicate).apply(10));
    }

    @Test
    public void not() throws Exception {
        Predicate<Integer> equals10 = x -> x == 10;

        assertTrue(equals10.apply(10));
        assertFalse(equals10.not().apply(10));

        assertFalse(equals10.apply(11));
        assertTrue(equals10.not().apply(11));
    }
}
