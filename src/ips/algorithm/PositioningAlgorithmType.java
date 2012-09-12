package ips.algorithm;

import ips.algorithm.bayes.BayesPositioning;
import ips.algorithm.knn.NNResults;
import ips.algorithm.knn.NearestNeighborAlgorithm;

/**
 * The Enum PositioningAlgorithmType.
 * 
 * Defines the different types of positionig algorithms that are available.
 * Each prediction technique type has:
 * 		- A name
 * 		- The actual class implementing the algorithm
 * 		- The names of the parameters used by the technique
 * 
 * @author Wouter Van Rossem
 */
public enum PositioningAlgorithmType {
	
	/** The Nearest Neighbors algorithm. */
	NearestNeighbors("Nearest Neighbors", NearestNeighborAlgorithm.class, 
			NNResults.class, new String [] {"k"}), 
	
	/** The Bayes Positioning algorithm. */
	BayesPositioning("Bayes Positioning", BayesPositioning.class);

	/** The different fields of a positioning algorithm. */
	private String                      algorihtmName;
	private Class<PositioningAlgorithm> positioningAlgorithm;
	private Class<PositioningResult>    resultType;
	private String[]                    parameters;
	
	/**
	 * Instantiates a new prediction technique type
	 * without parameters.
	 *
	 * @param name the name of the technique
	 * @param predictionTechnique the class implementing the technique
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private PositioningAlgorithmType(String algorithmName, Class positioningAlgorithm) {
		
		this.algorihtmName        = algorithmName;
		this.parameters           = null;
		this.positioningAlgorithm = positioningAlgorithm;
		this.resultType           = PositioningResult.class;
	}
	
	/**
	 * Instantiates a new prediction technique type
	 * without parameters.
	 *
	 * @param name the name of the technique
	 * @param predictionTechnique the class implementing the technique
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private PositioningAlgorithmType(String algorithmName, Class positioningAlgorithm, 
			Class resultType) {
		
		this.algorihtmName        = algorithmName;
		this.parameters           = null;
		this.positioningAlgorithm = positioningAlgorithm;
		this.resultType           = resultType;
	}

	/**
	 * Instantiates a new prediction technique type
	 * with parameters.
	 *
	 * @param name the name of the technique
	 * @param predictionTechnique the class implementing the technique
	 * @param parameters the names of the parameters used by the technique
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private PositioningAlgorithmType(String algorithmName, Class positioningAlgorithm, 
			Class resultType, String[] parameters) {
		
		this.algorihtmName        = algorithmName;
		this.parameters           = parameters;
		this.positioningAlgorithm = positioningAlgorithm;
		this.resultType           = resultType;
	}

	/**
	 * Gets the class implementing the prediction technique.
	 *
	 * @return the prediction technique
	 */
	public Class<PositioningAlgorithm> getPredictionTechnique() {
		
		return positioningAlgorithm;
	}

	/**
	 * Gets the name of the positioning algorithm.
	 *
	 * @return the algorithm name
	 */
	public String getAlgorithmName() {
		
		return algorihtmName;
	}
	
	/**
	 * Gets the type of result returned by the algorithm
	 *
	 * @return the result type
	 */
	public Class<PositioningResult> getResultType() {
		
		return resultType;
	}
	
	/**
	 * Checks for parameters.
	 *
	 * @return true, if successful
	 */
	public boolean hasParameters() {
		
		return parameters != null;
	}
	
	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public String[] getParameters() {
		
		return parameters;
	}

	/**
	 * Create a PositioningALgorithmType from a string, i.e. an algorithm name.
	 *
	 * @param string the name as string
	 * @return the positioning algorithm type
	 */
	public static PositioningAlgorithmType fromString(String string) {
		
		if (string != null) {
			for (PositioningAlgorithmType t : PositioningAlgorithmType.values()) {
				if (string.equals(t.algorihtmName)) {
					return t;
				}
			}
		}
		return null;
	}

}
