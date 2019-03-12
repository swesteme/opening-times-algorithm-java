package de.westemeyer.openingtimes.api;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Time slice that has to be provided by implementation.
 * 
 * @author Sebastian Westemeyer
 *
 */
public interface TimeSlice {
	/** Date, when the actual opening times setting begins. Usually from midnight. */
	ZonedDateTime getFrom();

	/** Date, until the actual opening times setting ends. Usually close before midnight. */
	ZonedDateTime getTo();

	/** Time, when we are open. */
	String getStartTime();

	/** Time, when we close. */
	String getEndTime();

	/** The weekdays for this setting. */
	Set<WeekDay> getWeekdays();
}
