package ips.algorithm.bayes.data;

import ips.data.entities.wlan.AccessPointPowerLevels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * The different intervals for power levels and the amount of values that are in
 * that interval.
 * 
 * For example, we can have an interval of -80 < value < -90 and three values
 * that are in that interval (e.g. -85, -87, -82).
 * 
 * @author Wouter Van Rossem
 * 
 */
public class AccessPointPowerLevelsIntervals extends AccessPointPowerLevels {

	Map<Interval, Integer> intervalAmounts;

	List<Interval> intervals;

	public AccessPointPowerLevelsIntervals(List<Double> lvls,
			List<Interval> intervals) {
		super(lvls);

		this.intervals = intervals;

		this.intervalAmounts = new HashMap<Interval, Integer>();

		// Filter levels on intervals
		// Count the nr of entries for that interval
		for (Interval interval : intervals) {

			intervalAmounts.put(interval, 0);

			for (double level : lvls) {

				if (interval.checkValue(level)) {

					int amount = intervalAmounts.get(interval);

					intervalAmounts.put(interval, amount + 1);
				}

			}

		}
	}

	/**
	 * 
	 * @param i The interval to get the amount of
	 * @return Returns how many values are in the interval
	 */
	public int getIntervalCount(Interval i) {

		if (intervalAmounts.containsKey(i)) {

			return intervalAmounts.get(i);
		}

		return 1;
	}

	/**
	 * 
	 * @return Returns the interval with the most amount of values that are in
	 *         that interval
	 */
	public Entry<Interval, Integer> getHighestIntervalCount() {

		Entry<Interval, Integer> highest = null;

		for (Map.Entry<Interval, Integer> entry : intervalAmounts.entrySet()) {

			if (highest == null) {
				highest = entry;
				break;
			}

			int value = entry.getValue();

			if (value > highest.getValue()) {
				highest = entry;
			}

		}

		return highest;

	}

}
