package de.westemeyer.openingtimes.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import de.westemeyer.openingtimes.api.WeekDay;
import de.westemeyer.openingtimes.api.TimeSlice;

/**
 * Example ITimeSlice implementation.
 * 
 * @author Sebastian Westemeyer
 *
 */
public class ExampleTimeSliceImpl implements TimeSlice {
	/** Start date/time for this time slice. */
	private final ZonedDateTime from;
	/** End date/time for this time slice. */
	private final ZonedDateTime to;
	/** Time of day when we will be open. */
	private final String starttime;
	/** Time of day when we will be closing. */
	private final String endtime;
	/** List of weekdays for which this setting will be applied. */
	private final Set<WeekDay> weekdays;
	/** Date time formatter for parsing. */
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

	/**
	 * Construct new instance and parse date.
	 * 
	 * @param from Start date/time for this time slice.
	 * @param to End date/time for this time slice.
	 * @param starttime Time of day when we will be open.
	 * @param endtime Time of day when we will be closing.
	 * @param weekdays List of weekdays for which this setting will be applied.
	 */
	public ExampleTimeSliceImpl(final String from, final String to, final String starttime, final String endtime, final Set<WeekDay> weekdays) {
		this.from = ZonedDateTime.parse(from + " +0000", FORMATTER);
		this.to = to == null ? null : ZonedDateTime.parse(to + " +0000", FORMATTER);
		this.starttime = starttime;
		this.endtime = endtime;
		this.weekdays = weekdays;
	}

	@Override
	public String getStartTime() {
		return starttime;
	}

	@Override
	public String getEndTime() {
		return endtime;
	}

	@Override
	public Set<WeekDay> getWeekdays() {
		return weekdays;
	}

	@Override
	public ZonedDateTime getFrom() {
		return from;
	}

	@Override
	public ZonedDateTime getTo() {
		return to;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(FORMATTER.format(from));
		buf.append(" - ");
		buf.append(FORMATTER.format(to));
		buf.append(": ");
		buf.append(starttime);
		buf.append(" - ");
		buf.append(endtime);
		buf.append(": ");
		buf.append(weekdays);
		return buf.toString();
	}
}
