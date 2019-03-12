package de.westemeyer.openingtimes.impl;

import java.time.ZonedDateTime;

import de.westemeyer.openingtimes.api.OpenStatus;
import de.westemeyer.openingtimes.api.TimeSlice;
import de.westemeyer.openingtimes.api.WeekDay;

/**
 * "Return value" for checked dates.
 * 
 * @author Sebastian Westemeyer
 *
 */
public class OpenType {
	/** The weekday. */
	private final WeekDay weekday;
	/** The date. */
	private final ZonedDateTime date;
	/** Whether it is currently open. */
	private final OpenStatus status;
	/** The time slice that caused the current setting. */
	private final TimeSlice slice;
	/** the date that has been used to check for next opening times. */
	private final ZonedDateTime checkedDate;

	/**
	 * Construct new OpenType object.
	 * 
	 * @param weekday Weekday for this open type.
	 * @param date The date.
	 * @param status Whether it is currently open.
	 */
	public OpenType(final WeekDay weekday, final ZonedDateTime date, final ZonedDateTime checkedDate, final TimeSlice slice,
			final OpenStatus status) {
		this.weekday = weekday;
		this.date = date;
		this.slice = slice;
		this.status = status;
		this.checkedDate = checkedDate;
	}

	/**
	 * If there are no parameters, construct "closed" type.
	 */
	public OpenType() {
		this(null, null, null, null, OpenStatus.CLOSED);
	}

	/**
	 * Returns whether or not the requested timestamp is in current opening times.
	 * 
	 * @return whether or not the requested timestamp is in current opening times.
	 */
	public OpenStatus getStatus() {
		return status;
	}

	/**
	 * Returns the weekday for the next opening time.
	 * 
	 * @return the weekday for the next opening time.
	 */
	public WeekDay getWeekday() {
		return weekday;
	}

	/**
	 * Returns the date/time for the next opening time.
	 * 
	 * @return the date/time for the next opening time.
	 */
	public ZonedDateTime getDateTime() {
		return date;
	}

	/**
	 * Returns the time slice that caused the current setting.
	 * 
	 * @return the time slice that caused the current setting.
	 */
	public TimeSlice getTimeSlice() {
		return slice;
	}

	/**
	 * Returns the original date that has been used to check for next opening times.
	 * 
	 * @return the date that has been used to check for next opening times.
	 */
	public ZonedDateTime getCheckedDate() {
		return checkedDate;
	}
}
