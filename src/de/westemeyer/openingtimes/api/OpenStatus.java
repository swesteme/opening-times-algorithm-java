package de.westemeyer.openingtimes.api;

/**
 * Open status open, closed (for example for end of season), or opening soon.
 * 
 * @author Sebastian Westemeyer
 *
 */
public enum OpenStatus {
	/** Status is "open". */
	OPEN,
	/** Status is "closed". */
	CLOSED,
	/** Will be opening soon. */
	OPENING_SOON;
}
