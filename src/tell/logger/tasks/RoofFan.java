package tell.logger.tasks;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.api.TellStickDuo;
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

		if (betterOutside(roof, outside)) {
			// here we shall start fan
		} else {
			// here we shall stop fan
		}
	}

	private boolean betterOutside(Sensor roof, Sensor outside) {
		if (roof.getAbsoluteHumidity() > outside.getAbsoluteHumidity()) {
			log.debug("Better outside, start fan");
			return true;
		} else {
			log.debug("Better inside, stop fan");
			return false;
		}
	}
}
