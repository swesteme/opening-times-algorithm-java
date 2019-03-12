package de.westemeyer.openingtimes.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.westemeyer.openingtimes.api.HolidayResolver;
import de.westemeyer.openingtimes.api.TimeSlice;
import de.westemeyer.openingtimes.api.WeekDay;

/**
 * @author Sebastian Westemeyer
 */
public class OpeningTimesTest {
	/** Date and time formatter for parsing of expected and returned dates. */
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

	private static final Formatter FORMATTER = new Formatter();

	private static OpeningTimes TIMES1;
	private static OpeningTimes TIMES2;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final List<TimeSlice> list1 = new ArrayList<>();
		final List<TimeSlice> list2 = new ArrayList<>();
		final Set<WeekDay> weekday = EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY);
		final Set<WeekDay> daily = EnumSet.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY,
				WeekDay.SATURDAY, WeekDay.SUNDAY);
		final Set<WeekDay> sunday = EnumSet.of(WeekDay.SUNDAY);
		final Set<WeekDay> holiday = EnumSet.of(WeekDay.SUNDAY, WeekDay.HOLIDAY);
		final Set<WeekDay> weekend = EnumSet.of(WeekDay.SATURDAY, WeekDay.SUNDAY, WeekDay.HOLIDAY);
		list1.add(new ExampleTimeSliceImpl("2014-08-31 22:00:00", "2014-09-30 21:59:59", "14:00", "20:00", daily));
		list1.add(new ExampleTimeSliceImpl("2014-07-07 22:00:00", "2014-08-31 21:59:59", "12:00", "20:00", daily));
		list1.add(new ExampleTimeSliceImpl("2014-09-30 22:00:00", "2014-10-31 22:59:59", "14:00", "18:00", weekend));
		list1.add(new ExampleTimeSliceImpl("2014-04-29 22:00:00", "2014-07-07 21:59:59", "12:00", "20:00", weekend));
		list1.add(new ExampleTimeSliceImpl("2014-02-15 23:00:00", "2014-03-31 21:59:59", "14:00", "17:00", sunday));
		list1.add(new ExampleTimeSliceImpl("2014-04-29 22:00:00", "2014-07-07 21:59:59", "14:00", "20:00", weekday));
		list1.add(new ExampleTimeSliceImpl("2014-10-31 23:00:00", "2014-12-07 22:59:59", "14:00", "17:00", holiday));
		list1.add(new ExampleTimeSliceImpl("2014-03-31 22:00:00", "2014-04-29 21:59:59", "14:00", "18:00", weekend));
		list2.add(new ExampleTimeSliceImpl("2014-02-15 23:00:00", "2014-03-31 21:59:59", "10:00", "18:00", sunday));
		list2.add(new ExampleTimeSliceImpl("2014-10-31 23:00:00", "2014-12-07 22:59:59", "10:00", "18:00", holiday));
		list2.add(new ExampleTimeSliceImpl("2014-03-31 22:00:00", "2014-04-29 21:59:59", "10:00", "19:00", weekend));
		list2.add(new ExampleTimeSliceImpl("2014-09-30 22:00:00", "2014-10-31 22:59:59", "10:00", "19:00", weekend));
		list2.add(new ExampleTimeSliceImpl("2014-04-29 22:00:00", "2014-09-30 21:59:59", "10:00", "24:00", daily));

		final HolidayResolver holidays = new ExampleHolidayResolver();
		TIMES1 = new OpeningTimes(new ExampleTimeSlicesCollector(list1), holidays);
		TIMES2 = new OpeningTimes(new ExampleTimeSlicesCollector(list2), holidays);
	}

	@Test
	public void testEightDaysLater() {
		runTestForDate("2014-02-16 17:01:00 +0100", "Sunday, 23.02.2014 at 14:00", "open");
		runTestForDate("2014-02-16 18:01:00 +0100", "Sunday, 23.02.2014 at 14:00", "Sunday, 23.02.2014 at 10:00");
	}

	@Test
	public void testEvenings() {
		runTestForDate("2014-05-08 20:01:00 +0200", "Friday, 09.05.2014 at 14:00", "open");
		runTestForDate("2014-05-06 20:01:00 +0200", "Wednesday, 07.05.2014 at 14:00", "open");
		runTestForDate("2014-05-06 20:00:00 +0200", "open", "open");
		runTestForDate("2014-05-06 19:59:59 +0200", "open", "open");
	}

	@Test
	public void testMornings() {
		runTestForDate("2014-05-06 14:01:00 +0200", "open", "open");
		runTestForDate("2014-05-06 10:01:00 +0200", "Today, 06.05.2014 at 14:00", "open");
		runTestForDate("2014-05-06 09:59:00 +0200", "Today, 06.05.2014 at 14:00", "Today, 06.05.2014 at 10:00");
	}

	@Test
	public void testBankHolidayInMiddleOfWeek() {
		runTestForDate("2014-05-01 10:01:00 +0200", "Bank holiday:Today, 01.05.2014 at 12:00", "open");
		runTestForDate("2014-05-01 12:01:00 +0200", "open", "open");
		runTestForDate("2014-05-01 09:59:00 +0200", "Bank holiday:Today, 01.05.2014 at 12:00", "Bank holiday:Today, 01.05.2014 at 10:00");
	}

	@Test
	public void testBeforeSwitchToNextTimeSlice() {
		// Friday
		runTestForDate("2014-04-25 10:01:00 +0200", "Saturday, 26.04.2014 at 14:00", "Saturday, 26.04.2014 at 10:00");
		// Sunday
		runTestForDate("2014-04-27 09:59:00 +0200", "Today, 27.04.2014 at 14:00", "Today, 27.04.2014 at 10:00");
		runTestForDate("2014-04-27 10:01:00 +0200", "Today, 27.04.2014 at 14:00", "open");
		runTestForDate("2014-04-27 14:01:00 +0200", "open", "open");
		// Monday
		runTestForDate("2014-04-28 10:01:00 +0200", "Wednesday, 30.04.2014 at 14:00", "Wednesday, 30.04.2014 at 10:00");
	}

	@Test
	public void testBeforeBankHoliday() {
		runTestForDate("2014-04-19 14:01:00 +0200", "open", "open");
		runTestForDate("2014-04-19 10:01:00 +0200", "Today, 19.04.2014 at 14:00", "open");
		runTestForDate("2014-04-19 09:59:00 +0200", "Today, 19.04.2014 at 14:00", "Today, 19.04.2014 at 10:00");

		runTestForDate("2014-04-30 14:01:00 +0200", "open", "open");
		runTestForDate("2014-04-30 10:01:00 +0200", "Today, 30.04.2014 at 14:00", "open");
		runTestForDate("2014-04-30 09:59:00 +0200", "Today, 30.04.2014 at 14:00", "Today, 30.04.2014 at 10:00");
	}

	@Test
	public void testBeforeDaylightSavingsTime() {
		runTestForDate("2014-03-27 23:01:00 +0100", "Sunday, 30.03.2014 at 14:00", "Sunday, 30.03.2014 at 10:00");
	}

	@Test
	public void testEndOfSeason() {
		runTestForDate("2014-12-20 12:00:00 +0100", "closed", "closed");
	}

	private void runTestForDate(final String timeString, final String expected1, final String expected2) {
		ZonedDateTime dateTime = ZonedDateTime.parse(timeString, DATE_FORMATTER);
		Assert.assertNotNull(dateTime);
		log("===== Test date: " + timeString + " =====");
		runTest(TIMES1, dateTime, expected1);
		runTest(TIMES2, dateTime, expected2);
	}

	private void runTest(final OpeningTimes openingTimes, final ZonedDateTime dateTime, final String expected) {
		OpenType openType = openingTimes.createOpenType(dateTime);
		String formattedType = FORMATTER.formatOpenType(openType);
		log("Value (expected): " + expected);
		log("Value:            " + formattedType);
		Assert.assertEquals(expected, formattedType);
	}

	public void log(final String output) {
		System.out.println(output);
	}
}
