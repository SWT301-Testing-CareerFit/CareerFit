package com.careerfit.automation.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility to read test data from CSV files in classpath (src/test/resources).
 * Ensures tests do not hard-code test parameters, adhering to strict testing guidelines.
 */
public class CsvDataReader {

    private CsvDataReader() {
        // Prevent instantiation of utility class
    }

    /**
     * Reads all rows from a CSV resource file into a list of key-value maps.
     *
     * @param resourcePath classpath relative path, e.g. "testdata/apply_data.csv"
     * @return List of Map<columnName, cellValue>
     */
    public static List<Map<String, String>> readCsv(String resourcePath) {
        List<Map<String, String>> rows = new ArrayList<>();
        String normalizedPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;

        InputStream is = CsvDataReader.class.getClassLoader().getResourceAsStream(normalizedPath);
        if (is == null) {
            System.err.println("[CsvDataReader] Resource not found: " + resourcePath);
            return rows;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return rows;
            }
            String[] headers = headerLine.split(",");
            for (int i = 0; i < headers.length; i++) {
                headers[i] = headers[i].trim();
            }

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] values = line.split(",", -1);
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    row.put(headers[i], values[i].trim());
                }
                rows.add(row);
            }
        } catch (Exception e) {
            System.err.println("[CsvDataReader] Error reading " + resourcePath + ": " + e.getMessage());
        }
        return rows;
    }

    /**
     * Finds the first row matching the specified tcId.
     *
     * @param resourcePath classpath relative path, e.g. "testdata/apply_data.csv"
     * @param tcId Test Case ID to match
     * @return Map of column values or null if not found
     */
    public static Map<String, String> getRowByTcId(String resourcePath, String tcId) {
        return readCsv(resourcePath).stream()
                .filter(r -> tcId.equalsIgnoreCase(r.get("tcId")))
                .findFirst()
                .orElse(null);
    }
}
