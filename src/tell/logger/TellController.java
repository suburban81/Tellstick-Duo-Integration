package tell.logger;

import java.io.IOException;
import java.util.List;

import tell.logger.api.TellStickDuo;
import tell.logger.dao.ArrayRowFileWriter;
import tell.logger.dao.JdbcSQLite;
import tell.logger.lights.time.LightsTimeDescision;
import tell.logger.tasks.LightsTimer;
import tell.logger.tasks.LogSensors;
import tell.logger.tasks.RoofFan;
import tell.logger.tasks.RoofFanDefault;
import tell.logger.tasks.RoofFanRestrictive;
import tell.logger.tasks.RoofFanTemperatureControl;

public class TellController {

	private TellStickDuo duo;
	private JdbcSQLite db;
	private ArrayRowFileWriter rowWriter = new ArrayRowFileWriter();

	public void createDbTables() {
		db.setupDb();
	}

	public void runSensorTask() {
		LogSensors logSensors = new LogSensors(getDuo(), getDB());
		logSensors.execute();
	}

	public void runRoofHumidity() throws IOException, InterruptedException {
		RoofFan roofFan = new RoofFanDefault(getDuo());
		roofFan.execute();
	}

	public void runRoofHumidityTempControl() throws IOException, InterruptedException {
		RoofFan roofFan = new RoofFanTemperatureControl(getDuo());
		roofFan.execute();
	}

	public void runRoofHumidityRestrictive() throws IOException, InterruptedException {
		RoofFan roofFan = new RoofFanRestrictive(getDuo());
		roofFan.execute();
	}

	public void runIndoorLightsTask(List<String> lights) {
		new LightsTimer().turnOnOffLights(lights, new LightsTimeDescision());
	}

	public void runOutdoorLightsTask(List<String> lights) {
		new LightsTimer().turnOnOffLights(lights, new LightsTimeDescision());
	}

	public void writeCSVDay(String filepath) {
		rowWriter.writeCSVFromArray(filepath, getDB().loadTempHumidityPerDay());
	}

	private TellStickDuo getDuo() {
		if (duo == null) {
			duo = new TellStickDuo();
		}
		return duo;
	}

	private JdbcSQLite getDB() {
		if (db == null) {
			try {
				db = new JdbcSQLite("tellstickv2");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return db;
	}
}
