package ips.algorithm.bayes;

import ips.algorithm.Context;
import ips.algorithm.PositioningAlgorithm;
import ips.algorithm.PositioningResult;
import ips.algorithm.bayes.data.BayesFingerprint;
import ips.data.DataManager;
import ips.data.entities.wlan.AccessPoint;
import ips.data.entities.wlan.WLANMeasurement;
import ips.data.entities.wlan.WLANFingerprint;
import ips.other.BayesQuickSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * The Bayes positioning algorithm.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class BayesPositioning implements PositioningAlgorithm {

	Context context;
	List<BayesFingerprint> fps;

	public Entry<BayesFingerprint, Integer>[] result;

	public BayesPositioning(Context context) {

		this.context = context;

	}

	/**
	 * 
	 */
	@Override
	public PositioningResult calculatePosition(WLANMeasurement measurements) {

		DataManager dm = context.getDataManager();

		List<String> bssids = new ArrayList<String>();

		// Sets the list of bssids for which we will need fingerprints that
		// contain any of these bssids
		for (AccessPoint ap : measurements.getLevels().keySet()) {

			bssids.add(ap.getBSSID());
		}

		// Get the fingerprints from the datastore
		List<WLANFingerprint> fps = dm.getTrainingDataWithBSSIDs(bssids);

		List<BayesFingerprint> bayesFps = new ArrayList<BayesFingerprint>();

		// Create new bayes fingerprints from the retrieved fps
		for (WLANFingerprint fp : fps) {

			bayesFps.add(new BayesFingerprint(fp));
		}

		BayesFingerprint bayesFp = null;
		int lowestSum = 0;

		Map<BayesFingerprint, Integer> probabilities = new HashMap<BayesFingerprint, Integer>();

		// Gets the propabilities from the bayes fingerprints (interval sum).
		// Lowest sum is saved.
		for (BayesFingerprint fp : bayesFps) {

			int sum = fp.getIntervalSum(measurements);

			probabilities.put(fp, sum);

			if ((bayesFp == null) || (sum > lowestSum)) {

				bayesFp = fp;
				lowestSum = sum;
			}
		}

		BayesQuickSort quickSort = new BayesQuickSort();

		result = quickSort.sort(probabilities);

		PositioningResult result = new PositioningResult(bayesFp.getPosition()
				.getX(), bayesFp.getPosition().getY());

		return result;
	}

}