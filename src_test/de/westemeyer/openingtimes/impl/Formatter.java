package de.westemeyer.openingtimes.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import de.westemeyer.openingtimes.api.WeekDay;

/**
 * Formatter object for time slices.
 * 
 * @author Sebastian Westemeyer
 *
 */
public class Formatter {
	/** Date and time formatter date output. */
	private final DateTimeFormatter dateFormatter;
	/** Date and time formatter weekday output. */
	private final DateTimeFormatter weekdayFormatter;

	/**
	 * Create new formatter object.
	 */
	public Formatter() {
		// locale the locale for weekday names.
		final Locale locale = Locale.ENGLISH;
		dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm", locale);
		weekdayFormatter = DateTimeFormatter.ofPattern("EEEE", locale);
	}

	/**
	 * Format a weekday.
	 * 
	 * @param openType the open type
	 * @return String for weekday.
	 */
	public String formatWeekday(final OpenType openType) {
		final StringBuffer buff = new StringBuffer();
		if (openType.getWeekday() == WeekDay.HOLIDAY) {
			buff.append("Bank holiday:");
		}
		if (openType.getCheckedDate().toLocalDate().equals(openType.getDateTime().toLocalDate())) {
			buff.append("Today");
		} else {
			buff.append(weekdayFormatter.format(openType.getDateTime().getDayOfWeek()));
		}
		return buff.toString();
	}

	/**
	 * Format date and time.
	 * 
	 * @param dateTime Zoned date and time.
	 * @return date and time string.
	 */
	public String formatDateTime(ZonedDateTime dateTime) {
		return dateFormatter.format(dateTime);
	}

	/**
	 * Format open type, depending on status.
	 * 
	 * @param openType open type to format.
	 * @return current open type description.
	 */
	public String formatOpenType(OpenType openType) {
		if (openType == null) {
			return "null";
		}
		switch (openType.getStatus()) {
		case OPENING_SOON:
			StringBuffer buf = new StringBuffer(formatWeekday(openType));
			buf.append(", ");
			buf.append(formatDateTime(openType.getDateTime()));
			return buf.toString();
		case CLOSED:
			return "closed";
		case OPEN:
			return "open";
		}
		return null;
	}
}
