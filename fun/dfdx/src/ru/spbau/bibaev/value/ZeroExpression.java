package ru.spbau.bibaev.value;

import ru.spbau.bibaev.Expression;

public class ZeroExpression extends ValueExpression {
    public ZeroExpression() {
        super("0");
    }

    @Override
    public boolean isZero() {
        return true;
    }

    @Override
    public Expression derivative(String variable) {
        return this;
    }
}
