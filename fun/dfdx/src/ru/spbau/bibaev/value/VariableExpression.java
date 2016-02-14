package ru.spbau.bibaev.value;

import ru.spbau.bibaev.Expression;

public class VariableExpression extends ValueExpression {
    public VariableExpression(String val) {
        super(val);
    }

    @Override
    public Expression derivative(String variable) {
        if (variable.equals(value)) {
            return new NumberExpression("1");
        }

        return new ZeroExpression();
    }
}
