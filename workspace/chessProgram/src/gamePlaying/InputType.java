package gamePlaying;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static support.UtilityFunctions.*;

/**
 * Organizes the various types of actions a player can make into a central paradigm. 
 * @author matthewslesinski
 *
 */
public enum InputType {

	// The only way to signal that the user is making a move is to just provide arguments (as in moves). Therefore, MOVE is the only
	// input type without identifier strings
	MOVE(UserActions::makeMove, 1, 1),
	POKE(UserActions::poke, 0, 0, "poke"),
	PRINT_MOVES(UserActions::printMoves, 0, 1, "moves"),
	PRINT_BOARD(UserActions::printBoard, 0, 0, "board"),
	GET_FEN(UserActions::getFEN, 0, 0, "fen"),
	EXPORT_PGN(UserActions::exportPGN, 0, 0, "export", "pgn"),
	UNDO(UserActions::undo, 1, 1, "undo"),
	REDO(UserActions::redo, 0, 0, "redo"),
	QUIT(UserActions::exit, 0, 0, "quit");
	
	/** The strings that can be used to signal this input type */
	private final String[] expectedInputs;
	
	/** The action to perform for input of this type */
	private final BiFunction<String[], Game, String> action;
	
	/** The minimum number of string arguments expected for this input type */
	private final int minArgs;
	
	/** The maximum number of string arguments expected for this input type */
	private final int maxArgs;
	
	/**
	 * The regex that can be used to extract the arguments from the string. It ignores the first word if it has a colon, but otherwise
	 * extracts each space-separated word. Furthermore, anything inside of quotes gets matched intact.
	 */
	private static final Pattern ARGUMENT_EXTRACTOR =
			Pattern.compile("(?<=^|\\s)[^:\\s]\\S*(?=\\s|$)|(?<=\")[^\"]*(?=\")(?=[^\"]*(\"[^\"]*\")*[^\"]*$)");
	
	/** A {@code Map} to hold the mappings from string identifiers to associated input types */
	private static final Map<String, InputType> INTERNAL_MAPPING = new HashMap<>();
	
	// map each identification string to the input type associated with it
	static {
		for (InputType type : values()) {
			for (String input : type.expectedInputs) {
				INTERNAL_MAPPING.put(input, type);
			}
		}
	}
	
	/**
	 * Creates the type of input action
	 * @param action The function to perform when given input of this type
	 * @param minArgs The minimum number of arguments that the function expects
	 * @param maxArgs The maximum number of arguments that the function expects
	 * @param expectedInputs The strings that can be used, with ":" as a prefix, to indicate an input of this type
	 */
	private InputType(BiFunction<String[], Game, String> action, int minArgs, int maxArgs, String... expectedInputs) {
		this.expectedInputs = expectedInputs;
		this.action = action;
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
	}
	
	/**
	 * Returns the type of input indicated by the given input
	 * @param input The user inputted string
	 * @return The type that corresponds with the input
	 */
	public static InputType getInputType(String input) {
		if (input == null) {
			return QUIT;
		}
		input = input.trim();
		if (input.startsWith(":")) {
			return INTERNAL_MAPPING.get(input.split("\\s+")[0].substring(1).toLowerCase());
		}
		return MOVE;
	}
	
	/**
	 * Validates the user input to make sure there's not a glaring error and the returns the action to perform for this type of input,
	 * with the arguments already applied.
	 * @param input The user inputted string
	 * @return The action to perform on a game
	 */
	public Function<Game, String> getAction(String input) {
		if (!input.matches("^[^\"]*(\"[^\"]*\")*[^\"]*$")) {
			return (game -> "There were an odd number of quotes (\") in the input");
		}
		String[] args = getArgsFromInput(input);
		if (args.length < minArgs) {
			return (game -> String.format("More arguments were expected for this action type. At least %d arguments must be provided", minArgs));
		}
		if (args.length > maxArgs) {
			return (game -> String.format("Less arguments were expected for this action type. At most %d arguments must be provided", maxArgs));
		}
		return bind(action, args);
	}
	
	/**
	 * Extracts the user's supplied arguments from their input
	 * @param input The user input
	 * @return The array of strings containing their arguments
	 */
	private static String[] getArgsFromInput(String input) {
		Matcher matcher = ARGUMENT_EXTRACTOR.matcher(input);
		List<String> args = new LinkedList<>();
		while(matcher.find()) {
			args.add(matcher.group());
		}
		return args.toArray(new String[args.size()]);
	}
	
	
}
