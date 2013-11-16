package tell.logger.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Sensor {

	public final static Map<String, String> DISPLAYNAMES;
	static {
		Map<String, String> map = new HashMap<String, String>();
		map.put("11", "Källare");
		map.put("12", "Entreplan");
		map.put("104", "Vind");
		DISPLAYNAMES = Collections.unmodifiableMap(map);
	}

	private String id;
	private String model;
	private String temp;
	private String humidity;
	private String logTime;
	private String lastUpdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		if (DISPLAYNAMES.containsKey(id)) {
			return DISPLAYNAMES.get(id);
		}
		return "unknown";
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Model     : " + model);
		sb.append("\nId        : " + id);
		sb.append("\nLogTime   : " + logTime);
		sb.append("\nLastUpdate: " + lastUpdate);
		if (humidity != null) {
			sb.append("\nHumidity: " + humidity);
		}
		if (temp != null) {
			sb.append("\nTemp    : " + temp + "\n");
		}
		return sb.toString();
	}

}
