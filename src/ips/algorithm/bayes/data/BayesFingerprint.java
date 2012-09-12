package ips.algorithm.bayes.data;

import ips.data.entities.wlan.AccessPoint;
import ips.data.entities.wlan.AccessPointPowerLevels;
import ips.data.entities.wlan.WLANFingerprint;
import ips.data.entities.wlan.WLANMeasurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * A bayes fingerprint is a WLAN Fingerprint with intervals.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class BayesFingerprint extends WLANFingerprint {

	Map<AccessPoint, AccessPointPowerLevelsIntervals> intervalLevels;

	List<Interval> intervals;

	public BayesFingerprint(WLANFingerprint fp) {

		super(fp.getPosition(), fp.getMeasurement());

		createIntervalLevels();
	}

	/**
	 * 
	 * Create the different intervals and add the power levels to the interval
	 * amounts
	 * 
	 */
	private void createIntervalLevels() {

		this.intervals = createIntervals();

		this.intervalLevels = new HashMap<AccessPoint, AccessPointPowerLevelsIntervals>();

		for (Entry<AccessPoint, AccessPointPowerLevels> entry : this
				.getLevels().entrySet()) {

			AccessPoint ap = entry.getKey();
			List<Double> lvls = entry.getValue().getLevels();
			AccessPointPowerLevelsIntervals iLvls = new AccessPointPowerLevelsIntervals(
					lvls, intervals);

			intervalLevels.put(ap, iLvls);
		}
	}

	/**
	 * 
	 * @param lvl
	 *            A power level value
	 * @return The interval that matches for the value
	 */
	private Interval getInterval(int lvl) {

		for (Interval interval : intervals) {

			if (interval.checkValue(lvl)) {

				return interval;
			}
		}

		return null;
	}

	private AccessPoint getAccessPoint(AccessPoint ap) {

		for (AccessPoint entryAp : this.intervalLevels.keySet()) {

			if (ap.getBSSID().equals(entryAp.getBSSID())) {
				return entryAp;
			}
		}

		return null;
	}

	/**
	 * 
	 * Get the amoutn of an interval of a certain access point.
	 * 
	 * @param ap
	 *            The access point
	 * @param i
	 *            The interval to get the amount of
	 * @return The amount
	 */
	private int getIntervalCount(AccessPoint ap, Interval i) {

		AccessPoint entryAp = getAccessPoint(ap);

		if (entryAp != null) {

			return intervalLevels.get(entryAp).getIntervalCount(i);
		}

		return 1;
	}

	/**
	 * 
	 * Calculate the sum of the interval amounts given a measurement
	 * 
	 * @param mm
	 *            The measurement
	 * @return The total sum
	 */
	public int getIntervalSum(WLANMeasurement mm) {

		int sum = 1;

		for (Entry<AccessPoint, Integer> entry : mm.getLevels().entrySet()) {

			Interval interval = getInterval(entry.getValue());

			if (interval != null) {

				sum *= getIntervalCount(entry.getKey(), interval);
			}
		}

		return sum;
	}

	/**
	 * 
	 * Gets the highest amounts for each interval
	 * 
	 * @return A list with the intervals and their highest amount
	 */
	public List<Entry<Interval, Integer>> getHighestIntervalCounts() {

		List<Entry<Interval, Integer>> counts = new ArrayList<Map.Entry<Interval, Integer>>();

		for (Entry<AccessPoint, AccessPointPowerLevelsIntervals> entry : intervalLevels
				.entrySet()) {

			AccessPointPowerLevelsIntervals intervals = entry.getValue();

			Entry<Interval, Integer> count = intervals
					.getHighestIntervalCount();

			counts.add(count);

		}

		return counts;

	}

	/**
	 * 
	 * Creates the different intervals by implementing new interval classes that
	 * implement the checkValue method that will check the value then.
	 * 
	 * @return A list containing the intervals
	 */
	private List<Interval> createIntervals() {

		List<Interval> intervals = new ArrayList<Interval>();

		intervals.add(new Interval() {

			@Override
			public boolean checkValue(double level) {
				if (level > -30)
					return true;
				else
					return false;
			}
		});

		intervals.add(new Interval() {

			@Override
			public boolean checkValue(double level) {
				if ((level > -40) && (level <= -30))
					return true;
				else
					return false;
			}
		});

		intervals.add(new Interval() {

			@Override
			public boolean checkValue(double level) {
				if ((level > -50) && (level <= -40))
					return true;
				else
					return false;
			}
		});

		intervals.add(new Interval() {

			@Override
			public boolean checkValue(double level) {
				if ((level > -60) && (level <= -50))
					return true;
				else
					return false;
			}
		});

		intervals.add(new Interval() {

			@Override
			public boolean checkValue(double level) {
				if ((level > -70) && (level <= -60))
					return true;
				else
					return false;
			}
		});

		intervals.add(new Interval() {

			@Override
			public boolean checkValue(double level) {
				if ((level > -80) && (level <= -70))
					return true;
				else
					return false;
			}
		});

		return intervals;

	}

}
