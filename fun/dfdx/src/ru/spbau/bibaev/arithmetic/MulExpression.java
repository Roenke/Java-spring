package ru.spbau.bibaev.arithmetic;

import ru.spbau.bibaev.Expression;
import ru.spbau.bibaev.value.ZeroExpression;

public class MulExpression extends ArithmeticExpression {
    public MulExpression(Expression left, Expression right) {
        super(left, right, " * ");
    }

    @Override
    public Expression derivative(String variable) {
        Expression left = leftExpression.derivative(variable);
        Expression right = rightExpression.derivative(variable);
        if(left.isZero() && right.isZero()) {
            return new ZeroExpression();
        }

        if(left.isZero()) {
            return new MulExpression(leftExpression, right);
        }

        if(right.isZero()) {
            return new MulExpression(left, rightExpression);
        }

        return new PlusExpression(new MulExpression(left, rightExpression), new MulExpression(leftExpression, right));
    }
}
