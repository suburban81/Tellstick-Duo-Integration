package tell.logger.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.model.Sensor;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public class TellStickDuo {

	private static final Logger log = Logger.getLogger(TellStickDuo.class);

	// Windows
	// private TellLibrary lib = (TellLibrary) Native.loadLibrary("TelldusCore",
	// TellLibrary.class);

	// Unix
	private TellLibrary lib = (TellLibrary) Native.loadLibrary("libtelldus-core.so.2", TellLibrary.class);
	
	public static final String SENSOR_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat dateFormat = new SimpleDateFormat(SENSOR_DATE_FORMAT);

	public List<Sensor> querySensors(Date logTime) {
		log.debug("Start query sensors");

		List<Sensor> sensors = new ArrayList<Sensor>();
		lib.tdInit();

		IntByReference id = new IntByReference();
		IntByReference dataTypes = new IntByReference();

		byte protocol[] = new byte[20];
		byte model[] = new byte[20];

		// check every sensor
		while (lib.tdSensor(protocol, 20, model, 20, id, dataTypes) == 0) {
			Sensor sensor = new Sensor();
			sensor.setId(String.valueOf(id.getValue()));
			sensor.setModel(Native.toString(protocol) + " " + Native.toString(model));
			sensor.setLogTime(dateFormat.format(logTime));

			Date lastUpdate = null;

			byte value[] = new byte[20];
			IntByReference timestamp = new IntByReference();
			if ((dataTypes.getValue() & TellLibrary.TELLSTICK_TEMPERATURE) != 0) {
				lib.tdSensorValue(protocol, model, id.getValue(), 1, value, 20, timestamp);
				lastUpdate = new Date(timestamp.getValue() * 1000l);
				sensor.setTemp(Native.toString(value));
			}
			if ((dataTypes.getValue() & TellLibrary.TELLSTICK_HUMIDITY) != 0) {
				lib.tdSensorValue(protocol, model, id.getValue(), 2, value, 20, timestamp);
				lastUpdate = new Date(timestamp.getValue() * 1000l);
				sensor.setHumidity(Native.toString(value));
			}

			sensor.setLastUpdate(dateFormat.format(lastUpdate));
			log.info(sensor.toString());
			sensors.add(sensor);
		}

		lib.tdClose();
		return sensors;
	}
}
