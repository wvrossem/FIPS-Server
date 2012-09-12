package ips.algorithm.knn;

import ips.algorithm.Context;
import ips.algorithm.PositioningAlgorithm;
import ips.algorithm.PositioningResult;
import ips.data.DataManager;
import ips.data.entities.wlan.AccessPoint;
import ips.data.entities.wlan.AccessPointPowerLevels;
import ips.data.entities.wlan.WLANFingerprint;
import ips.data.entities.wlan.WLANMeasurement;
import ips.other.InsertionSort;
import ips.other.QuickSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * The nearest neighbor algorithm implemented using the ForkJoin framework.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class NearestNeighborAlgorithm implements PositioningAlgorithm {

	Context context;

	/**
	 * Needed by ForkJoin to manage the tasks
	 */
	static final ForkJoinPool fjPool = new ForkJoinPool();

	public NNResults results;

	public NearestNeighborAlgorithm(Context context) {

		this.context = context;
	}

	/**
	 * Calculate the position from a WLANMeasurement
	 */
	@Override
	public PositioningResult calculatePosition(WLANMeasurement measurements) {

		/**
		 * First we retrieve our samples from the datastore
		 */
		DataManager dm = context.getDataManager();

		List<String> bssids = new ArrayList<String>();

		for (AccessPoint ap : measurements.getLevels().keySet()) {
			bssids.add(ap.getBSSID());
		}

		List<WLANFingerprint> fps = dm.getTrainingDataWithBSSIDs(bssids);

		int fpsSize = fps.size();

		/**
		 * Put the in an array so that the algorihtm can use them
		 */
		WLANFingerprint[] fingerprints = new WLANFingerprint[fpsSize];

		for (int i = 0; i < fpsSize; i++) {

			fingerprints[i] = fps.get(i);
		}

		/**
		 * Calculate the distances from this measurement to the different
		 * figerprints
		 */
		Map<WLANFingerprint, DistanceResult> dists = calculateDistances(
				fingerprints, measurements);

		/**
		 * Find the smallest distance from all the distance results
		 */
		findSmallestDistance(dists);

		return results;
	}

	/**
	 * 
	 * Starts the calculation of the distances by starting a new task
	 * 
	 * @param fps
	 *            The sample fingerprints
	 * @param m
	 *            The new measurement
	 * @return A map containing the different fingerprints and their
	 *         corresponding distance result
	 */
	private Map<WLANFingerprint, DistanceResult> calculateDistances(
			WLANFingerprint[] fps, WLANMeasurement m) {

		return fjPool.invoke(new EuclDistTask(fps, m, 0, fps.length));

	}

	/**
	 * Finds the most useful and smallest distances in a <Fingerprint,
	 * Distanceresult> Map.
	 * 
	 * First does a quicksort on the matched number of distances. A limited
	 * number of values is taken from this and sorted again, now using an
	 * insertion sort, on there distances.
	 * 
	 * @param dists
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Entry<WLANFingerprint, DistanceResult> findSmallestDistance(
			Map<WLANFingerprint, DistanceResult> dists) {

		/**
		 * Change the map into an array so that it can be sorted by the
		 * QuickSort algorithm.
		 */
		Entry<WLANFingerprint, DistanceResult>[] entries = (Entry<WLANFingerprint, DistanceResult>[]) new Map.Entry[dists
				.size()];
		dists.entrySet().toArray(entries);

		/**
		 * Sort them using the quicksort algorithm
		 */
		entries = new QuickSort().sort(entries);

		/**
		 * Take a subset of the data and sort this using InsertionSort
		 */
		int PARTIAL_AMOUNT = 5;

		if (entries.length < PARTIAL_AMOUNT) {

			PARTIAL_AMOUNT = entries.length;
		}

		Map.Entry<WLANFingerprint, DistanceResult>[] partialSet = (Map.Entry<WLANFingerprint, DistanceResult>[]) new Map.Entry[PARTIAL_AMOUNT];

		int size = (entries.length - 1);
		int j = 0;
		for (int i = size; i > (size - PARTIAL_AMOUNT); i--) {

			partialSet[j++] = entries[i];
		}

		entries = new InsertionSort()
				.sort((Entry<WLANFingerprint, DistanceResult>[]) partialSet);

		/**
		 * The final result is stored in a special NNResults object which
		 * contains an list of all the DistanceResults
		 */
		List<DistanceResult> distances = new ArrayList<DistanceResult>();
		for (Entry<WLANFingerprint, DistanceResult> entry : entries) {

			distances.add(entry.getValue());
		}

		results = new NNResults(distances);

		results.setResults(distances);

		return entries[0];
	}

	/**
	 * A {@link RecursiveTask} that will compute the distance between the
	 * measurement and an array of fingerprints.
	 * 
	 * The result is stored in a map that has as keys the fingerprint to which
	 * the distance is calculated and as value the {@link DistanceResult}.
	 * 
	 * @author Wouter Van Rossem
	 * 
	 */
	class EuclDistTask extends
			RecursiveTask<Map<WLANFingerprint, DistanceResult>> {

		private static final long serialVersionUID = 1L;

		int SEQUENTIAL_CUTOFF = 100;

		private WLANFingerprint[] fps;
		private WLANMeasurement mm;

		int low, high;

		private Map<WLANFingerprint, DistanceResult> res;

		public EuclDistTask(WLANFingerprint[] fps, WLANMeasurement mm, int low,
				int high) {

			this.fps = fps;
			this.mm = mm;

			this.low = low;
			this.high = high;

			this.res = new HashMap<WLANFingerprint, DistanceResult>();
		}

		/**
		 * Computes the distance between the measurement and an array of
		 * fingerprints by calculating it immediately if the sequential cutoff
		 * point is reached or forking two new threads and waiting to join the
		 * results.
		 */
		@Override
		protected Map<WLANFingerprint, DistanceResult> compute() {

			if (high - low < SEQUENTIAL_CUTOFF) {

				/**
				 * The array is small enough to calculate sequentially.
				 * 
				 * Loop over the fingerprints and calculate the distance from
				 * the measurement to itself.
				 */
				for (int i = low; i < high; i++) {

					WLANFingerprint fp = fps[i];

					DistanceResult distance = calcEuclDist(mm, fp);

					// Put the result in the map
					res.put(fp, distance);
				}

				return res;

			} else {

				/**
				 * The array is still large enough an can so the task can be
				 * further forked to two separate tasks.
				 * 
				 * The array is divided in two parts for the tasks.
				 */
				int mid = (int) Math.floor((high + low) / 2);

				EuclDistTask t1 = new EuclDistTask(fps, mm, low, mid);
				EuclDistTask t2 = new EuclDistTask(fps, mm, mid + 1, high);

				t1.fork();

				Map<WLANFingerprint, DistanceResult> res = t2.compute();
				Map<WLANFingerprint, DistanceResult> res2 = t1.join();

				// Combine the results of the two tasks
				res.putAll(res2);

				return res;
			}

		}

		/**
		 * Calculates the Euclidian distance between a measurement and a
		 * fingerprint.
		 * 
		 * @param mm
		 *            The measurement
		 * @param fp
		 *            The fingerprint to calculate the distance
		 * @return
		 */
		private DistanceResult calcEuclDist(WLANMeasurement mm,
				WLANFingerprint fp) {

			double d = 0;

			/**
			 * We keep track of exactly how many access points with the same
			 * BSSID occur in the measurement and the fingerprint. Since a
			 * higher amount of equal access points will lead to a better
			 * result.
			 */
			int nrOfMatchedBssids = 0;

			/**
			 * Loop over each access point from the measurement, get the access
			 * point average power interval of a access point with the same
			 * BSSID (if it exists). Subtract them and square that result.
			 */
			for (Entry<AccessPoint, Integer> m : mm.getLevels().entrySet()) {

				// Get an access point from the fingerprint with a same BSSID
				AccessPointPowerLevels levels = fp.getApLevels(m.getKey()
						.getBSSID());

				// Check if we actually found such an access point
				if (levels != null) {

					double val = (m.getValue() - levels.getAverage());

					d += Math.pow(val, 2);

					nrOfMatchedBssids++;
				}

			}

			/**
			 * Return the euclidian distance by taking the square root of d. We
			 * also return the number of matched access points and the
			 * figerprint to which the distance was calculated in a
			 * DistanceResult object.
			 */
			return new DistanceResult(Math.sqrt(d), nrOfMatchedBssids, fp);
		}

	}

}
