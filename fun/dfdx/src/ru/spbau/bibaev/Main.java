package ru.spbau.bibaev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String expression = null;
        String variable = null;
        try {
            expression = reader.readLine();
            variable = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Parser parser = new Parser(expression);

        Expression basicExpression = parser.getExpression();
        Expression derivative = basicExpression.derivative(variable);
        System.out.println(derivative.toString());
    }
}
