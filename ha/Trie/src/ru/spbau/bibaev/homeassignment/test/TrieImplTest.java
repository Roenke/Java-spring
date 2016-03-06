package ru.spbau.bibaev.homeassignment.test;

import static org.junit.Assert.*;

import ru.spbau.bibaev.homeassignment.Trie;
import ru.spbau.bibaev.homeassignment.TrieImpl;

public class TrieImplTest {

    @org.junit.Test
    public void testAdd() throws Exception {
        String str1 = "cat";
        String str2 = "dog";

        Trie trie = new TrieImpl();
        assertTrue(trie.add(str1));
        assertEquals(1, trie.size());
        assertTrue(trie.add(str2));
        assertFalse(trie.add(str1));
        assertFalse(trie.add(str2));

        assertTrue(trie.add("ca"));
        assertTrue(trie.add("c"));
        assertTrue(trie.add(""));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        Trie trie = new TrieImpl();
        trie.add("Very");
        trie.add("VeryVery");
        trie.add("VeryVeryLong");
        trie.add("VeryVeryLongWord");

        assertTrue(trie.contains("Very"));
        assertTrue(trie.contains("VeryVery"));
        assertTrue(trie.contains("VeryVeryLong"));
        assertTrue(trie.contains("VeryVeryLongWord"));

        assertFalse(trie.contains("AnyWord"));
        assertFalse(trie.contains("Word"));
        assertFalse(trie.contains("Ve"));
        assertFalse(trie.contains("VeryV"));
        assertFalse(trie.contains("VeryVeryLongWord1"));
        assertFalse(trie.contains("VeryVeryLongWor"));

        assertFalse(trie.contains(""));
        trie.add("");
        assertTrue(trie.contains(""));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        Trie trie = new TrieImpl();
        trie.add("String");
        trie.add("Word");
        assertEquals(2, trie.size());
        assertFalse(trie.remove("YetWord"));
        assertEquals(2, trie.size());
        assertTrue(trie.remove("Word"));
        assertEquals(1, trie.size());
        assertFalse(trie.contains("Word"));

        trie.add("Word");
        trie.add("word");

        assertTrue(trie.remove("word"));
        assertEquals(2, trie.size());
        assertFalse(trie.contains("word"));

        assertFalse(trie.contains(""));
        assertFalse(trie.remove(""));
        assertEquals(2, trie.size());


        trie.add("");
        assertEquals(3, trie.size());
        assertTrue(trie.remove(""));
        assertEquals(2, trie.size());
        assertFalse(trie.contains(""));

        trie.remove("Word");
        trie.remove("String");
        assertEquals(0, trie.size());

        trie.add("Very");
        trie.add("VeryVery");
        trie.add("VeryVeryLong");
        trie.add("VeryVeryLongWord");

        assertEquals(4, trie.size());
        trie.remove("Very");
        assertEquals(3, trie.size());
        assertFalse(trie.contains("Very"));
        assertTrue(trie.contains("VeryVery"));

        trie.remove("VeryVery");
        assertEquals(2, trie.size());
        assertFalse(trie.contains("VeryVery"));
        assertTrue(trie.contains("VeryVeryLong"));

        trie.add("VeryVeryLongWord1");
        assertEquals(3, trie.size());
        trie.remove("VeryVeryLongWord");
        assertEquals(2, trie.size());
        assertTrue(trie.contains("VeryVeryLongWord1"));
    }

    @org.junit.Test
    public void testSize() throws Exception {
        Trie trie = new TrieImpl();
        assertEquals(0, trie.size());
        trie.add("String");
        assertEquals(1, trie.size());
        trie.add("String2");
        assertEquals(2, trie.size());
        trie.add("Word");
        assertEquals(3, trie.size());

        trie.remove("str");
        assertEquals(3, trie.size());

        trie.remove("String");
        assertEquals(2, trie.size());

        trie.remove("String2");
        assertEquals(1, trie.size());

        trie.remove("Word");
        assertEquals(0, trie.size());

        trie.remove("Word");
        assertEquals(0, trie.size());

        trie.add("");
        assertEquals(1, trie.size());

        trie.add("");
        assertEquals(1, trie.size());

        trie.remove("");
        assertEquals(0, trie.size());
    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix() throws Exception {
        Trie trie = new TrieImpl();
        trie.add("Very");
        trie.add("VeryVery");
        trie.add("VeryVeryLong");
        trie.add("VeryVeryLongWord");

        assertEquals(4, trie.howManyStartsWithPrefix("V"));
        assertEquals(4, trie.howManyStartsWithPrefix("Very"));
        assertEquals(0, trie.howManyStartsWithPrefix("VE"));
        assertEquals(3, trie.howManyStartsWithPrefix("VeryV"));
        assertEquals(3, trie.howManyStartsWithPrefix("VeryVery"));
        assertEquals(0, trie.howManyStartsWithPrefix("VeryVeryV"));
        assertEquals(2, trie.howManyStartsWithPrefix("VeryVeryL"));
        assertEquals(1, trie.howManyStartsWithPrefix("VeryVeryLongWord"));
        assertEquals(4, trie.howManyStartsWithPrefix(""));
    }
}
