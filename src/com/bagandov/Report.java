package com.bagandov;

public class Report {

    private String tag;

    private String error;

    private int count;

    public Report(String tag) {
        this.tag = tag;
        this.count = 0;
    }

    public int increment() {
        return ++count;
    }

    public String getTag() {
        return tag;
    }

    public void setError(String errorMessage) {
        error = errorMessage;
    }


    @Override
    public String toString() {
        if (error != null) {
            return tag + ": ОШИБКА - " + error;
        }
        return tag + ": " + count;
    }
}
