package ips.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ips.data.DataManager;
import ips.data.entities.wlan.AccessPoint;
import ips.data.entities.wlan.AccessPointPowerLevels;
import ips.data.entities.wlan.WLANFingerprint;

/**
 * 
 * Can be used to analyze what is in the datastore.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class DataStoreAnalysis {

	DataManager dm;

	public double bestLvl;
	public double worstLvl;

	public DataStoreAnalysis() {
		super();
		this.dm = new DataManager();

		this.bestLvl = -100;
		this.worstLvl = 0;
	}

	/**
	 * 
	 * @return The average amount of access points that are in the fingerprints
	 *         in the data store.
	 */
	private int averageApAmount() {

		List<WLANFingerprint> fps = dm.getSampleData();

		List<Integer> amounts = new ArrayList<Integer>();

		for (WLANFingerprint fp : fps) {

			amounts.add(fp.getLevels().size());
		}

		int total = 0;

		for (int amount : amounts) {

			total += amount;
		}

		return total / amounts.size();
	}

	/**
	 * 
	 * @return The average power level from all the access point power levels
	 *         that are in the data store.
	 */
	private double averageLvl() {

		List<WLANFingerprint> fps = dm.getSampleData();

		List<Double> avgs = new ArrayList<Double>();

		for (WLANFingerprint fp : fps) {

			double avg = 0;

			for (Entry<AccessPoint, AccessPointPowerLevels> entry : fp
					.getLevels().entrySet()) {

				double lvl = entry.getValue().getAverage();

				avg += lvl;

				if (lvl > bestLvl) {
					bestLvl = lvl;
				}

				if (lvl < worstLvl) {

					worstLvl = lvl;
				}
			}

			avgs.add(avg / fp.getLevels().size());
		}

		double total = 0;

		for (double avg : avgs) {

			total += avg;
		}

		return total / avgs.size();
	}

	public static void main(String[] args) {

		DataStoreAnalysis ana = new DataStoreAnalysis();

		System.out.println("Average amount of aps = " + ana.averageApAmount());
		System.out.println("Average ap level = " + ana.averageLvl());

		System.out.println("Best level = " + ana.bestLvl);
		System.out.println("Worst level = " + ana.worstLvl);
	}

}
