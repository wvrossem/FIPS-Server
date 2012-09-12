package ips.algorithm.knn;

import ips.data.entities.Position;
import ips.data.entities.wlan.WLANFingerprint;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * A result from the nearest neighbors algorithm. Contains the distance found
 * to a fingerprint and the number of ssids that matched.
 * 
 * @author Wouter Van Rossem
 * 
 */
@Root
public class DistanceResult {

	@Element
	public double distance;

	@Element
	public int nrOfMatchedBssids;

	@Element(required = false)
	public Position pos;

	public DistanceResult() {
		super();
	}

	public DistanceResult(double d, int nrOfMatchedBssids, WLANFingerprint fp) {
		super();
		this.distance = d;
		this.nrOfMatchedBssids = nrOfMatchedBssids;
		// this.fingerprint = fp;
		this.pos = fp.getPosition();
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getNrOfMatchedBssids() {
		return nrOfMatchedBssids;
	}

	public void setNrOfMatchedBssids(int nrOfMatchedBssids) {
		this.nrOfMatchedBssids = nrOfMatchedBssids;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

}
