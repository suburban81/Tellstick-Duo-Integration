package tell.logger.tasks;

import tell.logger.api.TellStickDuo;
import tell.logger.model.Sensor;

public class RoofFanRestrictive extends RoofFan {

	private static final String PREFIX = "FAN-RES ";

	public RoofFanRestrictive(TellStickDuo duo) {
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
		if (howMuchDrierOutside > 3) {
			logFan("Lot better outside", true);
			return true;
		}
		if (howMuchDrierOutside > 1.5) {
			if (howMuchVarmerOutside > 0) {
				logFan("Better and warmer outside", true);
				return true;
			} else {
				logFan("Better but colder outside", false);
				return false;
			}
		} else {
			logFan("Enough better inside, stop fan", false);
			return false;
		}
	}

	private boolean neutralTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 4) {
			logFan("Enough better outside", true);
			return true;
		} else {
			logFan("Enough better inside", false);
			return false;
		}
	}

	private boolean summerTimeFan(Double howMuchDrierOutside, Double howMuchVarmerOutside) {
		if (howMuchDrierOutside > 10) {
			logFan("Lot better outside", true);
			return true;
		}
		if (howMuchDrierOutside > 7) {
			if (howMuchVarmerOutside < 0) {
				logFan("Better and colder outside", true);
				return true;
			} else {
				logFan("Better but warmer outside", false);
				return false;
			}
		} else {
			logFan("Enough better inside", false);
			return false;
		}
	}
	
	@Override
	protected String getPrefix() {
		return PREFIX;
	}
}
