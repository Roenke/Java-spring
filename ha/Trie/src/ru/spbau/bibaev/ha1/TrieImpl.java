package ru.spbau.bibaev.ha1;

import java.util.HashMap;

public class TrieImpl implements Trie {

    private class Node {
        boolean isEndOfSomeElement = false;
        int endsCountInSubtree = 0;
        HashMap<Character, Node> children = new HashMap<>();
    }

    private Node root;

    public TrieImpl() {
        root = new Node();
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        int i = 0;
        Node current = root;
        while (i < element.length() && current.children.containsKey(element.charAt(i))) {
            current.endsCountInSubtree++;
            current = current.children.get(element.charAt(i));
            i++;
        }

        while (i < element.length()) {
            current.endsCountInSubtree++;
            current.children.put(element.charAt(i), new Node());
            current = current.children.get(element.charAt(i));
            i++;
        }

        current.endsCountInSubtree++;
        current.isEndOfSomeElement = true;
        return true;
    }

    @Override
    public boolean contains(String element) {
        Node node = find(element);
        return node != null && node.isEndOfSomeElement;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        int i = 0;
        Node current = root;
        while (i < element.length() &&
                current.children.get(element.charAt(i)).endsCountInSubtree != 1) {
            current.endsCountInSubtree--;
            current = current.children.get(element.charAt(i));
            i++;
        }

        if (i == element.length()) {
            current.isEndOfSomeElement = false;
        }
        else {
            current.children.remove(element.charAt(i));
            current.endsCountInSubtree--;
        }

        return true;
    }

    @Override
    public int size() {
        return root.endsCountInSubtree;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node node = find(prefix);

        return node == null ? 0 : node.endsCountInSubtree;
    }

    private Node find(String prefix) {
        Node current = root;
        int i = 0;
        while (i < prefix.length() && current != null) {
            current = current.children.get(prefix.charAt(i));
            i++;
        }

        return current;
    }
}
