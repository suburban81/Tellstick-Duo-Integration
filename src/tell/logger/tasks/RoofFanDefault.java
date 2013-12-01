package tell.logger.tasks;

import tell.logger.api.TellStickDuo;
import tell.logger.model.Sensor;

public class RoofFanDefault extends RoofFan {

	private static final String PREFIX = "FAN-DEF ";

	public RoofFanDefault(TellStickDuo duo) {
		super(duo);
	}

	@Override
	protected boolean betterOutside(Sensor roof, Sensor outside) {
		if (roof.getAbsoluteHumidity() > outside.getAbsoluteHumidity()) {
			log.debug(PREFIX + "Better outside, start fan");
			return true;
		} else {
			log.debug(PREFIX + "Better inside, stop fan");
			return false;
		}
	}
}
