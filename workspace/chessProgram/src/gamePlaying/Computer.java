package gamePlaying;

import representation.Board;

/**
 * Holds the implementation to interface with a computer player
 * @author matthewslesinski
 *
 */
public class Computer extends Player {
	
	/** A default name for the computer */
	private static final String DEFAULT_NAME = "Computer";
	
	/** After each move by the other player, print the board */
	private static final UserInput DEFAULT_ADDON = new UserInput(InputType.PRINT_BOARD);
	
	public Computer() {
		this(DEFAULT_NAME);
	}
	
	public Computer(String name) {
		super(name);
	}
	
	@Override
	public UserInput getNextInput(Board currentPosition) {
		// TODO
		return null;
	}

	@Override
	public UserInput getDefaultAddonInput() {
		return DEFAULT_ADDON;
	}

}
