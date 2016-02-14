package ru.spbau.bibaev.math;

import ru.spbau.bibaev.Expression;
import ru.spbau.bibaev.arithmetic.MulExpression;
import ru.spbau.bibaev.value.ZeroExpression;

public class ExpExpression extends MathExpression {
    public ExpExpression(Expression inner) {
        super(inner, "exp");
    }

    @Override
    public Expression derivative(String variable) {
        Expression inner = innerExpression.derivative(variable);
        if (inner.isZero()) {
            return new ZeroExpression();
        }

        return new MulExpression(new ExpExpression(inner), inner);
    }

    @Override
    public String toString() {
        return "exp(" + innerExpression + ")";
    }
}
