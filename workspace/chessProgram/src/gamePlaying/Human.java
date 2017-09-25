package gamePlaying;

import java.io.BufferedReader;
import java.io.IOException;

import representation.Board;
import static support.Constants.*;

/**
 * Holds the implementation to interface with a human player
 * @author matthewslesinski
 *
 */
public class Human extends Player {

	/** After each move by the other player, print the baord, and the legal moves too if specified by a VM argument */
	private static final UserInput DEFAULT_ADDON = SHOULD_PRINT_MOVES ?
			new UserInput(InputType.PRINT_BOARD, InputType.PRINT_MOVES) : new UserInput(InputType.PRINT_BOARD);
	
	public Human(String name) {
		super(name);
	}
	
	@Override
	public UserInput getNextInput(Board currentPosition) {
		BufferedReader r = INPUT_READER;
		String input;
		try {
			input = r.readLine();
		} catch (IOException e) {
			// TODO log this
			input = null;
		}
		return new UserInput(input);
	}

	@Override
	public UserInput getDefaultAddonInput() {
		return DEFAULT_ADDON;
	}

}
