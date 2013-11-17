package tell.logger;

import org.apache.log4j.Logger;

import tell.logger.tasks.Tasks;

public class Main {

	private static final Logger log = Logger.getLogger(Main.class);

	/**
	 * args 0 - Task to run
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TellController tellController = new TellController();

		if (args[0].equals(Tasks.LOG_SENSORS.name())) {
			tellController.runSensorTask();
		} else if (args[0].equals(Tasks.SETUP_DB.name())) {
			tellController.createDbTables();
		} else {
			log.warn("Did not find any match for: " + args[0]);
		}
	}
}
