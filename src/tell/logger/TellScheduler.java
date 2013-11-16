package tell.logger;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tell.logger.api.TellStickDuo;
import tell.logger.dao.JdbcSQLite;
import tell.logger.tasks.LogSensors;

public class TellScheduler {

	private final ScheduledExecutorService logSensorsScheduler = Executors.newScheduledThreadPool(1);
	private TellStickDuo duo = new TellStickDuo();
	private JdbcSQLite db;

	public TellScheduler() {
		try {
			db = new JdbcSQLite("tellstick");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		db.setupDb();
	}

	public void startSensorTask() {
		LogSensors logSensors = new LogSensors(duo, db);
		logSensorsScheduler.scheduleAtFixedRate(logSensors, getWaitUntilNextHour(), 1000 * 60 * 60, TimeUnit.MILLISECONDS);
	}

	public void stopSensorTask() {
		logSensorsScheduler.shutdown();
	}

	private long getWaitUntilNextHour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		System.out.println("Next hour is: " + cal.getTime().toString());
		return cal.getTimeInMillis() - System.currentTimeMillis();
	}
}
