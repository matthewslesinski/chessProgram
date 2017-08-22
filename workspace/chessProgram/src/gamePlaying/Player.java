package gamePlaying;

import representation.Board;

/**
 * Represents a player of a game. Players obviously have a name, but then on top of that they can give some form
 * of input at a given time to do something, whether it's make a move or something else.
 * @author matthewslesinski
 *
 */
public abstract class Player {

	private final String name;
	
	protected Player(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the next input this player wants to make, when faced with a given board situation
	 * @param currentPosition The current board
	 * @return The user's input
	 */
	public abstract UserInput getNextInput(Board currentPosition);
	
	/**
	 * Gets the default input the player always gives, such as the board in string form after they make a move
	 * @return The extra input to add on
	 */
	public abstract UserInput getDefaultAddonInput();

	/**
	 * Gets a player's response to a given board. This combines their next action as well as what they always add on
	 * @param currentPosition The current position
	 * @return The next chained set of inputs the player gives
	 */
	public UserInput getFullResponse(Board currentPosition) {
		return getNextInput(currentPosition).chain(getDefaultAddonInput());
	}
	
	/**
	 * Retrieves the name of this player
	 * @return The name
	 */
	public String getName() {
		return name;
	}
}
