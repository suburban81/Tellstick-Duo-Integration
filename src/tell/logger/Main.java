package tell.logger;

import tell.logger.tasks.Tasks;

public class Main {

	/**
	 * args 0 - Task to run
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TellController tellController = new TellController();

		if (args[0].equals(Tasks.LOG_SENSORS.name())) {
			tellController.runSensorTask();
		}

	}
}
