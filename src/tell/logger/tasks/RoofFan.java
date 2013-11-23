package tell.logger.tasks;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.api.TellStickDuo;
import tell.logger.exec.SystemCommandExecutor;
import tell.logger.model.Sensor;

public class RoofFan {

	private static final Logger log = Logger.getLogger(RoofFan.class);

	private TellStickDuo duo;

	public RoofFan(TellStickDuo duo) {
		this.duo = duo;
	}

	public void execute() {
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

		SystemCommandExecutor exec = null;

		List<String> commands = new LinkedList<String>();
		commands.add("tdtool");
		if (betterOutside(roof, outside)) {
			commands.add("--on");
			commands.add("5");
			exec = new SystemCommandExecutor(commands);
			if (!"Turning on device 5, Vind flakt - Success".equals(exec.getStandardOutputFromCommand().toString())) {
				log.error("Unexpected answer from exec roof fan! " + exec.getStandardOutputFromCommand().toString());
			}
		} else {
			commands.add("--off");
			commands.add("5");
			exec = new SystemCommandExecutor(commands);
			if (!"Turning off device 5, Vind flakt - Success".equals(exec.getStandardOutputFromCommand().toString())) {
				log.error("Unexpected answer from exec roof fan! " + exec.getStandardOutputFromCommand().toString());
			}
		}

		if (!"".equals(exec.getStandardErrorFromCommand().toString())) {
			log.error("Unexpected answer from exec roof fan! " + exec.getStandardErrorFromCommand().toString());
		}
	}

	private boolean betterOutside(Sensor roof, Sensor outside) {
		String errorMsg = "";
		if (roof.updatedLastMinutes(40)) {
			errorMsg.concat(" Roof sensor where not updated.");
		}
		if (outside.updatedLastMinutes(10)) {
			errorMsg.concat(" Outside sensor where not updated.");
		}
		if (roof.getAbsoluteHumidity() > outside.getAbsoluteHumidity()) {
			if (errorMsg.equals("")) {
				log.debug("Better outside, start fan");
				return true;
			} else {
				log.error("Better outside, would started fan: " + errorMsg);
				return false;
			}
		} else {
			if (errorMsg.equals("")) {
				log.debug("Better inside, stop fan");
			} else {
				log.error("Better inside, will stop fan but: " + errorMsg);
			}

			return false;
		}
	}
}
