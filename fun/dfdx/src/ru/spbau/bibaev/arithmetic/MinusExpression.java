package ru.spbau.bibaev.arithmetic;

import ru.spbau.bibaev.Expression;
import ru.spbau.bibaev.value.ZeroExpression;

public class MinusExpression extends ArithmeticExpression {
    public MinusExpression(Expression left, Expression right) {
        super(left, right, " - ");
    }

    @Override
    public Expression derivative(String variable) {
        Expression left = leftExpression.derivative(variable);
        Expression right = rightExpression.derivative(variable);

        if (left.isZero() && right.isZero()){
            return new ZeroExpression();
        }

        if (left.isZero()){
            right.negate();
            return right;
        }

        if (right.isZero()){
            return left;
        }

        return new MinusExpression(left, right);
    }
}
