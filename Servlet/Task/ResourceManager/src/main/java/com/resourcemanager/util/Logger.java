package com.resourcemanager.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "/tmp/resourcemanager.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public synchronized static void info(String message) {
        log("INFO", message);
    }

    public synchronized static void error(String message) {
        log("ERROR", message);
    }

    public synchronized static void error(String message, Exception e) {
        log("ERROR", message + " -> " + e.getMessage());
    }

    public synchronized static void debug(String message) {
        log("DEBUG", message);
    }

    private synchronized static void log(String level, String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            String logMessage = String.format("[%s] %s - %s%n", timestamp, level, message);
            fw.write(logMessage);
            System.out.println(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
