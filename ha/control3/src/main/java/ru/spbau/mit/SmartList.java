package ru.spbau.mit;

import java.util.*;

public class SmartList<E> extends AbstractList<E> {
    public SmartList() {
    }

    public SmartList(Collection<E> collection) {
        addAll(collection);
    }

    @Override
    public boolean add(E e) {
        if (size == 0) {
            data = e;
        } else if (size < 5) {
            if (size == 1) {
                Object[] newArray = new Object[5];
                newArray[0] = data;
                data = newArray;
            }

            ((Object[]) data)[size] = e;
        } else {
            if (size == 5) {
                ArrayList<Object> newList = new ArrayList<>();
                Object[] dataCasted = (Object[]) data;
                Collections.addAll(newList, dataCasted);

                data = newList;
            }

            ((ArrayList) data).add(e);
        }

        size++;

        return true;
    }

    public E get(int index) {
        if (size == 1) {
            return (E) data;
        }
        if (size <= 5) {
            return (E)((Object[]) data)[index];
        }

        return (E)(((ArrayList) data).get(index));
    }

    @Override
    public E set(int index, E element) {
        Object old;
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (size == 1) {
            old = data;
            data = element;
        } else if (size <= 5) {
            old = ((Object[]) data)[index];
            ((Object[]) data)[index] = element;
        } else {
            old = ((ArrayList) data).get(index);
            ((ArrayList<Object>) data).set(index, element);
        }

        return (E) old;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (index == size) {
            add(element);
            return;
        }

        if (size < 5) {
            if (size == 1) {
                Object[] array = new Object[5];
                if (index == 0) {
                    array[0] = data;
                }
                data = array;
            }
            Object[] array = (Object[]) data;
            System.arraycopy(array, index, array, index + 1, size - index);

            array[index] = element;
        } else {
            if(size == 5) {
                Object[] oldArray = (Object[])data;
                ArrayList<Object> array = new ArrayList<>();
                array.addAll(Arrays.asList(oldArray).subList(0, size));

                data = array;
            }

            ((ArrayList)data).add(index, element);
        }

        size++;
    }

    @Override
    public E remove(int index) {
        Object res;
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (size == 1) {
            res = data;
            data = null;
        } else if (size <= 5) {
            Object[] array = (Object[]) data;
            res = array[index];
            array[index] = null;

            System.arraycopy(array, index + 1, array, index, size - 1 - index);
            if (size == 2) {
                data = array[0];
            }
        } else {
            ArrayList array = (ArrayList) data;
            res = array.remove(index);
            if (size == 6) {
                data = array.toArray();
            }
        }

        size--;

        return (E) res;
    }

    public int size() {
        return size;
    }

    private int size;
    private Object data;
}
