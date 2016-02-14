package ru.spbau.bibaev;

import ru.spbau.bibaev.value.VariableExpression;

public class Parser {
    public static Expression Parse(String expression) {
        return new VariableExpression("x");
    }
}
