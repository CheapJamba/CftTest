package com.bagandov;

import java.io.*;
import java.util.*;

public class Main {

    static BufferedReader inStream = new BufferedReader(new InputStreamReader(System.in));

    public static Map<SubsequenceMatcher, Report> seqMatchers = new HashMap<>();
    public static BatchQuantityMatcher batchQMatcher = new BatchQuantityMatcher();

    public static List<Report> reports = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Введите имя файла с текстом для анализа");
        String file1 = inStream.readLine();
        System.out.println("Введите имя файла с шаблонами");
        String file2 = inStream.readLine();
        System.out.println("Введите имя файла для вывода результатов");
        String file3 = inStream.readLine();


        Scanner scanner = new Scanner(new FileInputStream(file1));
        scanner.useDelimiter("[^\\p{javaLetterOrDigit}]+");

        List<String> words = new ArrayList<>();
        while (scanner.hasNext()) {
            words.add(scanner.next());
        }
        readPatternsFromFile(file2);

        for (String word : words) {
            for (SubsequenceMatcher matcher : seqMatchers.keySet()) {
                if (matcher.matches(word)) {
                    seqMatchers.get(matcher).increment();
                }
            }
            batchQMatcher.matches(word);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file3))) {
            for (Report report : reports) {
                bw.write(report.toString());
                bw.newLine();
            }

        }
    }


    public static void readPatternsFromFile(String patternsSource) throws Exception {
        Scanner scanner = new Scanner(new FileInputStream(patternsSource));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            stringToPattern(line);
        }
    }

    public static void stringToPattern(String string) {
        if (string.charAt(0) == '\"') {
            Report report = new Report(string);
            try {
                seqMatchers.put(new SubsequenceMatcher(string), report);
            } catch (PatternFormatException ex) {
                report.setError(ex.getMessage());
            }
            reports.add(report);
        } else {
            Report report = new Report(string);
            try {
                batchQMatcher.addPattern(report);
            } catch (PatternFormatException ex) {
                report.setError(ex.getMessage());
            }
            reports.add(report);
        }
    }
}
