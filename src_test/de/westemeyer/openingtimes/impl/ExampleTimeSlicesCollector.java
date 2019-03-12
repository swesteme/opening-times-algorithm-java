package de.westemeyer.openingtimes.impl;

import java.util.List;

import de.westemeyer.openingtimes.api.TimeSlice;
import de.westemeyer.openingtimes.api.TimeSlicesCollector;

/**
 * Example time slices collector.
 * 
 * @author Sebastian Westemeyer
 *
 */
public class ExampleTimeSlicesCollector implements TimeSlicesCollector {
	/** The list of time slices. */
	private final List<TimeSlice> timeSlices;

	/**
	 * Constructor.
	 * 
	 * @param slices list of time slices.
	 */
	public ExampleTimeSlicesCollector(final List<TimeSlice> slices) {
		this.timeSlices = slices;
	}

	@Override
	public List<TimeSlice> getTimeSlices() {
		return timeSlices;
	}
}
