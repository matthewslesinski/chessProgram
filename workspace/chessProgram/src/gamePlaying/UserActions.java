package gamePlaying;

import java.io.IOException;

import moves.Move;
import representation.Board;
import stringUtilities.FENStringWriter;
import stringUtilities.MoveParser;
import support.Constants;

/**
 * A utility class used to define the methods used to perform the actions a player intends whenever they give some input. Ideally,
 * each public method in this class should be static, and of the form such that it takes an array of strings and a game and returns a string.
 * The returned string should be the response intended to send back to the players. The array of strings will contain the arguments supplied
 * to the action. Although a string array should always be passed to these actions, it should not necessarily always contain data. For instance, the
 * different input types that these actions are used for specify the range of number of arguments they expect, so these methods will never
 * be called with more or less arguments than expected. Some of the actions expect 0 arguments as their maximum. For instance, the {@code QUIT} input type
 * doesn't expect any arguments, so the corresponding method, {@code exit}, will only have an empty array passed to it. Furthermore, the arguments
 * are not guaranteed to be validated for a particular form. The game argument for each of these methods is the current game being played
 * and to perform the action within. 
 * @author matthewslesinski
 *
 */
public class UserActions {
	
	private static final String ILLEGAL_MOVE = "The attempted move is illegal. For a complete list of legal moves, type \":moves\".";
	
	/**
	 * Exits the game. This is the only way for a user to quit the program, other than by manually killing the process
	 * @param args The arguments to the action - none are expected
	 * @param game The game that is being played
	 * @return null, but it doesn't matter since we're exiting
	 */
	public static String exit(String[] args, Game game) {
		try {
			Constants.INPUT_READER.close();
		} catch(IOException e) {
			// TODO log this
		}
		System.exit(0);
		return null;
	}
	
	/**
	 * Undoes the last ply of the given game
	 * @param args The arguments to the action - none are expected
	 * @param game The game to redo the last move for
	 * @return no response is needed
	 */
	public static String redo(String[] args, Game game) {
		game.undoPlies(1);
		return null;
	}
	
	/**
	 * undoes a given number of plies.
	 * @param args The array, of which the first and only argument expected should be number of plies to reverse.
	 * That number should not be larger than the number of moves played
	 * @param game The game to reverse the moves in
	 * @return no response is needed
	 */
	public static String undo(String[] args, Game game) {
		String argument = args[0];
		if (!argument.matches("\\d+")) {
			return "Can only undo an integer number of plies";
		}
		int numPlies = Integer.parseInt(argument);
		if (numPlies >= game.getPlyNumber()) {
			return "Can't undo more moves than were made";
		}
		game.undoPlies(numPlies);
		return null;
	}
	
	public static String exportPGN(String[] args, Game game) {
		// TODO
		return null;
	}
	
	public static String getFEN(String[] args, Game game) {
		Board currentPosition = game.getCurrentPosition();
		int moveNumber = (game.getPlyNumber() + 1) / 2;
		FENStringWriter writer = new FENStringWriter(currentPosition, moveNumber);
		return writer.makeFEN();
	}
	
	/**
	 * Prints the list of legal moves for the given position, in general, or if an argument is supplied, shows on the board
	 * the available {@code Square}s the provided piece, whether specified by name (and so including all other pieces of that type
	 * are included), or by the {@code Square} it's standing on
	 * @param args The arguments for the action, which can be nothing, or they can be a piece type or a square
	 * @param game The game with the current position
	 * @return The string of the list of legal moves, or the board with the highlighted squares pieces can move to
	 */
	public static String printMoves(String[] args, Game game) {
		if (args.length == 0) {
			return "Legal Moves: " + game.getCurrentPosition().getLegalMoves().toString();
		}
		// TODO
		return null;
	}
	
	/**
	 * Prints the current position
	 * @param args The arguments for this action, which are expected to be nothing
	 * @param game The current game being played
	 * @return The string of the board
	 */
	public static String printBoard(String[] args, Game game) {
		return game.getCurrentPosition().toString();
	}
	
	public static String poke(String[] args, Game game) {
		// TODO
		return null;
	}
	
	/**
	 * Performs the action of making a move. It checks if the move is legal, and it is it, adds the resulting position to the game.
	 * If the game is over, it will be declared over, but the engine will remain running in case an undo is wanted
	 * @param args The arguments for the action. In this case, one, no more and no less, is always expected, which contains the actual move being intended
	 * @param game The current game being played
	 * @return The string response to send back to the players.
	 */
	public static String makeMove(String[] args, Game game) {
		String moveString = args[0];
		Board currentPosition = game.getCurrentPosition();
		Move nextMove = MoveParser.parseAlgebraicNotation(currentPosition, moveString);
		if (nextMove == null) {
			return ILLEGAL_MOVE;
		}
		Board nextPosition = currentPosition.performMove(nextMove);
		game.addPosition(nextPosition);
		switch (GameState.getByBoard(nextPosition)) {
		case STALEMATE:
			return "It's a draw!";
		case WHITE_WINS:
			return "Congratulations, " + game.getPlayerByColor(Color.WHITE).getName() + " has won";
		case BLACK_WINS:
			return "Congratulations, " + game.getPlayerByColor(Color.BLACK).getName() + " has won";
		default:
			return null;
		}
		
	}
	
}
