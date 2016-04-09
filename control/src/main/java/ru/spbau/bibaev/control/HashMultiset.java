package ru.spbau.bibaev.control;

import java.util.*;

@SuppressWarnings("unused")
public class HashMultiset<E> extends AbstractSet<E> implements Multiset<E> {

    @Override
    public int count(Object element) {
        return map.getOrDefault(element, 0);
    }

    @Override
    public Set<E> elementSet() {
        return map.keySet();
    }

    @Override
    public Set<Entry<E>> entrySet() {
        return new AbstractSet<Entry<E>>() {
            @Override
            public Iterator<Entry<E>> iterator() {
                return new Iterator<Entry<E>>() {
                    private int currentCount;

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public Entry<E> next() {
                        final Entry<E> entry = new Entry<E>() {
                            private final Map.Entry<E, Integer> next = iterator.next();

                            @Override
                            public E getElement() {
                                return next.getKey();
                            }

                            @Override
                            public int getCount() {
                                return next.getValue();
                            }
                        };

                        currentCount = entry.getCount();
                        return entry;
                    }

                    @Override
                    public void remove() {
                        iterator.remove();
                        size -= currentCount;
                    }
                };
            }

            @Override
            public int size() {
                return map.size();
            }

            private final Iterator<Map.Entry<E, Integer>> iterator = map.entrySet().iterator();
        };
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return elementCount > 0 || iterator.hasNext();
            }

            @Override
            public E next() {
                wasDeleted = false;
                if (elementCount == 0) {
                    Map.Entry<E, Integer> next = iterator.next();
                    elementCount = next.getValue();
                    current = next.getKey();
                }
                --elementCount;

                return current;
            }

            @Override
            public void remove() {
                if (wasDeleted || current == null) {
                    throw new IllegalStateException();
                }

                int count = map.getOrDefault(current, 0);
                if (count == 1) {
                    iterator.remove();
                } else {
                    map.put(current, count - 1);
                }
                size--;
                wasDeleted = true;
            }

            private boolean wasDeleted = false;
            private E current = null;
            private int elementCount = 0;
            private Iterator<Map.Entry<E, Integer>> iterator = map.entrySet().iterator();
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        int count = map.getOrDefault(e, 0);

        map.put(e, count + 1);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int count = map.getOrDefault(o, 0);

        if (count == 0) {
            return false;
        }

        if (count == 1) {
            map.remove(o);
        }
        else{
            map.put((E) o, count - 1);
        }

        size--;
        return true;
    }

    @Override
    public void clear() {
        map.clear();
        size = 0;
    }

    private final LinkedHashMap<E, Integer> map = new LinkedHashMap<>();
    private int size = 0;
}
