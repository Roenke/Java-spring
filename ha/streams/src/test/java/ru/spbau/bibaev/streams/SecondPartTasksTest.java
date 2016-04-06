package ru.spbau.bibaev.streams;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

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
        Map<String, List<String>> authorsMap = new HashMap<>();
        authorsMap.put("Hello", Arrays.asList("Hello", "world"));
        assertEquals("Hello", SecondPartTasks.findPrinter(authorsMap));
    }

    @Test
    public void testCalculateGlobalOrder() {
        fail();
    }
}