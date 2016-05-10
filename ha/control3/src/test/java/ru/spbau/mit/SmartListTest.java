package ru.spbau.mit;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class SmartListTest {

    @Test
    public void testSimple() {
        List<Integer> list = newList();

        assertEquals(Collections.<Integer>emptyList(), list);

        list.add(1);
        assertEquals(Collections.singletonList(1), list);

        list.add(2);
        assertEquals(Arrays.asList(1, 2), list);
    }

    @Test
    public void testGetSet() {
        List<Object> list = newList();

        list.add(1);

        assertEquals(1, list.get(0));
        assertEquals(1, list.set(0, 2));
        assertEquals(2, list.get(0));
        assertEquals(2, list.set(0, 1));

        list.add(2);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));

        assertEquals(1, list.set(0, 2));

        assertEquals(Arrays.asList(2, 2), list);
    }

    @Test
    public void testRemove() throws Exception {
        List<Object> list = newList();

        list.add(1);
        list.remove(0);
        assertEquals(Collections.emptyList(), list);

        list.add(2);
        list.remove((Object) 2);
        assertEquals(Collections.emptyList(), list);

        list.add(1);
        list.add(2);
        assertEquals(Arrays.asList(1, 2), list);

        list.remove(0);
        assertEquals(Collections.singletonList(2), list);

        list.remove(0);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testIteratorRemove() throws Exception {
        List<Object> list = newList();
        assertFalse(list.iterator().hasNext());

        list.add(1);

        Iterator<Object> iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(Collections.emptyList(), list);

        list.addAll(Arrays.asList(1, 2));

        iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());

        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(Collections.singletonList(2), list);
        assertEquals(2, iterator.next());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(Collections.emptyList(), list);
    }


    @Test
    public void testCollectionConstructor() throws Exception {
        assertEquals(Collections.emptyList(), newList(Collections.emptyList()));
        assertEquals(
                Collections.singletonList(1),
                newList(Collections.singletonList(1)));

        assertEquals(
                Arrays.asList(1, 2),
                newList(Arrays.asList(1, 2)));
    }

    @Test
    public void testAddManyElementsThenRemove() throws Exception {
        List<Object> list = newList();
        for (int i = 0; i < 7; i++) {
            list.add(i + 1);
        }

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), list);

        for (int i = 0; i < 7; i++) {
            list.remove(list.size() - 1);
            assertEquals(6 - i, list.size());
        }

        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testIndexAdd() {
        List<Integer> list = newList();
        list.add(0, 4);
        assertEquals(4, list.get(0).intValue());
        list.add(0, 5);
        assertEquals(5, list.get(0).intValue());
        assertEquals(4, list.get(1).intValue());
        list.add(1, 3);
        assertEquals(Arrays.asList(5, 3, 4), list);
        list.add(0, 10);
        list.add(3, 15);
        assertEquals(Arrays.asList(10, 5, 3, 15, 4), list);
        list.add(3, 30);
        assertEquals(Arrays.asList(10, 5, 3, 30, 15, 4), list);
    }

    @Test
    public void testRemoveMy() {
        List<Integer> list = newList(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        list.remove(0);
        assertEquals(Arrays.asList(2, 3, 4, 5, 6, 7), list);
        list.remove(2);
        assertEquals(Arrays.asList(2, 3, 5, 6, 7), list);
        list.remove(4);
        assertEquals(Arrays.asList(2, 3, 5, 6), list);
        list.remove(2);
        assertEquals(Arrays.asList(2, 3, 6), list);
        list.remove(0);
        assertEquals(Arrays.asList(3, 6), list);
        list.add(123);
        list.add(0, 1);
        assertEquals(Arrays.asList(1, 3, 6, 123), list);
    }

    @Test
    public void testClear(){
        List<Integer> list = newList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        assertEquals(4, list.size());

        list.clear();
        assertEquals(0, list.size());
    }

    private static <T> List<T> newList() {
        try {
            return (List<T>) getListClass().getConstructor().newInstance();
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> newList(Collection<T> collection) {
        try {
            return (List<T>) getListClass().getConstructor(Collection.class).newInstance(collection);
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getListClass() throws ClassNotFoundException {
        return Class.forName("ru.spbau.mit.SmartList");
    }
}
