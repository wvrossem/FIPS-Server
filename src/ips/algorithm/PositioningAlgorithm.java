package ips.algorithm;

import ips.data.entities.wlan.WLANMeasurement;

/**
 * An interface for the positioning algorithms.
 * 
 * Part of the strategy pattern that is being used to have interchangeable
 * algorithms.
 * 
 * @author Wouter Van Rossem
 * 
 */
public interface PositioningAlgorithm {

	/**
	 * Calculates the positioning using a measurement that was collected by the
	 * client.
	 * 
	 * @param measurements
	 *            Measurement from the client that will be used to calculate its
	 *            position
	 * @return
	 */
	public PositioningResult calculatePosition(WLANMeasurement measurements);

}
