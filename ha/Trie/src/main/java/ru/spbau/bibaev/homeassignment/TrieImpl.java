package ru.spbau.bibaev.homeassignment;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TrieImpl implements Trie, StreamSerializable {
    private Node root;

    public TrieImpl() {
        root = new Node();
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        root.serialize(out);
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        Node node = new Node();
        node.deserialize(in);
        root = node;
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
            Node node = new Node();
            current.children.put(element.charAt(i), node);
            current = node;
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
            current.endsCountInSubtree--;
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

    private static class Node implements StreamSerializable {
        private boolean isEndOfSomeElement = false;
        private int endsCountInSubtree = 0;
        private final HashMap<Character, Node> children = new HashMap<>();

        @Override
        public void serialize(OutputStream out) throws IOException {
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            dataOutputStream.writeBoolean(isEndOfSomeElement);
            dataOutputStream.writeInt(endsCountInSubtree);

            dataOutputStream.writeInt(children.size());
            for(Map.Entry<Character, Node> mapEntry : children.entrySet()) {
                dataOutputStream.writeChar(mapEntry.getKey());
                mapEntry.getValue().serialize(out);
            }

            dataOutputStream.flush();
        }

        @Override
        public void deserialize(InputStream in) throws IOException {
            DataInputStream dataInputStream = new DataInputStream(in);
            isEndOfSomeElement = dataInputStream.readBoolean();
            endsCountInSubtree = dataInputStream.readInt();
            children.clear();
            int childrenCount = dataInputStream.readInt();
            for (int i = 0; i < childrenCount; i++) {
                char ch = dataInputStream.readChar();
                Node node = new Node();
                node.deserialize(in);
                children.put(ch, node);
            }
        }
    }
}
