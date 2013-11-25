package tell.logger.tasks;

import tell.logger.api.TellStickDuo;
import tell.logger.model.Sensor;

public class RoofFanDefault extends RoofFan {

	public RoofFanDefault(TellStickDuo duo) {
		super(duo);
	}

	@Override
	protected boolean betterOutside(Sensor roof, Sensor outside) {
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
