package ru.spbau.bibaev.streams;

import org.junit.Test;

import static org.junit.Assert.*;

public class SecondPartTasksTest {
    private static final double EPS = 1e-2;

    @Test
    public void testFindQuotes() {
        fail();
    }

    @Test
    public void testPiDividedBy4() {
        double result = SecondPartTasks.piDividedBy4();
        assertEquals(Math.PI / 4.0, result, EPS);
    }

    @Test
    public void testFindPrinter() {
        fail();
    }

    @Test
    public void testCalculateGlobalOrder() {
        fail();
    }
}