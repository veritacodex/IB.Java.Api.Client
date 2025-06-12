package com.ib.custom.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleHelper {
    public static void print(String message) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " - " + message);
    }
}
