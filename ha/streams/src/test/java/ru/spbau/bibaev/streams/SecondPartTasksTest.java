package ru.spbau.bibaev.streams;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {
    private static final double EPS = 1e-2;
    private static final String TEST_FILE_1 = "test1.txt";
    private static final String TEST_FILE_2 = "test2.txt";
    private static final String TEST_FILE_3 = "test3.txt";
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    @Test(expected = UncheckedIOException.class)
    public void testFindQuotesFileNotFound() {
        List<String> filenames = Arrays.asList("/dev/null", "/dev/random");
        SecondPartTasks.findQuotes(filenames, "abc");
    }

    @Test()
    public void testFindQuotes() {
        String subString1 = "a";
        List<String> lines = SecondPartTasks
                .findQuotes(Collections.singletonList(getAbsolutePathByResourceName(TEST_FILE_1)), subString1);
        long count = lines.stream().peek(o -> assertTrue(o.contains(subString1))).count();
        assertEquals(2, count);
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

    private String getAbsolutePathByResourceName(String name) {
        URL resource = ClassLoader.getSystemClassLoader().getResource(name);
        if(resource == null) {
            return null;
        }

        String path = resource.getPath();
        if (IS_WINDOWS) {
            path = path.substring(1);
        }

        return path;
    }
}