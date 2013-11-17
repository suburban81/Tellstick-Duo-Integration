package tell.logger.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.api.TellStickDuo;
import tell.logger.dao.JdbcSQLite;
import tell.logger.model.Sensor;

public class LogSensors {

	private static final Logger log = Logger.getLogger(LogSensors.class);

	private TellStickDuo duo;
	private JdbcSQLite db;

	public LogSensors(TellStickDuo duo, JdbcSQLite db) {
		super();
		this.duo = duo;
		this.db = db;
	}

	public void execute() {
		log.info("-- Started task LogSensors --");
		List<Sensor> sensors = duo.querySensors(getHourDate());

		for (Sensor sensor : sensors) {
			db.logSensor(sensor);
		}
		log.debug("-- Finished task LogSensors --");
	}

	private Date getHourDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}
}
