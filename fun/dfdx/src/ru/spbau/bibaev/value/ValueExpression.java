package ru.spbau.bibaev.value;

import ru.spbau.bibaev.Expression;

public abstract class ValueExpression extends Expression {
    protected String value;

    @Override
    public String toString() {
        return value;
    }

    public ValueExpression(String val) {
        value = val;
    }
}
