package tell.logger.lights.time;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

public class LightsTimeDecisionTest {

	LightsDecision lightsDecision = new LightsTimeDescision();

	@Test
	public void testStartOfMonth() throws Exception {
		assertThat(lightsDecision.decide(DateTime.parse("2014-09-03T18:30:05Z")), is(LightsDecision.Decision.OFF));
	}

	@Test
	public void testEndOfMonth() throws Exception {
		assertThat(lightsDecision.decide(DateTime.parse("2014-09-27T18:30:05Z")), is(LightsDecision.Decision.ON));
	}

	@Test
	public void testStartOfMonthDusk() throws Exception {
		assertThat(lightsDecision.decide(DateTime.parse("2014-12-05T09:10:05Z")), is(LightsDecision.Decision.OFF));
	}

	@Test
	public void testEndOfMonthDusk() throws Exception {
		assertThat(lightsDecision.decide(DateTime.parse("2014-12-30T09:10:05Z")), is(LightsDecision.Decision.ON));
	}
	
	@Test
	public void testWeekendMornings() throws Exception {
		assertThat(lightsDecision.decide(DateTime.parse("2014-09-06T06:05:05Z")), is(LightsDecision.Decision.OFF));
		assertThat(lightsDecision.decide(DateTime.parse("2014-09-06T06:40:05Z")), is(LightsDecision.Decision.OFF));
		assertThat(lightsDecision.decide(DateTime.parse("2014-09-06T06:45:05Z")), is(LightsDecision.Decision.ON));
	}
	
	@Test
	public void testNormalMornings() throws Exception {
		assertThat(lightsDecision.decide(DateTime.parse("2014-09-05T05:55:05Z")), is(LightsDecision.Decision.OFF));
		assertThat(lightsDecision.decide(DateTime.parse("2014-09-05T06:05:05Z")), is(LightsDecision.Decision.ON));
	}
	
	
}
