package ru.spbau.bibaev.arithmetic;

import ru.spbau.bibaev.Expression;

public abstract class ArithmeticExpression extends Expression {
    private String arithmeticSign;

    protected Expression leftExpression;
    protected Expression rightExpression;

    public ArithmeticExpression(Expression left, Expression right, String sym) {
        leftExpression = left;
        rightExpression = right;
        arithmeticSign = sym;
    }

    @Override
    public String toString() {
        return "(" + leftExpression + arithmeticSign + rightExpression + ")";
    }
}
