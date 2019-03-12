package de.westemeyer.openingtimes.api;

import java.time.ZonedDateTime;

/**
 * Holiday resolver interface.
 * 
 * @author Sebastian Westemeyer
 *
 */
public interface HolidayResolver {
	/**
	 * Determines, whether a date is a local holiday.
	 * 
	 * @param testDate test date
	 * @return whether a date is a local holiday
	 */
	boolean isHoliday(ZonedDateTime testDate);
}
