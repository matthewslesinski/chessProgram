package boardFeatures;

/**
 * Used to control the initialization order of the various classes in this package, since there are various circular dependencies
 * @author matthewslesinski
 *
 */
public class StaticInitializer {

	/**
	 * Initializes values through this initialization process, to avoid circular dependencies
	 */
	static void initialize() {
		File.setContainedSquares();
		Rank.setContainedSquares();
		UpRightDiagonal.setContainedSquares();
		DownRightDiagonal.setContainedSquares();
		Square.postInitialization();
	}
}
