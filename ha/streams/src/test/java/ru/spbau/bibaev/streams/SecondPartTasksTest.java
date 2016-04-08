package ru.spbau.bibaev.streams;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SecondPartTasksTest {
    @Test(expected = UncheckedIOException.class)
    public void testFindQuotesFileNotFound() {
        List<String> filenames = Arrays.asList("file", "file1");
        SecondPartTasks.findQuotes(filenames, "abc");
    }

    @Test()
    public void testFindQuotes() {
        Function<String, List<String>> find = input -> SecondPartTasks.findQuotes(Arrays.asList(
                getAbsolutePathByResourceName(TEST_FILE_1),
                getAbsolutePathByResourceName(TEST_FILE_2),
                getAbsolutePathByResourceName(TEST_FILE_3)
        ), input);
        // check no match
        long count = find.apply("unknown quote").stream().count();
        assertEquals(0, count);

        // check match line
        count = find.apply("ccc").stream().peek(o -> assertTrue(o.contains("ccc"))).count();
        assertEquals(1, count);

        // check match one word in line
        count = find.apply("word").stream().peek(o -> assertTrue(o.contains("word"))).count();
        assertEquals(1, count);

        // check match as substring of some line
        count = find.apply("out").stream().peek(o -> assertTrue(o.contains("out"))).count();
        assertEquals(1, count);
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
        Map<String, Integer> result = SecondPartTasks.calculateGlobalOrder(ORDERS);

        assertEquals(4, result.size());
        assertEquals(5, result.get("apples").intValue());
        assertEquals(15, result.get("banana").intValue());
        assertEquals(4, result.get("lemon").intValue());
        assertEquals(12, result.get("pineapple").intValue());
    }

    private String getAbsolutePathByResourceName(String name) {
        URL resource = ClassLoader.getSystemClassLoader().getResource(name);
        if (resource == null) {
            return null;
        }

        String path = resource.getPath();
        if (IS_WINDOWS) {
            path = path.substring(1);
        }

        return path;
    }

    private static final double EPS = 1e-2;
    private static final String TEST_FILE_1 = "test1.txt";
    private static final String TEST_FILE_2 = "test2.txt";
    private static final String TEST_FILE_3 = "test3.txt";
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    private static final List<Map<String, Integer>> ORDERS =
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
}
