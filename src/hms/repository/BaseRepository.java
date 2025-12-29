package hms.repository;

import java.util.Map;

public class BaseRepository {
	protected String value(Map<String, String> row, String key) {
		if (row == null || key == null) {
			return "";
		}
		String v = row.get(key);
		return v == null ? "" : v.trim();
	}
}
