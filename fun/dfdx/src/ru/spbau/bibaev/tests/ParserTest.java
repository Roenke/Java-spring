package ru.spbau.bibaev.tests;

import org.junit.Assert;
import ru.spbau.bibaev.Parser;

import static org.junit.Assert.*;

public class ParserTest {

    @org.junit.Test
    public void testGetNextToken() throws Exception {
        Parser parser = new Parser("x + y");
        Assert.assertEquals(parser.getNextToken().getValue(), "x");
        Assert.assertEquals(parser.getNextToken().getValue(), "+");
        Assert.assertEquals(parser.getNextToken().getValue(), "y");
    }

    @org.junit.Test
    public void testGetExpression() throws Exception {

    }
}