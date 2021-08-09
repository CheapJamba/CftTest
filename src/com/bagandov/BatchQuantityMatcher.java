package com.bagandov;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatchQuantityMatcher {

    private Map<String, Integer> occurrences;

    private Map<QuantityMatcher, Report> quantityMatchers;

    StringBuilder patternBuilder = new StringBuilder();

    public BatchQuantityMatcher() {
        occurrences = new HashMap<>();
        quantityMatchers = new HashMap<>();
        patternBuilder.append("[]");
    }

    public void addPattern(Report report) {
        String patternString = report.getPatternString();
        QuantityMatcher newQMatcher = new QuantityMatcher(patternString);
        quantityMatchers.put(newQMatcher, report);

        for(String symbol : newQMatcher.getTargetSymbolsSet()) {
            this.occurrences.put(symbol, 0);
            patternBuilder.insert(1, symbol);
        }
    }

    public void addPatterns(List<Report> reports) {
        reports.forEach(report -> addPattern(report));
    }

    public void matches(String target) {
        Matcher matcher = Pattern.compile(patternBuilder.toString()).matcher(target);

        while (matcher.find()) {
            occurrences.put(matcher.group(), occurrences.get(matcher.group()) + 1);
        }

        for (QuantityMatcher qMatcher : quantityMatchers.keySet()) {
            if(qMatcher.matches(target, occurrences)) {
                quantityMatchers.get(qMatcher).increment();
            }
        }

        for(String key : occurrences.keySet()) {
            occurrences.put(key, 0);
        }
    }
}
