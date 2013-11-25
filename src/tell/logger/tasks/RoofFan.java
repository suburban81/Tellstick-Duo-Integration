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

		if (roof.getTempDouble() < -0.5) {
			log.info("To cold on roof to run fan");
			return;
		}

		if (outside.getTempDouble() < 0) {
			log.info("To cold outside to run fan");
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
				log.error("Unexpected answer from exec roof fan! " + exec.getStandardOutputFromCommand().toString());
			}
		} else {
			commands.add("--off");
			commands.add("5");
			exec = new SystemCommandExecutor(commands);
			result = exec.executeCommand();

			if (!"Turning off device 5, Vind flakt - Success\n".equals(exec.getStandardOutputFromCommand().toString())) {
				log.error("Unexpected answer from exec roof fan! " + exec.getStandardOutputFromCommand().toString());
			}
		}

		log.debug("Result from exec: " + result);
		if (!"".equals(exec.getStandardErrorFromCommand().toString())) {
			log.error("Unexpected answer from exec roof fan! " + exec.getStandardErrorFromCommand().toString());
		}
	}

	protected abstract boolean betterOutside(Sensor roof, Sensor outside);

}
