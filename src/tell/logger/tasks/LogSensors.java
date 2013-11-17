package tell.logger.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tell.logger.api.TellStickDuo;
import tell.logger.dao.JdbcSQLite;
import tell.logger.model.Sensor;

public class LogSensors{

	private TellStickDuo duo;
	private JdbcSQLite db;

	public LogSensors(TellStickDuo duo, JdbcSQLite db) {
		super();
		this.duo = duo;
		this.db = db;
	}

	public void execute() {
		System.out.println("\n-- Started task LogSensors --");
		List<Sensor> sensors = duo.querySensors(getHourDate());

		for (Sensor sensor : sensors) {
			db.logSensor(sensor);
		}
		System.out.println("-- Finished task LogSensors --");
	}

	private Date getHourDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}
}
