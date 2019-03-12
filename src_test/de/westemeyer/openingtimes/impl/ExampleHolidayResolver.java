package de.westemeyer.openingtimes.impl;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.westemeyer.openingtimes.api.HolidayResolver;

/**
 * Example holiday resolver object, using jollyday library.
 * 
 * @author Sebastian Westemeyer
 *
 */
public class ExampleHolidayResolver implements HolidayResolver {
	@Override
	public boolean isHoliday(final ZonedDateTime testDate) {
		// create calendar instance from test date
		Calendar gc = GregorianCalendar.from(testDate);
		// use german holiday manager
		HolidayManager manager = HolidayManager.getInstance(HolidayCalendar.GERMANY);
		// check whether date is a holiday in specified region (NRW)
		return manager.isHoliday(gc, "nw");
	}
}
