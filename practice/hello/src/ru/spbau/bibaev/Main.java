package ru.spbau.bibaev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        palindromeTask();
        fizzBuzzTask();
    }

    private static void palindromeTask() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line;
        while((line = reader.readLine()) != null){
            if (new StringBuilder(line).reverse().toString().equals(line)) {
                System.out.println(line + " is palindrome.");
            }
            else {
                System.out.println(line + " is not palindrome.");
            }
        }
    }

    private static void fizzBuzzTask() {
        for (int i = 1; i <= 100; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println("FizzBuzz");
            }
            else if(i % 3 == 0) {
                System.out.println("Fizz");
            }
            else {
                System.out.println(i % 5 == 0 ? "Buzz" : i);
            }
        }
    }
}
