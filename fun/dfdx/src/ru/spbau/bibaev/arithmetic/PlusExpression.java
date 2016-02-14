package ru.spbau.bibaev.arithmetic;

import ru.spbau.bibaev.Expression;

public class PlusExpression extends ArithmeticExpression {
    public PlusExpression(Expression left, Expression right) {
        super(left, right, " + ");
    }

    @Override
    public Expression derivative(String variable) {
        Expression left = leftExpression.derivative(variable);
        Expression right = rightExpression.derivative(variable);
        if(left.isZero() || right.isZero()) {
            if(left.isZero()) {
                return right;
            }
            else {
                return left;
            }
        }

        return new PlusExpression(left, right);
    }
}
