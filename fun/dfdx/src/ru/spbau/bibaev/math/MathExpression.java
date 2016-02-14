package ru.spbau.bibaev.math;

import ru.spbau.bibaev.Expression;

public abstract class MathExpression extends Expression {
    private String symbols;
    protected Expression innerExpression;

    public MathExpression(Expression inner, String sym) {
        innerExpression = inner;
        symbols = sym;
    }

    @Override
    public String toString() {
        return symbols + "(" + innerExpression + ")";
    }
}
