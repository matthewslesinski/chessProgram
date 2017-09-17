package stringUtilities;

import gamePlaying.Game;

/**
 * Houses the logic for turning a {@code Game} into a .pgn formatted string
 * @author matthewslesinski
 *
 */
public class PGNWriter {

	/**
	 * Writes a {@code Game} to a pgn format, and returns that string directly
	 * @param game The {@code Game} to turn into a pgn
	 * @return The string
	 */
	public static String write(Game game) {
		StringBuilder builder = new StringBuilder();
		//TODO
		return builder.toString();
	}
	
	/**
	 * Writes the pgn representation of a {@code Game} to a file. The filename/location will be default values
	 * @param game The {@code Game} to write to file
	 */
	public static void writeToFile(Game game) {
		// TODO
		return;
	}
}
