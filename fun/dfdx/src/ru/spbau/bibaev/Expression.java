package ru.spbau.bibaev;

public abstract class Expression {
    protected boolean isNegative;

    public void setPositive() {
        isNegative = false;
    }

    public void setNegative() {
        isNegative = true;
    }

    public void negate() {
        isNegative ^= true;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public boolean isZero() {
        return false;
    }
    public abstract Expression derivative(String variable);
}
