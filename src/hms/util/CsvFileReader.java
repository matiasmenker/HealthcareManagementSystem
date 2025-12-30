package hms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvFileReader {

  public static List<Map<String, String>> readRowsAsMaps(String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      throw new IllegalArgumentException("filePath is required");
    }

    List<List<String>> rows = readAllRows(filePath);
    if (rows.isEmpty()) {
      return List.of();
    }

    List<String> headers = normalizeHeaders(rows.get(0));
    List<Map<String, String>> result = new ArrayList<>();

    for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
      List<String> row = rows.get(rowIndex);
      Map<String, String> rowMap = mapRow(headers, row, rowIndex + 1, filePath);
      result.add(rowMap);
    }

    return result;
  }

  private static List<List<String>> readAllRows(String filePath) {
    Path path = Path.of(filePath);
    if (!Files.exists(path)) {
      throw new IllegalArgumentException("CSV file not found: " + filePath);
    }

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      List<List<String>> rows = new ArrayList<>();

      List<String> currentRow = new ArrayList<>();
      StringBuilder currentField = new StringBuilder();

      boolean insideQuotes = false;

      int characterInt;
      while ((characterInt = reader.read()) != -1) {
        char character = (char) characterInt;

        if (character == '"') {
          if (insideQuotes) {
            reader.mark(1);
            int nextInt = reader.read();
            if (nextInt == '"') {
              currentField.append('"');
            } else {
              insideQuotes = false;
              if (nextInt != -1) {
                reader.reset();
              }
            }
          } else {
            insideQuotes = true;
          }
          continue;
        }

        if (character == ',' && !insideQuotes) {
          currentRow.add(trimToEmpty(currentField.toString()));
          currentField.setLength(0);
          continue;
        }

        if ((character == '\n' || character == '\r') && !insideQuotes) {
          currentRow.add(trimToEmpty(currentField.toString()));
          currentField.setLength(0);

          boolean isEmptyRow = currentRow.size() == 1 && currentRow.get(0).isEmpty();
          if (!isEmptyRow) {
            rows.add(currentRow);
          }

          currentRow = new ArrayList<>();

          if (character == '\r') {
            reader.mark(1);
            int nextInt = reader.read();
            if (nextInt != '\n' && nextInt != -1) {
              reader.reset();
            }
          }
          continue;
        }

        currentField.append(character);
      }

      boolean hasPendingData = currentField.length() > 0 || !currentRow.isEmpty();
      if (hasPendingData) {
        currentRow.add(trimToEmpty(currentField.toString()));
        boolean isEmptyRow = currentRow.size() == 1 && currentRow.get(0).isEmpty();
        if (!isEmptyRow) {
          rows.add(currentRow);
        }
      }

      return rows;
    } catch (IOException exception) {
      throw new RuntimeException("Failed reading CSV file: " + filePath + " (" + exception.getMessage() + ")");
    }
  }

  private static List<String> normalizeHeaders(List<String> rawHeaders) {
    List<String> headers = new ArrayList<>();
    for (String header : rawHeaders) {
      headers.add(trimToEmpty(header));
    }
    return headers;
  }

  private static Map<String, String> mapRow(List<String> headers, List<String> row, int lineNumber, String filePath) {
    int expected = headers.size();
    int actual = row.size();

    if (actual > expected) {
      throw new IllegalArgumentException(
          "CSV row has more columns than header. File: " + filePath + ", line: " + lineNumber + ", expected: " + expected + ", actual: " + actual
      );
    }

    List<String> padded = new ArrayList<>(row);
    while (padded.size() < expected) {
      padded.add("");
    }

    Map<String, String> mapped = new LinkedHashMap<>();
    for (int index = 0; index < expected; index++) {
      mapped.put(headers.get(index), trimToEmpty(padded.get(index)));
    }

    return mapped;
  }

  private static String trimToEmpty(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}