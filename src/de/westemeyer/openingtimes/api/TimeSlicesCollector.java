package de.westemeyer.openingtimes.api;

import java.util.List;

/**
 * 
 * Supplier for any number of @{link ITimeSlice}s to be analysed.
 * 
 * @author Sebastian Westemeyer
 *
 */
public interface TimeSlicesCollector {
	/**
	 * Provides all available time slices.
	 * 
	 * @return all available time slices.
	 */
	List<TimeSlice> getTimeSlices();
}
