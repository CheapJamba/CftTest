package com.bagandov;

public class Report {

    private String patternString;

    private String error;

    private int count;

    public Report(String patternString) {
        this.patternString = patternString;
        this.count = 0;
    }

    public int increment() {
        return ++count;
    }

    public String getPatternString() {
        return patternString;
    }

    public void setError(String errorMessage) {
        error = errorMessage;
    }


    @Override
    public String toString() {
        if (error != null) {
            return patternString + ": ОШИБКА - " + error;
        }
        return patternString + ": " + count;
    }
}
