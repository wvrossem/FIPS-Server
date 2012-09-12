package ips.server;

import ips.data.entities.wlan.AccessPoint;
import ips.data.entities.wlan.AccessPointPowerLevels;
import ips.data.entities.wlan.WLANFingerprint;
import ips.data.upload.DataUploadFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * A data upload request consists of the fingerprints that will need to be
 * uploaded and the filters that will be used before uploading to filter some of
 * the access points power levels that were captured in a fingerprint.
 * 
 * @author Wouter Van Rossem
 * 
 */
@Root
public class DataUploadRequest {

	@ElementList
	List<WLANFingerprint> fps;

	@ElementList
	List<DataUploadFilter> filters;

	@Attribute
	boolean clearData;

	public DataUploadRequest() {
		super();
	}

	public DataUploadRequest(List<WLANFingerprint> fps,
			List<DataUploadFilter> filters, boolean clearData) {
		super();
		this.fps = fps;
		this.filters = filters;
		this.clearData = clearData;
	}

	public boolean doClearData() {
		return clearData;
	}

	public void doClearData(boolean clearData) {
		this.clearData = clearData;
	}

	public List<WLANFingerprint> getFps() {
		return fps;
	}

	public void setFps(List<WLANFingerprint> fps) {
		this.fps = fps;
	}

	public List<DataUploadFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<DataUploadFilter> filters) {
		this.filters = filters;
	}

	/**
	 * Checks if the filters resolve for each access point power level of each
	 * fingerprint and removes that ap from the fingerprint if it did.
	 */
	public void resolveFilters() {

		for (WLANFingerprint fp : fps) {

			Map<AccessPoint, AccessPointPowerLevels> levels = fp
					.getMeasurement().getApMeasurements();

			// Keep track of the access points we want to remove so
			// we can do it afterwards
			List<AccessPoint> apsToRemove = new ArrayList<AccessPoint>();

			// Check the filters for each access point power level
			// in the fingerprint
			for (Entry<AccessPoint, AccessPointPowerLevels> entry : levels
					.entrySet()) {

				// Check if the filter resolved
				if (checkFilters(entry.getValue().getAverage())) {
					// If it does, add it to the list so that it will be removed
					apsToRemove.add(entry.getKey());
				}
			}

			// Now we remove the access points for which the filter resolved
			for (AccessPoint ap : apsToRemove) {

				levels.remove(ap);
			}

			// And save the list of levels with the removed access points
			fp.getMeasurement().setApMeasurements(levels);
		}
	}

	/**
	 * Checks if the value resolves for any of the filters
	 * 
	 * @param d
	 *            The value to check on the filter
	 * @return A boolean indicating if it matched a filter
	 */
	private boolean checkFilters(double d) {

		// Loop over all the filters
		for (DataUploadFilter filter : filters) {
			// And check if it matches
			if (filter.checkFilter(d)) {

				return true;
			}
		}

		return false;
	}
}
