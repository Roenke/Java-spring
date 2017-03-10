package ru.spbau.bibaev;

import java.util.ArrayList;

enum State {
    START,
    NUMBER,
    ID,
    EXP_E, EXP_X, EXP_P,
    LN_L, LN_N,
    SIGN,
    TERM
}

enum SymbolType {
    SPACE,
    LETTER,
    DIGIT,
    SIGN,
}

public class Parser {
    private String expression;
    private int offset;

    public Parser(String expression) {
        offset = 0;
        this.expression = expression;
    }

    public Token getNextToken() {
        if (offset >= expression.length()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        State state = State.START;
        while (offset < expression.length() && state != State.TERM) {
            char ch = expression.charAt(offset);
            SymbolType symbolType = getCharType(ch);
            switch (state) {
                case START:
                    switch (symbolType) {
                        case DIGIT:
                            state = State.NUMBER;
                            builder.append(ch);
                            break;
                        case SIGN:
                            state = State.SIGN;
                            builder.append(ch);
                            break;
                        case LETTER:
                            if (ch == 'e') {
                                state = State.EXP_E;
                            }
                            else if (ch == 'l') {
                                state = State.LN_L;
                            }
                            else {
                                state = State.ID;
                            }
                            builder.append(ch);
                            break;
                        case SPACE:
                            break;
                    }
                    break;
                case EXP_E:
                    if (ch == 'x') {
                        state = State.EXP_X;
                    }
                    else {
                        state = State.ID;
                    }
                    builder.append(ch);
                    break;
                case EXP_X:
                    if (ch == 'p') {
                        state = State.EXP_P;
                    }
                    else {
                        state = State.ID;
                    }
                    builder.append(ch);
                    break;
                case EXP_P:
                    if (symbolType == SymbolType.SPACE || symbolType == SymbolType.SIGN)
            }
            offset++;
        }
    }

    public Expression getExpression() {
        Token token = getNextToken();
        ArrayList<Token> tokens = new ArrayList<>();
        while(token != null) {
            tokens.add(token);
            token = getNextToken();
        }

        return null;
    }

    private SymbolType getCharType(char ch) {
        if (Character.isSpaceChar(ch)) {
            return SymbolType.SPACE;
        }

        if (Character.isDigit(ch)) {
            return SymbolType.DIGIT;
        }

        if (Character.isLetter(ch)) {
            return SymbolType.LETTER;
        }

        return SymbolType.SIGN;
    }
}
