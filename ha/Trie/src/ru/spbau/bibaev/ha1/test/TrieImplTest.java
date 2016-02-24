package ru.spbau.bibaev.ha1.test;

import org.junit.Assert;
import ru.spbau.bibaev.ha1.Trie;
import ru.spbau.bibaev.ha1.TrieImpl;

public class TrieImplTest {

    @org.junit.Test
    public void testAdd() throws Exception {
        String str1 = "cat";
        String str2 = "dog";

        Trie trie = new TrieImpl();
        Assert.assertTrue(trie.add(str1));
        Assert.assertTrue(trie.add(str2));
        Assert.assertFalse(trie.add(str1));
        Assert.assertFalse(trie.add(str2));

        Assert.assertTrue(trie.add("ca"));
        Assert.assertTrue(trie.add("c"));
        Assert.assertTrue(trie.add(""));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        Trie trie = new TrieImpl();
        trie.add("Very");
        trie.add("VeryVery");
        trie.add("VeryVeryLong");
        trie.add("VeryVeryLongWord");

        Assert.assertTrue(trie.contains("Very"));
        Assert.assertTrue(trie.contains("VeryVery"));
        Assert.assertTrue(trie.contains("VeryVeryLong"));
        Assert.assertTrue(trie.contains("VeryVeryLongWord"));

        Assert.assertFalse(trie.contains("AnyWord"));
        Assert.assertFalse(trie.contains("Word"));
        Assert.assertFalse(trie.contains("Ve"));
        Assert.assertFalse(trie.contains("VeryV"));
        Assert.assertFalse(trie.contains("VeryVeryLongWord1"));
        Assert.assertFalse(trie.contains("VeryVeryLongWor"));

        trie.remove("Very");
        Assert.assertFalse(trie.contains("Very"));
        Assert.assertTrue(trie.contains("VeryVery"));

        trie.remove("VeryVery");
        Assert.assertFalse(trie.contains("VeryVery"));
        Assert.assertTrue(trie.contains("VeryVeryLong"));

        trie.add("VeryVeryLongWord1");
        trie.remove("VeryVeryLongWord");
        Assert.assertTrue(trie.contains("VeryVeryLongWord1"));

        Assert.assertFalse(trie.contains(""));

        trie.add("");
        Assert.assertTrue(trie.contains(""));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        Trie trie = new TrieImpl();
        trie.add("String");
        trie.add("Word");
        Assert.assertFalse(trie.remove("YetWord"));
        Assert.assertTrue(trie.remove("Word"));

        trie.add("Word");
        trie.add("word");

        Assert.assertTrue(trie.remove("word"));

        Assert.assertFalse(trie.remove(""));

        trie.add("");
        Assert.assertTrue(trie.remove(""));
    }

    @org.junit.Test
    public void testSize() throws Exception {
        Trie trie = new TrieImpl();
        Assert.assertEquals(trie.size(), 0);
        trie.add("String");
        Assert.assertEquals(trie.size(), 1);
        trie.add("String2");
        Assert.assertEquals(trie.size(), 2);
        trie.add("Word");
        Assert.assertEquals(trie.size(), 3);

        trie.remove("str");
        Assert.assertEquals(trie.size(), 3);

        trie.remove("String");
        Assert.assertEquals(trie.size(), 2);

        trie.remove("String2");
        Assert.assertEquals(trie.size(), 1);

        trie.remove("Word");
        Assert.assertEquals(trie.size(), 0);

        trie.remove("Word");
        Assert.assertEquals(trie.size(), 0);

        trie.remove("Word");
        Assert.assertEquals(trie.size(), 0);

        trie.add("");
        Assert.assertEquals(trie.size(), 1);

        trie.add("");
        Assert.assertEquals(trie.size(), 1);

        trie.remove("");
        Assert.assertEquals(trie.size(), 0);
    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix() throws Exception {
        Trie trie = new TrieImpl();
        trie.add("Very");
        trie.add("VeryVery");
        trie.add("VeryVeryLong");
        trie.add("VeryVeryLongWord");

        Assert.assertEquals(trie.howManyStartsWithPrefix("V"), 4);
        Assert.assertEquals(trie.howManyStartsWithPrefix("Very"), 4);
        Assert.assertEquals(trie.howManyStartsWithPrefix("VE"), 0);
        Assert.assertEquals(trie.howManyStartsWithPrefix("VeryV"), 3);
        Assert.assertEquals(trie.howManyStartsWithPrefix("VeryVery"), 3);
        Assert.assertEquals(trie.howManyStartsWithPrefix("VeryVeryV"), 0);
        Assert.assertEquals(trie.howManyStartsWithPrefix("VeryVeryL"), 2);
        Assert.assertEquals(trie.howManyStartsWithPrefix("VeryVeryLongWord"), 1);
        Assert.assertEquals(trie.howManyStartsWithPrefix(""), 4);
    }
}
