package ru.spbau.mit.bibaev.functional.func;

import org.junit.Test;

import javax.swing.text.StyledEditorKit;

import static org.junit.Assert.*;

public class Function1Test {
    @Test
    public void compose() {
        Function1<Integer, Integer> square = value -> value * value;
        Function1<Integer, Integer> prev = value -> value - 1;

        assertEquals(3, (int) square.compose(prev).apply(2));
        assertEquals(1, (int) prev.compose(square).apply(2));

        Function1<Object, Boolean> notNull = x -> x != null;
        Function1<Integer, Integer> toNull = x -> null;
        assertFalse(toNull.compose(notNull).apply(10));
    }
}
