package tell.logger.tasks;

import tell.logger.api.TellStickDuo;
import tell.logger.model.Sensor;

public class RoofFanTemperatureControl extends RoofFan {

	private static final String PREFIX = "FAN-TMP ";

	public RoofFanTemperatureControl(TellStickDuo duo) {
		super(duo);
	}

	@Override
	protected boolean betterOutside(Sensor roof, Sensor outside) {
		Double howMuchDrierOutside = roof.getAbsoluteHumidity() - outside.getAbsoluteHumidity();
		Double howMuchVarmerOutside = outside.getTempDouble() - roof.getTempDouble();
		if (roof.getTempDouble() < 4) {
			return winterTimeFan(howMuchDrierOutside, howMuchVarmerOutside);
		} else if (roof.getTempDouble() < 20) {
			return neutralTimeFan(howMuchDrierOutside, howMuchVarmerOutside);
		} else {
			return summerTimeFan(howMuchDrierOutside, howMuchVarmerOutside);
		}
	}

	private boolean winterTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 1) {
			log.debug(PREFIX + "Lot better outside, start fan");
		}
		if (howMuchDrierOutside > 0.1) {
			if (howMuchVarmerOutside > 0) {
				log.debug(PREFIX + "Better and warmer outside, start fan");
				return true;
			} else {
				log.debug(PREFIX + "Better but colder outside, stop fan");
				return false;
			}
		} else {
			log.debug(PREFIX + "Better inside, stop fan");
			return false;
		}
	}

	private boolean neutralTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 0.3) {
			log.debug(PREFIX + "Enough better outside, start fan");
			return true;
		} else {
			log.debug(PREFIX + "Enough better inside, stop fan");
			return false;
		}
	}

	private boolean summerTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 3) {
			log.debug(PREFIX + "Lot better outside, start fan");
		}
		if (howMuchDrierOutside > 0.3) {
			if (howMuchVarmerOutside < 0) {
				log.debug(PREFIX + "Better and colder outside, start fan");
				return true;
			} else {
				log.debug(PREFIX + "Better but warmer outside, stop fan");
				return false;
			}
		} else {
			log.debug(PREFIX + "Better inside, stop fan");
			return false;
		}
	}
}
