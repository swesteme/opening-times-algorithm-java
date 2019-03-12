# Upcoming opening times algorithm
[![Build Status](https://travis-ci.org/swesteme/opening-times-algorithm-java.svg?branch=master)](https://travis-ci.org/swesteme/opening-times-algorithm-java)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://lbesson.mit-license.org/)

This algorithm has been converted from Objective C code of the Toeppersee iOS app ([App Store link](https://itunes.apple.com/de/app/toeppersee/id793480458?mt=8)), to be used in its Android counterpart. It can be used to find out (for any timestamp), when a restaurant,  or in our case wakeboard lift will be open. Most important for opening times that are subject to seasonal changes.

![Season](https://github.com/swesteme/opening-times-algorithm-java/raw/master/Documentation/season.png)

What we really want to know is, whether we should go wakeboard _now_, _later today_ or maybe on _Thursday afternoon_, because it is a bank holiday.

![Season](https://github.com/swesteme/opening-times-algorithm-java/raw/master/Documentation/home.png)

## Installation
Copy the content of `src` directory to project's source directory.

## Usage
A list of opening times (slices) implementing the `TimeSlice` interface is returned by a `TimeSlicesCollector` object.

```java
public class ExampleTimeSlicesCollector implements TimeSlicesCollector {
	/** The list of time slices. */
	private final List<TimeSlice> timeSlices;

	/**
	 * Constructor.
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
```

A `TimeSlice` object "knows" its own validity period, and at what time of which weekdays or bank holidays it is effective. A restaurant might be open at different times on weekdays or weekends.

To be able to determine whether a date is a bank holiday or not, a `HolidayResolver` is necessary. If the time slices do not make use of bank holiday setting, it is possible to create an instance that always returns `false`.

Both `TimeSlicesCollector` and `HolidayResolver` are passed to a new instance of `OpeningTimes`, which can then in turn be asked for opening times at specific date/time instants (or instances).

```java
final OpeningTimes openingTimes = new OpeningTimes(new ExampleTimeSlicesCollector(list), holidays);
OpenType openType = openingTimes.createOpenType(dateTime);
```
The returned `OpenType` has all the information necessary to inform the user about whether or not our restaurant (or wakeboard lift) is open at the instant of `dateTime`.

For more details see the example implementation in the test package.

## Communication
- If you **found a bug**, open an issue.
- If you **have a feature request**, open an issue.
- If you **want to contribute**, submit a pull request.
- If you would like to meet uns or learn wakeboarding, [visit us in Duisburg](https://www.toeppersee.de).
- If you would prefer the original Objective C algorithm or maybe a port to Swift, just let me know ;-).

## License
The algorithm is available under the MIT license. See the LICENSE file for more info.

Copyright (c) 2019 Sebastian Westemeyer