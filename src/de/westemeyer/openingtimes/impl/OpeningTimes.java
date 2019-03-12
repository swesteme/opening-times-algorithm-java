package de.westemeyer.openingtimes.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.westemeyer.openingtimes.api.HolidayResolver;
import de.westemeyer.openingtimes.api.OpenStatus;
import de.westemeyer.openingtimes.api.TimeSlice;
import de.westemeyer.openingtimes.api.TimeSlicesCollector;
import de.westemeyer.openingtimes.api.WeekDay;

/**
 * 
 * Class to analyse a list of opening times, together with a test date/time.
 * 
 * @author Sebastian Westemeyer
 *
 */
public class OpeningTimes {
	/** List of time slices. */
	private final TimeSlicesCollector timeSlicesCollector;
	/** The holiday resolver. */
	private final HolidayResolver holidayResolver;

	/**
	 * Creates a new OpeningTimes object.
	 * 
	 * @param slices List of time slices.
	 * @param holidayResolver Object to check dates for holidays.
	 */
	public OpeningTimes(final TimeSlicesCollector slices, final HolidayResolver holidayResolver) {
		this.timeSlicesCollector = slices;
		this.holidayResolver = holidayResolver;
	}

	/**
	 * The actual check for upcoming opening times.
	 * 
	 * @param date The test date.
	 * @return The opening type (open, opening soon or closed).
	 */
	public OpenType createOpenType(final ZonedDateTime date) {
		// one week from today
		final ZonedDateTime nextWeek = addDays(date, 8);
		// get relevant slices
		List<TimeSlice> slices = relevantSlices(date);

		// keep current time as reference
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String currentTime = timeFormatter.format(date);

		final Map<LocalDate, TimeSlice> currentMap = new HashMap<>();
		// iterate all slices
		for (final TimeSlice slice : slices) {
			// have to use same instant
			ZonedDateTime fromDate = slice.getFrom().withZoneSameInstant(date.getZone());
			// check one week of dates
			for (ZonedDateTime dateIter = laterDate(date, fromDate); dateIter.isBefore(nextWeek); dateIter = addDays(dateIter, 1)) {
				// we have not yet reached end of slice
				if (dateIter.isBefore(slice.getTo())) {
					// weekday
					final DayOfWeek currentDay = dateIter.getDayOfWeek();

					for (final WeekDay dayOfWeek : slice.getWeekdays()) {
						// check for matches
						if ((dayOfWeek.getDayOfWeek() != null && dayOfWeek.getDayOfWeek().equals(currentDay))
								|| (dayOfWeek == WeekDay.HOLIDAY && holidayResolver.isHoliday(dateIter))) {
							// date part of the iterator
							LocalDate localDate = dateIter.toLocalDate();
							// get already set last matching slice
							final TimeSlice previouslySetSlice = currentMap.get(localDate);
							// if storage object at index is null, store current slice
							if (previouslySetSlice == null) {
								currentMap.put(localDate, slice);
							} else {
								// check, whether new object would be "earlier match"
								if (previouslySetSlice.getStartTime().compareTo(slice.getStartTime()) > 0) {
									currentMap.put(localDate, slice);
								}
							}
						}

					}
				}
			}
		}

		// check one week of dates for the earliest
		for (ZonedDateTime dateIter = date; dateIter.isBefore(nextWeek); dateIter = addDays(dateIter, 1)) {

			// check for a slice matching the date
			final TimeSlice slice = currentMap.get(dateIter.toLocalDate());

			// is there a matching open type for the date?
			if (slice != null) {
				// determine day of week (monday to sunday, or bank holiday)
				final WeekDay dayOfWeek = WeekDay.valueOf(dateIter.getDayOfWeek(), holidayResolver.isHoliday(dateIter));
				// compile timestamp for checks
				ZonedDateTime openingDateTime = ZonedDateTime.of(dateIter.toLocalDate(),
						LocalTime.parse(slice.getStartTime(), timeFormatter), dateIter.getZone());
				// either we will be opening today...
				if (!dateIter.isEqual(date) || slice.getStartTime().compareTo(currentTime) >= 0) {
					return new OpenType(dayOfWeek, openingDateTime, date, slice, OpenStatus.OPENING_SOON);
				} else if (slice.getStartTime().compareTo(currentTime) < 0 && (slice.getEndTime().compareTo(currentTime) >= 0)) {
					// ... or we are already open
					return new OpenType(dayOfWeek, openingDateTime, date, slice, OpenStatus.OPEN);
				}

				// ...otherwise, if none of the conditions match, we are already closed for today.
			}

		}
		// end of season/closed
		return new OpenType();
	}

	/**
	 * Compare two dates and return the later date of the two candidates.
	 * 
	 * @param date1 first date in comparison
	 * @param date2 second date in comparison
	 * @return the later date of the two candidates.
	 */
	private ZonedDateTime laterDate(final ZonedDateTime date1, final ZonedDateTime date2) {
		if (date1.isAfter(date2)) {
			return date1;
		}
		return date2;
	}

	/**
	 * Get relevant time slices, sorted by "from" member.
	 * 
	 * @param date The reference date to determine the relevant time slices.
	 * @return the relevant time slices sorted by "from" member.
	 */
	public List<TimeSlice> relevantSlices(final ZonedDateTime date) {
		final ZonedDateTime nextWeek = addDays(date, 8);
		return timeSlicesCollector.getTimeSlices().stream()
				.filter(s -> isInRange(s.getFrom(), date, nextWeek) || isInRange(s.getTo(), date, nextWeek)
						|| (s.getFrom().isBefore(date) && s.getTo().isAfter(nextWeek)))
				.sorted(Comparator.comparing(TimeSlice::getFrom)).collect(Collectors.toList());
	}

	/**
	 * Check whether a supplied date is in the range between two other dates.
	 * 
	 * @param testDate test date which is supposed to be in between the other ones.
	 * @param lowerBound lower bound date (from)
	 * @param upperBound upper bound date (to)
	 * @return whether or not, test date is in range of the two dates.
	 */
	private boolean isInRange(final ZonedDateTime testDate, final ZonedDateTime lowerBound, final ZonedDateTime upperBound) {
		return (testDate.isAfter(lowerBound) || testDate.isEqual(lowerBound))
				&& (testDate.isBefore(upperBound) || testDate.isEqual(upperBound));
	}

	/**
	 * Add a number of days to supplied date.
	 * 
	 * @param date Date to add days to.
	 * @param days number of days to add
	 * @return new date object
	 */
	private ZonedDateTime addDays(final ZonedDateTime date, final int days) {
		return date.plus(Period.ofDays(days));
	}
}
