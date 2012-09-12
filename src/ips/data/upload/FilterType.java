package ips.data.upload;

/**
 * 
 * The different types of filters that can be used when uploading data.
 * 
 * @author Wouter Van Rossem
 * 
 */
public enum FilterType {

	/**
	 * The different filter types with their string types
	 */
	GREATER_THEN(">"), 
	GREATER_THEN_EQUAL(">="), 
	LESS_THEN("<"), 
	LESS_THEN_EQUAL("<="), 
	EQUAL("=="), 
	NOT_EQUAL("!=");

	private String type;

	/**
	 * Private constructor to create filter types that have a String value type.
	 * 
	 * @param type
	 *            Mathematical symbol representing the filter type
	 */
	private FilterType(String type) {
		this.type = type;
	}

	/**
	 * Create a FilterType from a string that represents the type
	 * 
	 * @param string
	 *            the string
	 * @return the prediction technique type
	 */
	public static FilterType fromString(String string) {

		if (string != null) {
			for (FilterType f : FilterType.values()) {
				if (string.equals(f.type)) {
					return f;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return The type of the filter as a string
	 */
	public String getStringType() {
		return type;
	}
}
