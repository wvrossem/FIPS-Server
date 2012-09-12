package ips.server;

import ips.algorithm.PositioningAlgorithmType;
import ips.data.entities.wlan.WLANFingerprint;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * A request to execute a position algorithm on a new measurement.
 * 
 * @author Wouter Van Rossem
 * 
 */
@Root
public class PositioningRequest {

	@Attribute
	public boolean fullResult;

	@Element(required = false)
	public WLANFingerprint fp;

	@Element(required = false)
	public PositioningAlgorithmType algorithm;

	public PositioningRequest(boolean fullResult, WLANFingerprint fp,
			PositioningAlgorithmType algorithm) {
		super();
		this.fullResult = fullResult;
		this.fp = fp;
		this.algorithm = algorithm;
	}

	public PositioningRequest() {
		super();
	}
}
