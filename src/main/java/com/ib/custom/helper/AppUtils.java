package com.ib.custom.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AppUtils {
    public static String findFirstFutureDate(Collection<String> dates, long daysToAdd) {
        LocalDate today = LocalDate.now();
        LocalDate minFutureDate = today.plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return dates.stream()
                .map(date -> LocalDate.parse(date, formatter))
                .filter(date -> !date.isBefore(minFutureDate))
                .min(LocalDate::compareTo)
                .map(formatter::format)
                .orElse(null);
    }

    public static Double findStrike(Set<Double> strikes, double underlying, int range) {
        return strikes.stream()
                .filter(num -> num > underlying + range)
                .findFirst()
                .orElse(null);
    }

    public static Double findNext(Set<Double> strikes, double start, int steps) {
        return strikes.stream()
                .filter(strike -> strike > start)
                .sorted()
                .skip(steps - 1)
                .findFirst()
                .orElse(null);
    }

    public static Double findAtmStrike(Set<Double> numbers, double target) {
        return numbers.stream()
                .min(Comparator.comparingDouble(a -> Math.abs(a - target)))
                .orElse(null);
    }

    public static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.err.println("Error clearing console: " + e.getMessage());
        }
    }
}
