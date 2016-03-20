package ru.spbau.mit.bibaev.FunctionalJava;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function2Test {

    @Test
    public void compose() throws Exception {
        Function2<Double, Double, Double> mul = (a, b) -> a * b;
        Function1<Double, Integer> square = value -> (int) (value * value);

        assertEquals(36, mul.compose(square).apply(2.0, 3.0).intValue());
        assertEquals(0, mul.compose(square).apply(0.0, 100.0).intValue());
    }

    @Test
    public void bind1() throws Exception {
        Function2<Integer, Integer, Integer> mod = (a, b) -> a % b;
        Function1<Integer, Integer> mod144 = mod.bind1(144);

        assertEquals(0, mod144.apply(12).intValue());
        assertEquals(0, mod144.apply(2).intValue());
        assertEquals(0, mod144.apply(3).intValue());
        assertEquals(4, mod144.apply(5).intValue());
    }

    @Test
    public void bind2() throws Exception {
        Function2<Integer, Integer, Integer> div = (a, b) -> a / b;
        Function1<Integer, Integer> div2 = div.bind2(2);

        assertEquals(1, div2.apply(2).intValue());
        assertEquals(2, div2.apply(4).intValue());
        assertEquals(2, div2.apply(5).intValue());
    }

    @Test
    public void carry() throws Exception {
        Function2<Long, Long, Long> div = (a, b) -> a / b;

        assertEquals(5, div.carry().apply(10L).apply(2L).intValue());
        assertEquals(10, div.carry().apply(10L).apply(1L).intValue());
    }
}