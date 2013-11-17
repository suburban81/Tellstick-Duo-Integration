package tell.logger.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;

import tell.logger.model.Sensor;

public class JdbcSQLite {

	String jdbcConnection;
	DecimalFormat df = new DecimalFormat("#.00");

	public JdbcSQLite(String dbName) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		jdbcConnection = "jdbc:sqlite:" + dbName + ".db";
	}

	public void setupDb() {
		update("create table sensor (logTime String, lastUpdate String, id String, model String, displayname String, temp String, humidity String, absHumidity String)");
	}

	public void logSensor(Sensor sensor) {
		update("insert into sensor values('" + sensor.getLogTime() + "','" + sensor.getLastUpdate() + "','" + sensor.getId() + "','" + sensor.getModel() + "','" + sensor.getDisplayName() + "','"
				+ sensor.getTemp() + "','" + sensor.getHumidity() + "','" + df.format(sensor.getAbsoluteHumidity()) + "')");
	}

	private void update(String sql) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcConnection);
			connection.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}
}
