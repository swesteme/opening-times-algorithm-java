package de.westemeyer.openingtimes.api;

import java.time.DayOfWeek;

/**
 *
 * Enum as wrapper for DayOfWeek with additional bank holiday.
 * 
 * @author Sebastian Westemeyer
 *
 */
public enum WeekDay {
	/** Monday. */
	MONDAY(DayOfWeek.MONDAY),
	/** Tuesday. */
	TUESDAY(DayOfWeek.TUESDAY),
	/** Wednesday. */
	WEDNESDAY(DayOfWeek.WEDNESDAY),
	/** Thursday. */
	THURSDAY(DayOfWeek.THURSDAY),
	/** Friday. */
	FRIDAY(DayOfWeek.FRIDAY),
	/** Saturday. */
	SATURDAY(DayOfWeek.SATURDAY),
	/** Sunday. */
	SUNDAY(DayOfWeek.SUNDAY),
	/** Bank holiday. */
	HOLIDAY(null);

	/** Day of week for all except "bank holiday". */
	private final DayOfWeek dayOfWeek;

	/** Constructor. */
	private WeekDay(final DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/** Get the wrapped DayOfWeek enum. */
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	/** Get enum value for day of week or bank holiday. */
	public static WeekDay valueOf(final DayOfWeek day, final boolean isHoliday) {
		if (isHoliday) {
			return HOLIDAY;
		}
		for (final WeekDay dayOfWeek : values()) {
			if (dayOfWeek.dayOfWeek.equals(day)) {
				return dayOfWeek;
			}
		}
		return null;
	}
}
