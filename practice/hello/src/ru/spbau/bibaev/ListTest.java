package ru.spbau.bibaev;

import org.junit.Assert;

import static org.junit.Assert.*;

public class ListTest {

    @org.junit.Test
    public void testGet() throws Exception {
        List<Integer> list = new List<>();
        list.add(1);
        assert list.get(0) == 1;
        assert list.size() == 1;
        list.add(2);
        assert list.get(0) == 1;
        assert list.get(1) == 2;
        assert list.size() == 2;
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        List<Integer> list = new List<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(4);
        list.add(4);
        list.add(4);
        list.add(4);
        assert !list.remove((Integer) 0);
        if (!list.remove((Integer) 1)) throw new AssertionError();
        assert list.size() == 7;
        list.remove((Integer)4);
        assert list.size() == 3;
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