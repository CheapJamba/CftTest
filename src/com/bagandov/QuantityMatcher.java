package com.bagandov;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
        if (!patternString.matches("(\\p{javaLetterOrDigit}\\d+)+")) {
            throw new PatternFormatException("Неверный формат шаблона *символ**количество*");
        }
        this.requirements = new HashMap<>();

        Matcher matcher = Pattern.compile("\\p{javaLetterOrDigit}\\d+").matcher(patternString);
        while (matcher.find()) {
            this.requirements.put(matcher.group().substring(0, 1), Integer.parseInt(matcher.group().substring(1)));
        }
    }

    public boolean addRequirement(String symbol, Integer quantity) {
        if (symbol.length() != 1) {
            throw new IllegalArgumentException("Matcher данного типа не поддерживает проверку для строк длиной больше 1");
        }
        if (requirements.containsKey(symbol)) {
            throw new IllegalStateException("Данный Matcher уже содержит требование для символа \"" + symbol + "\" с количеством " + quantity);
        }

        requirements.put(symbol, quantity);
        return true;
    }

    public Set<String> getTargetSymbolsSet() {
        return requirements.keySet();
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
