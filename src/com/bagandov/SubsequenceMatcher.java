package com.bagandov;

import java.util.Objects;

public class SubsequenceMatcher {

    private String pattern;

    public SubsequenceMatcher(String patternString) {
        if (patternString.charAt(patternString.length() - 1) != '\"') {
            throw new PatternFormatException("Нет закрывающей кавычки");
        }
        this.pattern = patternString.substring(1, patternString.length() - 1);
    }

    public boolean matches(String target) {
        return target.matches(".*" + pattern + ".*");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        SubsequenceMatcher other = (SubsequenceMatcher) obj;
        return other.pattern == this.pattern || other.pattern.equals(this.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern);
    }
}
