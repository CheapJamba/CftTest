package com.bagandov;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuantityMatcher {

    private Map<String, Integer> requirements;

    public QuantityMatcher() {
        this.requirements = new HashMap<>();
    }

    public QuantityMatcher(Map<String, Integer> requirements) {
        this.requirements = requirements;
    }

    public QuantityMatcher(String patternString) {
        if (patternString.length() % 2 != 0) {
            throw new PatternFormatException("Шаблон, регламентирующий количество символов, содержит нечетное количество символов");
        }
        this.requirements = new HashMap<>();
        for (int i = 0; i < patternString.length(); i += 2) {
            this.requirements.put(patternString.substring(i, i + 1), Integer.parseInt(patternString.substring(i + 1, i + 2)));
        }
    }

    public boolean addRequirement(String symbol, Integer quantity) {
        if (requirements.containsKey(symbol)) {
            throw new IllegalStateException("Данный Matcher уже содержит требование для символа \"" + symbol + "\" с количеством " + quantity);
        }
        if (symbol.length() != 1) {
            throw new IllegalArgumentException("Matcher данного типа не поддерживает проверку для строк длиной больше 1");
        }

        requirements.put(symbol, quantity);
        return true;
    }

    public boolean matches(String target) {
        Map<String, Integer> present = new HashMap<>();
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("[");
        for (String key : requirements.keySet()) {
            present.put(key, 0);
            patternBuilder.append(key);
        }
        patternBuilder.append("]");
        Matcher matcher = Pattern.compile(patternBuilder.toString()).matcher(target);

        while (matcher.find()) {
            present.put(matcher.group(), present.get(matcher.group()) + 1);
        }

        for (String key : requirements.keySet()) {
            if (present.get(key) < requirements.get(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean matches(String target, Map<String, Integer> externalOccurrences) {
        for (String key : requirements.keySet()) {
            if (externalOccurrences.get(key) == null || externalOccurrences.get(key) < requirements.get(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        QuantityMatcher other = (QuantityMatcher) obj;
        return other.requirements == this.requirements || other.requirements.equals(this.requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirements);
    }


}
