package hms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CsvFileReader {
	private CsvFileReader() {
	}

	public static List<Map<String, String>> readRowsAsMaps(String filePath) {
		Objects.requireNonNull(filePath, "filePath");

		Path path = Path.of(filePath);
		if (!Files.exists(path)) {
			throw new IllegalArgumentException("CSV file not found: " + path.toAbsolutePath());
		}

		try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String headerLine = bufferedReader.readLine();
			if (headerLine == null) {
				return List.of();
			}

			String[] headerNames = splitByComma(headerLine);
			for (int headerIndex = 0; headerIndex < headerNames.length; headerIndex++) {
				headerNames[headerIndex] = normalizeCellValue(headerNames[headerIndex]);
			}

			List<Map<String, String>> rows = new ArrayList<>();
			String rowLine;

			while ((rowLine = bufferedReader.readLine()) != null) {
				if (rowLine.isBlank()) {
					continue;
				}

				String[] rowValues = splitByComma(rowLine);
				Map<String, String> row = new LinkedHashMap<>();

				for (int columnIndex = 0; columnIndex < headerNames.length; columnIndex++) {
					String columnName = headerNames[columnIndex];
					String rawValue = columnIndex < rowValues.length ? rowValues[columnIndex] : "";
					row.put(columnName, normalizeCellValue(rawValue));
				}

				rows.add(row);
			}

			return rows;
		} catch (IOException exception) {
			throw new IllegalStateException("Failed reading CSV file: " + path.toAbsolutePath(), exception);
		}
	}

	private static String[] splitByComma(String line) {
		return line.split(",", -1);
	}

	private static String normalizeCellValue(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}