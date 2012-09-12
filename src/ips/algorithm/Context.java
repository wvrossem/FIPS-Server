package ips.algorithm;

import ips.algorithm.bayes.BayesPositioning;
import ips.algorithm.knn.NearestNeighborAlgorithm;
import ips.data.DataManager;
import ips.data.entities.wlan.WLANFingerprint;
import ips.data.entities.wlan.WLANMeasurement;

/**
 * 
 * The context class that is needed to implement the "Strategy pattern" for our
 * algorithms.
 * 
 * Delegates the positioning request to the positioning algorithm.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class Context {

	/**
	 * The positioning algorithm that is currently being used
	 */
	PositioningAlgorithm positioningAlgorithm;

	/**
	 * A datamanager that handles the communication with the datastore
	 */
	DataManager dataManager;

	/**
	 * Constructor which takes a {@link DataManager}.
	 * 
	 * @param dataManager
	 */
	public Context(DataManager dataManager) {

		this.dataManager = dataManager;
		this.positioningAlgorithm = new NearestNeighborAlgorithm(this);
	}

	/**
	 * Main method of the context class. Delegation of the positioning request
	 * to the configured algorithm.
	 * 
	 * @param measurements
	 *            The measurement from the client
	 * @return A positioning result
	 */
	public PositioningResult calculatePosition(WLANMeasurement measurements) {

		return positioningAlgorithm.calculatePosition(measurements);
	}

	/**
	 * Additional method that can take a fingerprint instead of a measurement.
	 * First converts the fingerprint to a measurement and then calls its own
	 * calculatePositioning method with it.
	 * 
	 * @param fp
	 *            A fingerprint
	 * @return A positioning result
	 */
	public PositioningResult calculatePosition(WLANFingerprint fp) {

		return this.calculatePosition(fp.getMeasurement());
	}

	/**
	 * 
	 * @return The positioning algorithm that is currently being used.
	 */
	public PositioningAlgorithm getPositioningAlgorithm() {

		return positioningAlgorithm;
	}

	/**
	 * Changes the positioning algorithm of the context.
	 * 
	 * @param positioningAlgorithm
	 *            The positioning algorithm to switch to
	 */
	public void setPositioningAlgorithm(
			PositioningAlgorithm positioningAlgorithm) {

		this.positioningAlgorithm = positioningAlgorithm;
	}

	/**
	 * 
	 * @return The data manager of the context.
	 */
	public DataManager getDataManager() {

		return dataManager;
	}

	/**
	 * 
	 * Sets the algorihtm that needs to be run to do the positioning.
	 * 
	 * @param algoType
	 *            The type of algorithm
	 */
	public void setPositioningAlgorithm(PositioningAlgorithmType algoType) {

		switch (algoType) {
		case NearestNeighbors:
			positioningAlgorithm = new NearestNeighborAlgorithm(this);
			break;
		case BayesPositioning:
			positioningAlgorithm = new BayesPositioning(this);
			break;
		}
	}
}
