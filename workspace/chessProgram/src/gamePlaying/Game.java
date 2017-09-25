package gamePlaying;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import representation.Board;
import representation.BoardBuilder;
import stringUtilities.OptionalPGNTag;
import stringUtilities.PGNTag;
import stringUtilities.PGNWriter;
import stringUtilities.RequiredPGNTag;
import static support.Constants.*;
import static support.Constructors.*;

public abstract class Game {

	/** Holds the current game being played, so that it is always accessible */
	private static Game CURRENT_GAME;
	
	private final Map<PGNTag, String> gameDetails = new HashMap<>();
	
	/** The {@code Player} representation of the first player */
	private final Player player1;
	
	/** The {@code Player} representation of the second player */
	private final Player player2;
	
	/** Keeps track of which player has which color */
	private final Map<Color, Player> colorMapping = new EnumMap<>(Color.class);
	
	
	/** Builds the {@code Game} using constants passed as System properties */
	public Game() {
		this(PLAYER_1_TYPE, PLAYER_1_NAME, PLAYER_2_TYPE, PLAYER_2_NAME, BOARD_BUILDER_CONSTRUCTOR);
	}
	
	public Game(PlayerType player1Type, String name1, PlayerType player2Type, String name2, Function<String, BoardBuilder<? extends Board>> constructor) {
		this(player1Type, name1, player2Type, name2, constructor, STANDARD_START_POSITION);
	}
	
	public Game(PlayerType player1Type, String name1, PlayerType player2Type, String name2, Function<String, BoardBuilder<? extends Board>> constructor, String startPosition) {
		this.player1 = player1Type.createNew(name1);
		this.player2 = player2Type.createNew(name2);
		colorMapping.put(Color.WHITE, player1);
		colorMapping.put(Color.BLACK, player2);
		initBoardStore(constructor.apply(startPosition).build());
		CURRENT_GAME = this;
	}
	
	/**
	 * Initializes the data structure that keeps track of the game's positions
	 * @param initialBoard The first position in the game
	 */
	protected abstract void initBoardStore(Board initialBoard);
	
	/**
	 * Retrieves the current position
	 * @return The {@code Board}
	 */
	public abstract Board getCurrentPosition();
	
	/**
	 * Retrieves the final position played in the game
	 * @return The {@code Board}
	 */
	public abstract Board getLastPosition();
	
	/**
	 * Pushes a position onto the game stack
	 * @param board The {@code Board} with the position to push
	 */
	public abstract void addPosition(Board board);
	
	/**
	 * Adds a variation for the game proceeding from the current position
	 * @param board The board resulting from the first move of the variation
	 */
	public abstract void addVariation(Board board);
	
	/**
	 * pops the last 'plies' number of positions from this game
	 * @param plies The number of plies to go back
	 */
	public abstract void undoPlies(int plies);
	
	/**
	 * Gets the number of plies played in this game
	 * @return The number
	 */
	public abstract int getPlyNumber();
	
	/**
	 * Retrieves the {@code Player} object representing the player whose move it is
	 * @return The {@code Player}
	 */
	public Player getCurrentPlayer() {
		return colorMapping.get(getCurrentPosition().whoseMove());
	}
	
	/**
	 * Returns the {@code Player} of a particular color
	 * @param color The color to get
	 * @return The {@code Player} representing the player using that color of pieces
	 */
	public Player getPlayerByColor(Color color) {
		return colorMapping.get(color);
	}
	
	/**
	 * Retrieves the {@code Player} object representing the player whose move it is not
	 * @return The {@code Player}
	 */
	public Player getIdlePlayer() {
		return colorMapping.get(getCurrentPosition().whoseMove().getOtherColor());
	}
	
	/**
	 * Performs the action of waiting for a player to make their move or perform some other action, and then performs that action and prints the responses
	 */
	public void takeTurn() {
		Board currentPosition = getCurrentPosition();
		Player player = getCurrentPlayer();
		UserInput nextAction = player.getFullResponse(currentPosition);
		Collection<String> responses = nextAction.performAction(this);
		printResponses(responses);
	}
	
	/**
	 * Prints the responses to the action(s) performed.
	 * @param responses The responses to print sequentially
	 */
	private void printResponses(Collection<String> responses) {
		// TODO send this to the Players instead of System.out
		responses.stream().filter(string -> string != null).forEach(System.out::println);
	}
	
	/**
	 * Gets the details that are present for this game
	 * @param detailType The tag for the detail
	 * @param defaultValue The default value to supply
	 * @return The tag's value for this game
	 */
	public String getDetails(PGNTag detailType, String defaultValue) {
		return gameDetails.getOrDefault(detailType, defaultValue);
	}
	
	/**
	 * Associates a detail with this game
	 * @param name The tag name for the detail
	 * @param value The value to associate
	 */
	public void addDetail(String name, String value) {
		PGNTag tag = RequiredPGNTag.getByName(name);
		if (tag == null) {
			tag = new OptionalPGNTag(name);
		}
		gameDetails.put(tag, value);
	}
	
	@Override
	public String toString() {
		return PGNWriter.write(this);
	}
	
	/**
	 * Repeatedly takes turns for eternity. The only way out is closing the program or a user quitting
	 */
	public void play() {
		printResponses(getCurrentPlayer().getDefaultAddonInput().performAction(this));
		for (;;) {
			takeTurn();
		}
	}
	
	/**
	 * Retrieves the current game being played. This is static so it can be used anywhere that wants access to the game
	 * @return The {@code Game}
	 */
	public static Game getCurrentGame() {
		return CURRENT_GAME;
	}
	
	/**
	 * Sets up a game and plays it
	 */
	public static void startPlaying() {
		Game game = GAME_CONSTRUCTOR.get();
		game.play();
	}
	
	/**
	 * Enters the program
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		startPlaying();
	}
}
