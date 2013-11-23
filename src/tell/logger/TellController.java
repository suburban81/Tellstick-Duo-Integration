package tell.logger;

import java.io.IOException;

import tell.logger.api.TellStickDuo;
import tell.logger.dao.JdbcSQLite;
import tell.logger.tasks.LogSensors;
import tell.logger.tasks.RoofFan;

public class TellController {

	private TellStickDuo duo = new TellStickDuo();
	private JdbcSQLite db;

	public TellController() {
		try {
			db = new JdbcSQLite("tellstickv2");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void createDbTables() {
		db.setupDb();
	}

	public void runSensorTask() {
		LogSensors logSensors = new LogSensors(duo, db);
		logSensors.execute();
	}

	public void runRoofHumidity() throws IOException, InterruptedException {
		RoofFan roofFan = new RoofFan(duo);
		roofFan.execute();
	}
}
