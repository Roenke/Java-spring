package ru.spbau.bibaev.streams;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Interner;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

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
        // TODO: Move string values to class private final fields.

        List<Map<String, Integer>> orders =
                Arrays.asList(
                        ImmutableMap.of(
                                "apples", 3,
                                "banana", 5,
                                "lemon", 4
                        ),
                        ImmutableMap.of(
                                "apples", 2,
                                "banana", 10,
                                "pineapple", 12
                        )
                );
        Map<String, Integer> result = SecondPartTasks.calculateGlobalOrder(orders);

        assertEquals(4, result.size());
        assertEquals(5, result.get("apples").intValue());
        assertEquals(15, result.get("banana").intValue());
        assertEquals(4, result.get("lemon").intValue());
        assertEquals(12, result.get("pineapple").intValue());
    }
}