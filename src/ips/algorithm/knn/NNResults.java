package ips.algorithm.knn;

import ips.algorithm.PositioningResult;
import ips.data.entities.wlan.WLANFingerprint;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * A result returned from the nearest neighbors algorithm.
 * 
 * @author Wouter Van Rossem
 * 
 */
@Root
public class NNResults extends PositioningResult {

	@Element(required = false)
	WLANFingerprint fp;

	@ElementList
	List<DistanceResult> results;

	public NNResults() {
		super();
	}

	public NNResults(List<DistanceResult> results) {
		super(results.get(0).pos);

		this.results = results;
	}

	public WLANFingerprint getFp() {
		return fp;
	}

	public void setFp(WLANFingerprint fp) {
		this.fp = fp;
	}

	public List<DistanceResult> getResults() {
		return results;
	}

	public void setResults(List<DistanceResult> results) {
		this.results = results;
	}
}
