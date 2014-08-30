package tell.logger.lights.time;

import org.joda.time.LocalTime;

public enum Dawn {
	JANUARY("T14:32:0", "T23:30:0"), 
	FEBRUARY("T15:30:0", "T23:30:0"), 
	MARCH("T16:31:0", "T23:30:0"), 
	APRIL("T18:35:0", "T23:30:0"), 
	MAY("T19:39:0", "T23:30:0"), 
	JUNE("T20:33:0", "T23:30:0"), 
	JULY("T20:48:0", "T23:30:0"), 
	AUGUST("T20:06:0", "T23:30:0"), 
	SEPTEMBER("T18:52:0", "T23:30:0"), 
	OCTOBER("T17:32:0", "T23:30:0"), 
	NOVEMBER("T15:16:0", "T23:30:0"), 
	DECEMBER("T14:27:0", "T23:30:0");

	private LocalTime startLights;
	private LocalTime stopLights;

	private Dawn(String startTime, String stopTime) {
		startLights = LocalTime.parse(startTime);
		stopLights = LocalTime.parse(stopTime);
	}

	public LocalTime getStartLights() {
		return startLights;
	}

	public LocalTime getStopLights() {
		return stopLights;
	}
}
