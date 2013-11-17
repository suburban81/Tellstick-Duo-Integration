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
		map.put("135", "Ute tak");
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

	public Double getAbsoluteHumidity() {
		if (temp != null && humidity != null) {
			Double thisTemp = Double.valueOf(temp);
			Double relativeHumidity = Double.valueOf(humidity);

			return calcAbsoluteHumidity(thisTemp, relativeHumidity);
		}
		return null;
	}

	public Double calcAbsoluteHumidity(Double temp, Double relativeHumidity) {
		Double absoluteHumitidty = calcMaxAbsoluteHumidity(temp) * relativeHumidity / 100;

		return absoluteHumitidty;

	}

	public Double calcMaxAbsoluteHumidity(Double temp) {
		Double base = 1.098 + temp / 100.0;
		Double tmp = Math.pow(base, 8.02f);
		Double maxHumidity = 288.68 * tmp * 1 / (461.4 * (temp + 273.15));

		return maxHumidity * 1000;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nId        : " + id + "(" + model + ")");
		sb.append("\nLogTime   : " + logTime + "(LastUpdate: " + lastUpdate + ")");
		sb.append("\nTemp      : " + temp);
		sb.append("\nHumidity  : " + humidity);
		sb.append("\nAbsHum    : " + getAbsoluteHumidity());
		
		return sb.toString();
	}
}
