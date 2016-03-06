package ru.spbau.bibaev;

import java.util.AbstractList;

import static org.junit.Assert.*;

public class ListTest {

    @org.junit.Test
    public void testGet() throws Exception {
        List<Integer> list = new List<>();
        list.add(1);
        assertEquals((Integer) 1, list.get(0));
        list.add(2);
        assertEquals(new Integer(2), list.get(1));
        assertEquals(2, list.size());
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        AbstractList<Integer> list= new List<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(4);
        list.add(4);
        list.add(4);
        list.add(4);
        assertFalse(list.remove(new Integer(0)));
        assertEquals(8, list.size());
        assertTrue(list.remove(new Integer(4)));
        assertEquals(3, list.size());
    }

    @org.junit.Test
    public void testSize() throws Exception {
        List<Integer> list = new List<>();
        assert list.size() == 0;
        list.add(1);
        list.add(2);
        assert list.size() == 2;
        list.add(3);
        list.add(4);
        assert list.size() == 4;
    }
}