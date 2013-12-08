package tell.logger.tasks;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.api.TellStickDuo;
import tell.logger.exec.SystemCommandExecutor;
import tell.logger.model.Sensor;

public abstract class RoofFan {

	private static final String PREFIX = "FAN-    ";

	protected static final Logger log = Logger.getLogger(RoofFan.class);

	private TellStickDuo duo;

	public RoofFan(TellStickDuo duo) {
		this.duo = duo;
	}

	public void execute() throws IOException, InterruptedException {
		List<Sensor> sensors = duo.querySensors(new Date());

		Sensor roof = null;
		Sensor outside = null;

		for (Sensor sensor : sensors) {
			if ("Vind".equals(sensor.getDisplayName())) {
				roof = sensor;
			} else if ("Ute tak".equals(sensor.getDisplayName())) {
				outside = sensor;
			}
		}

		prettyPrint(roof, outside);

		if (!hasBasicRequirements(roof, outside)) {
			return;
		}

		SystemCommandExecutor exec = null;
		int result = -9999;

		List<String> commands = new LinkedList<String>();
		commands.add("tdtool");
		if (betterOutside(roof, outside)) {
			commands.add("--on");
			commands.add("5");
			exec = new SystemCommandExecutor(commands);
			result = exec.executeCommand();
			if (!"Turning on device 5, Vind flakt - Success\n".equals(exec.getStandardOutputFromCommand().toString())) {
				log.error(PREFIX + "Unexpected answer from exec roof fan! " + exec.getStandardOutputFromCommand().toString());
			}
		} else {
			commands.add("--off");
			commands.add("5");
			exec = new SystemCommandExecutor(commands);
			result = exec.executeCommand();

			if (!"Turning off device 5, Vind flakt - Success\n".equals(exec.getStandardOutputFromCommand().toString())) {
				log.error(PREFIX + "Unexpected answer from exec roof fan! " + exec.getStandardOutputFromCommand().toString());
			}
		}

		if (result != 0) {
			log.warn("Unexpected result from exec: " + result);
		}

		if (!"".equals(exec.getStandardErrorFromCommand().toString())) {
			log.error(PREFIX + "Unexpected answer from exec roof fan! " + exec.getStandardErrorFromCommand().toString());
		}
	}

	private void prettyPrint(Sensor roof, Sensor outside) {
		StringBuilder sb = new StringBuilder(PREFIX);
		sb.append(String.format("Inside  abs:%1$,.5f temp:%2$,.1fC rel:%3$,.0f%%", roof.getAbsoluteHumidity(), roof.getTempDouble(), roof.getHumidityDouble()));
		log.info(sb.toString());
		sb = new StringBuilder(PREFIX);
		sb.append(String.format("Outside abs:%1$,.5f temp:%2$,.1fC rel:%3$,.0f%%", outside.getAbsoluteHumidity(), outside.getTempDouble(), outside.getHumidityDouble()));
		log.info(sb.toString());
	}

	private boolean hasBasicRequirements(Sensor roof, Sensor outside) {
		if (roof.updatedLastMinutes(40)) {
			log.error(PREFIX + "Roof sensor has not been updated, could not run fan");
			return false;
		}
		if (outside.updatedLastMinutes(10)) {
			log.error(PREFIX + "Outside sensor has not been updated, could not run fan");
			return false;
		}
		if (roof.getTempDouble() < -0.5) {
			log.info(PREFIX + "To cold on roof to run fan");
			return false;
		}
		if (outside.getTempDouble() < 0) {
			log.info(PREFIX + "To cold outside to run fan");
			return false;
		}

		return true;
	}

	protected abstract boolean betterOutside(Sensor roof, Sensor outside);

}
