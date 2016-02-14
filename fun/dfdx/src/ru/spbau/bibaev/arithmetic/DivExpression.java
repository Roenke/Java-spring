package ru.spbau.bibaev.arithmetic;

import ru.spbau.bibaev.Expression;
import ru.spbau.bibaev.value.ZeroExpression;

public class DivExpression extends ArithmeticExpression {
    public DivExpression(Expression left, Expression right) {
        super(left, right, " / ");
    }

    @Override
    public Expression derivative(String variable) {
        Expression left = leftExpression.derivative(variable);
        Expression right = rightExpression.derivative(variable);

        if (left.isZero() && right.isZero()) {
            return new ZeroExpression();
        }

        Expression rightSqr = new MulExpression(leftExpression, leftExpression);

        if (left.isZero()) {
            Expression result = new DivExpression(new MulExpression(leftExpression, right), rightSqr);
            result.negate();
            return result;
        }

        if (right.isZero()) {
            return new DivExpression(new MulExpression(left, rightExpression), rightSqr);
        }

        Expression up = new MinusExpression(new MulExpression(left, rightExpression),
                new MulExpression(leftExpression, right));
        return new DivExpression(up, rightSqr);
    }
}
