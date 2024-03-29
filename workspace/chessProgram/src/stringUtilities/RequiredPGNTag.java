package stringUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import gamePlaying.Color;
import gamePlaying.Game;
import static support.Constants.*;

/**
 * An enum with details for the seven pgn tags that must be provided for each game
 * @author matthewslesinski
 *
 */
public enum RequiredPGNTag implements PGNTag {

	EVENT("Event", "morcanie/chessProgram"),
	SITE("Site", "localhost"),
	DATE("Date", new Date()),
	ROUND("Round"),
	WHITE("White", game -> game.getPlayerByColor(Color.WHITE).getName()),
	BLACK("Black", game -> game.getPlayerByColor(Color.BLACK).getName()),
	RESULT("Result", game -> game.getLastPosition().getState().toString()),
	;
			
	/** Whether a value for this tag must be added to a game */
	private final boolean isRequired;
	
	/** The name for this tag */
	private final String name;
	
	/** The way to retrieve a default value for this tag if it's not present in a game */
	private final Function<Game, String> detailsObtainer;
	
	/** Keeps track of the required tag enums for easy retrieval by name */
	private static final Map<String, RequiredPGNTag> NAME_MAP = new HashMap<>();
	static {
		for (RequiredPGNTag detail : values()) {
			NAME_MAP.put(detail.getName(), detail);
		}
	}
	
	/**
	 * Initializes a required tag, but does not associate a default value
	 * @param name The name for the tag
	 */
	private RequiredPGNTag(String name) {
		this.name = name;
		this.detailsObtainer = game -> game.getDetails(this, UNKNOWN_PGN_VALUE);
		this.isRequired = false;
	}
	
	/**
	 * Relates a tag with a default string to provide
	 * @param name The name for the tag
	 * @param defaultValue The default value to associate it with
	 */
	private RequiredPGNTag(String name, String defaultValue) {
		this.name = name;
		this.detailsObtainer = game -> game.getDetails(this, defaultValue);
		this.isRequired = true;
	}
	
	/**
	 * Defaults the date to year.month.day format
	 * @param name The name of the tag
	 * @param date The date value for a default value
	 */
	private RequiredPGNTag(String name, Date date) {
		this(name, new SimpleDateFormat("yyyy.MM.dd").format(date));
	}
	
	/**
	 * Relates a tag with a way to retrieve a default value for it
	 * @param name The name for the tag
	 * @param detailsObtainer The method for retrieving a default value
	 */
	private RequiredPGNTag(String name, Function<Game, String> detailsObtainer) {
		this.detailsObtainer = detailsObtainer;
		this.isRequired = true;
		this.name = name;
	}
	
	
	
	/**
	 * If a value for this tag must be added to a game
	 * @return The boolean saying if it must be added
	 */
	public boolean isRequired() {
		return isRequired;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String retrieveFromGame(Game game) {
		return detailsObtainer.apply(game);
	}
	
	/**
	 * Retrieves the enum instance for the tag with the given name, or null
	 * @param name The name to retrieve
	 * @return The enum representing that tag
	 */
	public static RequiredPGNTag getByName(String name) {
		return NAME_MAP.get(name);
	}
}

