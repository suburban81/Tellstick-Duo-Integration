package tell.logger;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.tasks.Tasks;

public class Main {

	private static final Logger log = Logger.getLogger(Main.class);

	/**
	 * args 0 - Task to run
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		TellController tellController = new TellController();

		try {
			if (args[0].equals(Tasks.LOG_SENSORS.name())) {
				tellController.runSensorTask();
			} else if (args[0].equals(Tasks.ROOF_HUMIDITY.name())) {
				tellController.runRoofHumidity();
			} else if (args[0].equals(Tasks.ROOF_HUMIDITY_TEMP_CONTROL.name())) {
				tellController.runRoofHumidityTempControl();
			} else if (args[0].equals(Tasks.ROOF_HUMIDITY_RESTRICTIVE.name())) {
				tellController.runRoofHumidityRestrictive();
			} else if (args[0].equals(Tasks.INDOOR_LIGHTS.name())) {
				tellController.runIndoorLightsTask(readLightsInput(args));
			} else if (args[0].equals(Tasks.OUTDOOR_LIGHTS.name())) {
				tellController.runOutdoorLightsTask(readLightsInput(args));
			} else if (args[0].equals(Tasks.WRITE_CSV_DAY.name())) {
				log.warn("About to fire up task: " + args[0] +  ", filepath: " + args[1]);
				tellController.writeCSVDay(args[1]);
			} else if (args[0].equals(Tasks.SETUP_DB.name())) {
				tellController.createDbTables();
			} else {
				log.warn("Did not find any match for: " + args[0]);
			}
		} catch (Exception e) {
			log.error("Fail due to unexpected error: " + e, e);
			throw e;
		}
	}

	private static List<String> readLightsInput(String[] args) {
		List<String> lights = new ArrayList<String>();
		for (int i = 1; i <= args.length - 1; i++) {
			lights.add(args[i]);
		}
		return lights;
	}

}
