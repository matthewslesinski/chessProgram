package stringUtilities;

import gamePlaying.Game;
import support.BadArgumentException;

/**
 * Represents a pgn tag that doesn't necessarily need to be provided. Therefore, this can be anything, and is essentially
 * a wrapper for a string that is augmented to support the {@code PGNTag} interface
 * @author matthewslesinski
 *
 */
public class OptionalPGNTag implements PGNTag {

	/** The name for the tag */
	private final String name;
	
	/** Ensures that the provided name isn't already the name of a required tag, and then instantiates the wrapped string */
	public OptionalPGNTag(String name) {
		if (RequiredPGNTag.getByName(name) != null) {
			throw new BadArgumentException(name, OptionalPGNTag.class, "The provided name is the same as a required tag");
		}
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String retrieveFromGame(Game game) {
		return game.getDetails(this, null);
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
