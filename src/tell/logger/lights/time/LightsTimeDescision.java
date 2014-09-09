package tell.logger.lights.time;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

/**
 * Calculate if lights should be turned on by look at time of day (depending on
 * date of the year)
 */
public class LightsTimeDescision implements LightsDecision {

	private final int WEEKEND_MORNINGS = 45;

	public Decision decide(DateTime now) {
		String startMonth = now.monthOfYear().getAsText(Locale.ENGLISH).toUpperCase();
		String nextMonth = now.plusMonths(1).monthOfYear().getAsText(Locale.ENGLISH).toUpperCase();

		Interval dawn = calcPeriod(now, Dawn.valueOf(startMonth).getStartLights(), Dawn.valueOf(startMonth).getStopLights(), Dawn.valueOf(nextMonth).getStartLights(), Dawn.valueOf(nextMonth)
				.getStopLights());

		LocalTime startLightsDusk = Dusk.valueOf(startMonth).getStartLights();
		LocalTime startLightsDuskNextMonth = Dusk.valueOf(nextMonth).getStartLights();

		if (now.getDayOfWeek() == DateTimeConstants.SATURDAY || now.getDayOfWeek() == DateTimeConstants.SUNDAY) {
			startLightsDusk = startLightsDusk.plusMinutes(WEEKEND_MORNINGS);
			startLightsDuskNextMonth = startLightsDuskNextMonth.plusMinutes(WEEKEND_MORNINGS);
		}

		Interval dusk = calcPeriod(now, startLightsDusk, Dusk.valueOf(startMonth).getStopLights(), startLightsDuskNextMonth, Dusk.valueOf(nextMonth).getStopLights());

		if (dawn.contains(now) || dusk.contains(now)) {
			return Decision.ON;
		} else {
			return Decision.OFF;
		}
	}

	private Interval calcPeriod(DateTime now, LocalTime start, LocalTime end, LocalTime startNextMonth, LocalTime endNextMonth) {
		double minutesPerDayStart = (double) Minutes.minutesBetween(start, startNextMonth).getMinutes() / now.dayOfMonth().getMaximumValue();
		double minutesPerDayEnd = (double) Minutes.minutesBetween(end, endNextMonth).getMinutes() / now.dayOfMonth().getMaximumValue();

		start = start.plusMinutes((int) (minutesPerDayStart * now.dayOfMonth().get()));
		end = end.plusMinutes((int) (minutesPerDayEnd * now.dayOfMonth().get()));

		return new Interval(now.withFields(start), now.withFields(end));
	}

}