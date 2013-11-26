package tell.logger.tasks;

import tell.logger.api.TellStickDuo;
import tell.logger.model.Sensor;

public class RoofFanTemperatureControl extends RoofFan {

	public RoofFanTemperatureControl(TellStickDuo duo) {
		super(duo);
	}

	@Override
	protected boolean betterOutside(Sensor roof, Sensor outside) {
		Double howMuchDrierOutside = roof.getAbsoluteHumidity() - outside.getAbsoluteHumidity();
		Double howMuchVarmerOutside = outside.getTempDouble() - roof.getTempDouble();
		if (roof.getTempDouble() < 5) {
			return winterTimeFan(howMuchDrierOutside, howMuchVarmerOutside);
		} else if (roof.getTempDouble() < 20) {
			return neutralTimeFan(howMuchDrierOutside, howMuchVarmerOutside);
		} else {
			return summerTimeFan(howMuchDrierOutside, howMuchVarmerOutside);
		}
	}

	private boolean winterTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 1.5) {
			log.debug("Lot better outside, start fan");
		}
		if (howMuchDrierOutside > 0) {
			if (howMuchVarmerOutside > 0) {
				log.debug("Better and warmer outside, start fan");
				return true;
			} else {
				log.debug("Colder outside, stop fan");
				return false;
			}
		} else {
			log.debug("Better inside, stop fan");
			return false;
		}
	}

	private boolean neutralTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 0.3) {
			log.debug("Enough better outside, start fan");
			return true;
		} else {
			log.debug("Enough better inside, stop fan");
			return false;
		}
	}

	private boolean summerTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 3) {
			log.debug("Lot better outside, start fan");
		}
		if (howMuchDrierOutside > 0) {
			if (howMuchVarmerOutside < 0) {
				log.debug("Better and colder outside, start fan");
				return true;
			} else {
				log.debug("warmer outside, stop fan");
				return false;
			}
		} else {
			log.debug("Better inside, stop fan");
			return false;
		}
	}
}
