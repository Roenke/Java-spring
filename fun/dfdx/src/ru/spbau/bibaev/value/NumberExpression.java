package ru.spbau.bibaev.value;

import ru.spbau.bibaev.Expression;

public class NumberExpression extends ValueExpression {
    public NumberExpression(String val) {
        super(val);
    }

    @Override
    public Expression derivative(String variable) {
        return new ZeroExpression();
    }
}
