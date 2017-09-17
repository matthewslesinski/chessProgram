package stringUtilities;

import gamePlaying.Game;

/**
 * Represents a type of tag that can be used as a descriptor for a game in pgn format.
 * @author matthewslesinski
 *
 */
public interface PGNTag {

	/** Returns the name of this tag */
	public String getName();
	
	/**
	 * Gets the value associated with this tag in a particular {@code Game}
	 * @param game The {@code Game} to retrieve the value from
	 * @return The value associated with this tag
	 */
	public String retrieveFromGame(Game game);
}
