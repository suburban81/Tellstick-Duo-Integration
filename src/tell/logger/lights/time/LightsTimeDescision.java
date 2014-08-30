package tell.logger.lights.time;

/**
 * Calculate if lights should be turned on by look at time of day (depending on
 * date of the year) Optional should let calling class make other descision
 * (like weather integration)
 */
public class LightsTimeDescision {

	public enum Decision {
		ON, OPTIONAL, OFF
	}

	public Decision check() {
		return Decision.OFF;
	}

}