package ru.spbau.bibaev.math;

import ru.spbau.bibaev.*;
import ru.spbau.bibaev.arithmetic.DivExpression;
import ru.spbau.bibaev.arithmetic.MulExpression;
import ru.spbau.bibaev.value.NumberExpression;
import ru.spbau.bibaev.value.ZeroExpression;

public class LogExpression extends MathExpression {
    public LogExpression(Expression inner) {
        super(inner, "ln");
    }

    @Override
    public Expression derivative(String variable) {
        Expression inner = innerExpression.derivative(variable);

        if (inner.isZero()) {
            return new ZeroExpression();
        }

        return new MulExpression(new DivExpression(new NumberExpression("1"), innerExpression), inner);
    }
}
