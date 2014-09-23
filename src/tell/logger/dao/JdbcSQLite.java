package tell.logger.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.model.DataRow;
import tell.logger.model.Sensor;

public class JdbcSQLite {

	private static final Logger log = Logger.getLogger(JdbcSQLite.class);

	private String jdbcConnection;
	private DecimalFormat df = new DecimalFormat("#.00");

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

	public List<DataRow> loadTempHumidityPerDay() {
		Connection connection = null;
		ResultSet result = null;

		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("vind.logTime as time, ");
		sql.append("vind.temp, vind.humidity, vind.absHumidity, ");
		sql.append("ute.temp, ute.humidity, ute.absHumidity, ");
		sql.append("kallare.temp, kallare.humidity, kallare.absHumidity, ");
		sql.append("entre.temp, entre.humidity, entre.absHumidity ");
		sql.append("from ");
		sql.append("(select substr(logTime,0,11) as logTime, round(avg(temp),1) as temp, round(avg(humidity),1) as humidity, round(avg(absHumidity),1) as absHumidity from sensor where id = '104' group by logtime) vind, ");
		sql.append("(select substr(logTime,0,11) as logTime, round(avg(temp),1) as temp, round(avg(humidity),1) as humidity, round(avg(absHumidity),1) as absHumidity from sensor where id = '135' group by logtime) ute, ");
		sql.append("(select substr(logTime,0,11) as logTime, round(avg(temp),1) as temp, round(avg(humidity),1) as humidity, round(avg(absHumidity),1) as absHumidity from sensor where id = '11' group by logtime) kallare, ");
		sql.append("(select substr(logTime,0,11) as logTime, round(avg(temp),1) as temp, round(avg(humidity),1) as humidity, round(avg(absHumidity),1) as absHumidity from sensor where id = '12' group by logtime) entre ");
		sql.append("where ");
		sql.append("vind.logTime =+ ute.logTime ");
		sql.append("AND vind.logTime =+ kallare.logTime ");
		sql.append("AND vind.logTime =+ entre.logTime ");
		sql.append("group by ");
		sql.append("vind.logTime ");
		sql.append("order by ");
		sql.append("vind.logTime asc ");

		List<DataRow> rows = new ArrayList<DataRow>();
		rows.add(new DataRow("Datum", "Vind temp", "Vind fukt", "Vind absolut fukt", "Ute temp", "Ute fukt", "Ute absolut fukt", "Kallare temp", "Kallare fukt", "Kallare absolut fukt", "Entre temp",
				"Entre fukt", "Entre absolut fukt"));

		try {
			connection = DriverManager.getConnection(jdbcConnection);
			result = connection.createStatement().executeQuery(sql.toString());
			while (result.next()) {
				rows.add(new DataRow(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result
						.getString(8), result.getString(9), result.getString(10), result.getString(12), result.getString(13)));
			}
		} catch (SQLException e) {
			log.error(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}

		return rows;
	}

	private void update(String sql) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcConnection);
			connection.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			log.error(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}

}
