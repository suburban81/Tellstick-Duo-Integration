package tell.logger.tasks;

import java.util.Date;
import java.util.List;

import tell.logger.api.TellStickDuo;
import tell.logger.dao.JdbcSQLite;
import tell.logger.model.Sensor;

public class LogSensors extends Thread {

	private TellStickDuo duo;
	private JdbcSQLite db;

	public LogSensors(TellStickDuo duo, JdbcSQLite db) {
		super();
		this.duo = duo;
		this.db = db;
	}

	@Override
	public void run() {
		System.out.println("\n-- Started task LogSensors --");
		List<Sensor> sensors = duo.querySensors(new Date());

		for (Sensor sensor : sensors) {
			db.logSensor(sensor);
		}
		System.out.println("-- Finished task LogSensors --");
	}
}
