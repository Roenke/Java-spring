package ru.spbau.mit.bibaev.FunctionalJava;

import org.junit.Test;
import ru.spbau.mit.bibaev.FunctionalJava.Function1;

import static org.junit.Assert.*;

public class Function1Test {

    @Test
    public void compose() throws Exception {
        Function1<Integer, Integer> square = value -> value * value;
        Function1<Integer, Integer> prev = value -> value - 1;

        assertEquals(3, (long)square.compose(prev).apply(2));
        assertEquals(1, (long)prev.compose(square).apply(2));
    }
}