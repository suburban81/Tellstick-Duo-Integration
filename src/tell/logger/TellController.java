package tell.logger;

import tell.logger.api.TellStickDuo;
import tell.logger.dao.JdbcSQLite;
import tell.logger.tasks.LogSensors;

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
}
