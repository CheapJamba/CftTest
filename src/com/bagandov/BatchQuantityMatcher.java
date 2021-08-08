package com.bagandov;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatchQuantityMatcher {

    private Map<String, Integer> occurrences;

    private Map<QuantityMatcher, Report> quantityMatchers;

    public BatchQuantityMatcher() {
        occurrences = new HashMap<>();
        quantityMatchers = new HashMap<>();
    }

    public void addPattern(Report report) {
        String tag = report.getTag();
        quantityMatchers.put(new QuantityMatcher(tag), report);
        for (int i = 0; i < tag.length(); i += 2) {
            this.occurrences.put(tag.substring(i, i + 1), 0);
        }
    }

    public void addPatterns(List<Report> reports) {
        reports.forEach(report -> addPattern(report));
    }

    public void matches(String target) {
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("[");
        for (String key : occurrences.keySet()) {
            occurrences.put(key, 0);
            patternBuilder.append(key);
        }
        patternBuilder.append("]");
        Matcher matcher = Pattern.compile(patternBuilder.toString()).matcher(target);

        while (matcher.find()) {
            occurrences.put(matcher.group(), occurrences.get(matcher.group()) + 1);
        }

        for (QuantityMatcher qMatcher : quantityMatchers.keySet()) {
            if(qMatcher.matches(target, occurrences)) {
                quantityMatchers.get(qMatcher).increment();
            }
        }
    }
}
