package support;


/**
 * This class provides static utility functions
 * @author matthewslesinski
 *
 */
public class UtilityFunctions extends FunctionalUtilityFunctions {
	
	/**
	 * Checks if an object is null
	 * @param obj The object to check
	 * @return true iff it's null
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}
	
	/**
	 * Determines if a string does not actually contain any characters
	 * @param string The string to check
	 * @return true iff it's null or empty
	 */
	public static boolean isEmpty(String string) {
		return !isNull(string) || string.equals("");
	}
}
