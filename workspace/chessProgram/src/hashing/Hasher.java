package hashing;

import moves.Move;
import representation.Board;
import static support.Constructors.*;

/**
 * Implementations of this class provide functionality for hashing a board into a long.
 * Since only one hashing object is needed, but we also want to allow for inheritance and
 * hiding the names of the implementation class, this class keeps track of a static reference
 * to that lone hashing object that can be accessed globally
 * @author matthewslesinski
 */
public abstract class Hasher {

	/** A globally accessible instance of a {@code Hasher} */
	private static Hasher GLOBAL_HASHER = null;
	
	protected Hasher() {
		GLOBAL_HASHER = this;
	}
	
	/**
	 * Retrieves the {@code Hasher} object kept by the program to hash a board into a long
	 * @return The {@code Hasher}
	 */
	public static Hasher getGlobalHasher() {
		if (GLOBAL_HASHER == null) {
			return HASHER_CONSTRUCTOR.get();
		}
		return GLOBAL_HASHER;
	}
	
	/**
	 * Translates a board directly into a long
	 * @param board The board to hash
	 * @return The hash for the board
	 */
	public abstract long getHash(Board board);
	
	/**
	 * Relies on the knowledge of a previously calculated hash for the preceding board to calculate the hash for the
	 * board that results from making a move on that preceding board. This hash should end up being the same value
	 * as calling {@code getHash} on the board that results from making the given move on the given board
	 * @param previous The previous board
	 * @param transition The move to make to get to the position that this method provides the hash for
	 * @return The hash for the resulting board
	 */
	public abstract long getNextHash(Board previous, Move transition);
	
}
