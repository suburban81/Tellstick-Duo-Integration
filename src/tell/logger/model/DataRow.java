package tell.logger.model;

import java.util.ArrayList;
import java.util.List;

public class DataRow {

	private List<String> values = new ArrayList<String>();

	public DataRow(String... columns) {
		for (String column : columns) {
			values.add(column);
		}
	}

	public String commaSeparated() {
		StringBuilder line = new StringBuilder();
		for (String val : values) {
			line.append(val + ",");
		}
		return line.toString();
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

}
