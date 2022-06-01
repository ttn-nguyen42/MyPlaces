package com.example.places.utils.base;

public class Validation {
    private final boolean isCorrect;
    private final Throwable e;

    public static Validation validateEmptiness(String s) {
        if (!s.trim().isEmpty()) {
            return new Validation(true, null);
        }
        return new Validation(false, new Throwable("Empty input"));
    }

    public Throwable getError() {
        return e;
    }

    public String getReason() {
        return e.getMessage();
    }

    public boolean isValidated() {
        return this.isCorrect;
    }

    private Validation(boolean isCorrect, Throwable e) {
        this.isCorrect = isCorrect;
        this.e = e;
    }
}
