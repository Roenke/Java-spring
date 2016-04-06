package ru.spbau.bibaev.control;

import java.util.*;

public class HashMultiset<E> extends AbstractSet<E> implements Multiset<E> {
    @Override
    public int count(Object element) {
        return elementCountMapping.getOrDefault(element, 0);
    }

    @Override
    public Set<E> elementSet() {
        // TODO fix it.
        return elementCountMapping.keySet();
    }

    @Override
    public Set<Entry<E>> entrySet() {
        return new AbstractSet<Entry<E>>() {
            @Override
            public Iterator<Entry<E>> iterator() {
                return new Iterator<Entry<E>>() {

                    private final Iterator<Map.Entry<E, Integer>> iterator = elementCountMapping.entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return iterator().hasNext();
                    }

                    @Override
                    public Entry<E> next() {
                        return new Entry<E>() {

                            private final Map.Entry<E, Integer> entry = iterator.next();

                            @Override
                            public E getElement() {
                                return entry.getKey();
                            }

                            @Override
                            public int getCount() {
                                return entry.getValue();
                            }
                        };
                    }
                };
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<Map.Entry<E, Integer>> iterator = elementCountMapping.entrySet().iterator();

            private E currElem = null;
            private int currCount = 0;

            private boolean wasRemoved = false;

            @Override
            public boolean hasNext() {
                return currCount> 0 || iterator.hasNext();
            }

            @Override
            public E next() {
                wasRemoved = false;
                if (currCount > 0) {
                    currCount--;
                    return currElem;
                }

                final Map.Entry<E, Integer> next = iterator.next();
                currElem = next.getKey();
                currCount = next.getValue();
                currCount--;
                return currElem;
            }

            @Override
            public void remove() {
                if (wasRemoved) {
                    throw new IllegalStateException("Element already removed");
                }
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        return elementCountMapping.getOrDefault(o, 0) != 0;
    }

    @Override
    public boolean add(E e) {
        final int count = elementCountMapping.getOrDefault(e, 0);
        elementCountMapping.put(e, count + 1);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        final Integer count = elementCountMapping.getOrDefault(o, 0);
        if (count == 0) {
            return false;
        }
        if (count == 1) {
            elementCountMapping.remove(o);
        } else {
            elementCountMapping.put((E) o, count - 1);
        }

        size--;
        return true;
    }

    @Override
    public void clear() {
        elementCountMapping.clear();
        size = 0;
    }

    private final LinkedHashMap<E, Integer> elementCountMapping = new LinkedHashMap<E, Integer>();
    private int size = 0;

    private final class HashMultisetEntry<E> implements Entry<E> {

        public HashMultisetEntry(E elem, int c) {
            element = elem;
            count = c;
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        private E element;
        private int count;
    }

    private final class EntrySetIterator<E> implements Iterator<Entry<E>> {
        public EntrySetIterator(Set<E> elems, HashMultiset<E> parent) {
            internalIterator = elems.iterator();
            parentMultiset = parent;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Entry<E> next() {
            current = internalIterator.next();
            return new HashMultisetEntry<E>(current, parentMultiset.count(current));
        }

        @Override
        public void remove() {
            parentMultiset.remove(current);
        }

        private E current;
        private HashMultiset<E> parentMultiset;
        private Iterator<E> internalIterator;
    }

    private final class HashMultisetIterator<E> implements Iterator<E> {
        public HashMultisetIterator(Set<E> keySet, HashMultiset<E> multiset) {
            internalIterator = keySet.iterator();
            parentMultiset = multiset;
        }

        @Override
        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        @Override
        public E next() {
            if (yetCount == 0) {
                currentElem = internalIterator.next();
                yetCount = parentMultiset.count(currentElem);
            }

            yetCount--;
            return currentElem;
        }

        @Override
        public void remove() {
            parentMultiset.remove(currentElem);
        }

        private E currentElem = null;
        private int yetCount = 0;
        private Iterator<E> internalIterator;
        private HashMultiset<E> parentMultiset;
    }
}
