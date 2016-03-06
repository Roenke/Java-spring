package ru.spbau.bibaev;

import java.util.AbstractList;

class node<T> {
    public node<T> next;
    public T value;
}

public class List<T> extends AbstractList {
    private node<T> head;
    private int size;

    public List() {
        size = 0;
        head = null;
    }

    @Override
    public boolean add(Object o) {
        node<T> newNode = new node<>();
        newNode.next = null;
        newNode.value = (T) o;
        if (head == null) {
            head = newNode;
        }
        else {
            node<T> ptr = head;
            while (ptr.next != null) {
                ptr = ptr.next;
            }
            ptr.next = newNode;
        }

        size++;
        return true;
    }

    @Override
    public T get(int index) {
        node<T> ptr = head;
        int i = 0;
        while (ptr != null && i != index){
            ptr = ptr.next;
            i++;
        }

        if(ptr == null) {
            return null;
        }

        return ptr.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean remove(Object o) {
        if (head == null) {
            return false;
        }

        boolean found = false;
        while(head.next != null && head.value == (T) o){
            head = head.next;
            found = true;
        }

        node<T> ptr = head;
        node<T> ptrNext = head.next;

        while (ptrNext != null){
            if (ptrNext.value.equals(o)){
                found = true;
                ptr.next = ptrNext.next;
                ptrNext = ptrNext.next;
                size--;
            }
            else{
                ptr = ptrNext;
                ptrNext = ptrNext.next;
            }
        }

        return found;
    }
}
