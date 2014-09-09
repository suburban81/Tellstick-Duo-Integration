package tell.logger.lights.time;

import org.joda.time.LocalTime;

public enum Dusk {
	JANUARY("T06:00:0", "T09:29:0"), 
	FEBRUARY("T06:00:0", "T08:52:0"), 
	MARCH("T06:00:0", "T08:10:0"), 
	APRIL("T06:00:0", "T08:10:0"), 
	MAY("T06:00:0", "T08:10:0"), 
	JUNE("T06:00:0", "T08:10:0"), 
	JULY("T06:00:0", "T08:10:0"), 
	AUGUST("T06:00:0", "T08:10:0"), 
	SEPTEMBER("T06:00:0", "T08:10:0"), 
	OCTOBER("T06:00:0", "T08:10:0"), 
	NOVEMBER("T06:00:0", "T08:30:0"), 
	DECEMBER("T06:00:0", "T09:05:0");

	private LocalTime startLights;
	private LocalTime stopLights;

	private Dusk(String startTime, String stopTime) {
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
