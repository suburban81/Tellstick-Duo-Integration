package tell.logger.lights.time;

import org.joda.time.DateTime;

public interface LightsDecision {

	// Optional should let calling class make other descision
	// (like weather integration)
	public enum Decision {
		ON, OPTIONAL, OFF
	}

	public Decision decide(DateTime now);

}
