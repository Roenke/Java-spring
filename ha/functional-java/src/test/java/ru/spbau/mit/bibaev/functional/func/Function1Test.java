package ru.spbau.mit.bibaev.functional.func;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {

    @Test
    public void compose() throws Exception {
        Function1<Integer, Integer> square = value -> value * value;
        Function1<Integer, Integer> prev = value -> value - 1;

        assertEquals(3, (int)square.compose(prev).apply(2));
        assertEquals(1, (int)prev.compose(square).apply(2));
    }
}
