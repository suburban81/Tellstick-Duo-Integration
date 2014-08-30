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
			logFan("Better outside", true);
			return true;
		} else {
			logFan("Better inside", false);
			return false;
		}
	}
	
	@Override
	protected String getPrefix() {
		return PREFIX;
	}
}
