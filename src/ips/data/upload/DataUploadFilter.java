package ips.data.upload;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * A filter that can be used to remove values when uploading data.
 * 
 * @author Wouter Van Rossem
 * 
 */
@Root
public class DataUploadFilter {

	@Element
	FilterType type;

	@Element
	int filterValue;

	public DataUploadFilter(FilterType type, int filterValue) {
		super();
		this.type = type;
		this.filterValue = filterValue;
	}

	public DataUploadFilter() {
		super();
	}

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	public int getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(int filterValue) {
		this.filterValue = filterValue;
	}

	/**
	 * Checks if the value needs to be filtered.
	 * 
	 * @param d
	 *            To value to check
	 * @return A boolean that indicating if the value should be filtered
	 */
	public boolean checkFilter(double d) {

		switch (type) {
		case GREATER_THEN:
			return d > filterValue;
		case GREATER_THEN_EQUAL:
			return d >= filterValue;
		case LESS_THEN:
			return d < filterValue;
		case LESS_THEN_EQUAL:
			return d <= filterValue;
		case EQUAL:
			return d == filterValue;
		case NOT_EQUAL:
			return d != filterValue;
		default:
			return false;
		}
	}
}
