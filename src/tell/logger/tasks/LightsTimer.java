package tell.logger.tasks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import tell.logger.dao.ReadTimestampFile;
import tell.logger.exec.SystemCommandExecutor;
import tell.logger.lights.time.LightsDecision;
import tell.logger.lights.time.LightsDecision.Decision;

public class LightsTimer {

	private static final Logger log = Logger.getLogger(LightsTimer.class);

	private ReadTimestampFile readTimestampFile = new ReadTimestampFile();

	public void turnOnOffLights(List<String> lights, LightsDecision... lightsDecisions) {
		Decision decision;
		for (LightsDecision lightsDecision : lightsDecisions) {
			decision = lightsDecision.decide(new DateTime());
			if (decision == Decision.ON) {
				turn("--on", lights);
				return;
			} else if (decision == Decision.OFF) {
				turn("--off", lights);
				return;
			}
		}
	}

	private void turn(String command, List<String> lights) {
		List<String> commands = new LinkedList<String>();

		for (String light : lights) {
			if (!hasManualOverride(light)) {
				commands.clear();
				commands.add("tdtool");
				commands.add(command);
				commands.add(light);
				execute(commands);
			} else {
				log.info("Light " + light + " is manually set, leave it");
			}
		}
	}

	private boolean hasManualOverride(String light) {
		long fileEpoch = readTimestampFile.readEpoch(light);
		long currentEpoch = System.currentTimeMillis() / 1000;
		long delta = fileEpoch - currentEpoch;
		log.info(light + " - Curremt:" + currentEpoch + " Manual:" + fileEpoch + " Delta:" + delta);
		return delta > 0;
	}

	private void execute(List<String> commands) {
		log.info("About to turn " + commands.get(1) + " light " + commands.get(2));
		SystemCommandExecutor exec = null;
		int result = -9999;
		exec = new SystemCommandExecutor(commands);
		try {
			result = exec.executeCommand();
		} catch (IOException e) {
			log.error("Failed to change light " + commands.get(2), e);
		} catch (InterruptedException e) {
			log.error("Failed to change light " + commands.get(2), e);
		}

		if (!exec.getStandardOutputFromCommand().toString().contains(" - Success")) {
			log.error("Failed to execute command on light " + commands.get(2) + "! " + exec.getStandardOutputFromCommand().toString());
		}

		if (result != 0) {
			log.warn("Unexpected result from exec: " + result);
		}
	}
}
