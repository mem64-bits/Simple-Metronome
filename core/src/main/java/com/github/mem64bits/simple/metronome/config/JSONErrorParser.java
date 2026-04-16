package com.github.mem64bits.simple.metronome.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JSONErrorParser{

    public static void report(String fileName, Exception e) {
        report(fileName, null, e);
    }

    public static void report(String fileName, Path filePath, Exception e) {
        System.err.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.err.println("  JSON Error in: " + fileName);
        System.err.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        if (e instanceof JsonParseException parseEx) {
            reportParseError(fileName, filePath, parseEx);

        } else if (e instanceof JsonMappingException mapEx) {
            reportMappingError(fileName, filePath, mapEx);
        } else {
            System.err.println("Error: " + e.getLocalizedMessage());
        }

        System.err.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }

    private static void reportParseError(String fileName, Path filePath, JsonParseException parseEx) {
        int line = parseEx.getLocation().getLineNr();
        int column = parseEx.getLocation().getColumnNr();

        System.err.println("SyntaxError: Invalid JSON syntax");
        System.err.printf("  at line %d, column %d%n%n", line, column);

        // Show the problematic line with context
        if (filePath != null) {
            showLineContext(filePath, line, column);
        }

        // Show a simplified error message
        String msg = parseEx.getOriginalMessage();
        if (msg != null) {
            System.err.println("Reason: " + msg);
        }
    }

    private static void reportMappingError(String fileName, Path filePath, JsonMappingException mapEx) {
        int line = mapEx.getLocation().getLineNr();
        int column = mapEx.getLocation().getColumnNr();

        System.err.println("TypeError: Invalid data type or missing required field");

        // Extract the field path
        String path = mapEx.getPath().stream()
            .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "[" + ref.getIndex() + "]")
            .reduce((a, b) -> a + " -> " + b)
            .orElse("(root)");

        System.err.printf("  at field: %s%n", path);
        System.err.printf("  at line %d, column %d%n%n", line, column);

        // Show the problematic line with context
        if (filePath != null) {
            showLineContext(filePath, line, column);
        }

        // Extract and show the most relevant part of the error message
        String msg = mapEx.getOriginalMessage();
        if (msg != null) {
            String[] parts = msg.split("\n");
            if (parts.length > 0) {
                System.err.println("Reason: " + parts[0]);
            }
        }
    }

    private static void showLineContext(Path filePath, int errorLine, int errorColumn) {
        try {
            List<String> lines = Files.readAllLines(filePath);

            // Calculate context range (show 2 lines before and after)
            int startLine = Math.max(0, errorLine - 3);
            int endLine = Math.min(lines.size(), errorLine + 2);

            // Calculate line number width for padding
            int lineNumWidth = String.valueOf(endLine).length();

            for (int i = startLine; i < endLine; i++) {
                int displayLineNum = i + 1;
                String lineContent = lines.get(i);

                // Mark the error line with ">" and highlight it
                if (displayLineNum == errorLine) {
                    System.err.printf("  %s %s | %s%n",
                        ">>>",
                        padLeft(String.valueOf(displayLineNum), lineNumWidth),
                        lineContent);

                    // Show a column indicator with "^"
                    if (errorColumn > 0) {
                        int padding = lineNumWidth + 8 + errorColumn - 1;
                        System.err.println("  " + " ".repeat(padding) + "^");
                    }
                } else {
                    System.err.printf("  %s %s | %s%n",
                        "   ",
                        padLeft(String.valueOf(displayLineNum), lineNumWidth),
                        lineContent);
                }
            }
            System.err.println();

        } catch (IOException e) {
            // If file can't be read, just skip showing context
        }
    }

    private static String padLeft(String str, int width) {
        return " ".repeat(Math.max(0, width - str.length())) + str;
    }
}
