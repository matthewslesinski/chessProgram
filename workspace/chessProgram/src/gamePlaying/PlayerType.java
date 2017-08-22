package gamePlaying;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Describes the various types of players there can be.
 * @author matthewslesinski
 *
 */
public enum PlayerType {

	HUMAN(Human::new, "human"),
	COMPUTER(Computer::new, "computer");
	
	private static final Map<String, PlayerType> PLAYER_TYPE_MAPPER = new HashMap<>();
	static {
		for (PlayerType type : values()) {
			for (String identifier : type.identifiers) {
				PLAYER_TYPE_MAPPER.put(identifier, type);
			}
		}
	}
	
	/** The method used to create a new {@code Player} of this type */
	private final Function<String, Player> constructor;
	
	/** The set of strings that can be used to identify this particular type */
	private final String[] identifiers;
	
	private PlayerType(Function<String, Player> constructor, String... identifiers) {
		this.constructor = constructor;
		this.identifiers = identifiers;
	}
	
	/**
	 * Creates a new player of the given type
	 * @param name The name for the new player
	 * @return The {@code Player}
	 */
	public Player createNew(String name) {
		return constructor.apply(name);
	}
	
	/**
	 * Retrieves the player type specified by a string
	 * @param identifier The identifier for a type
	 * @return The type, defaulting to human
	 */
	public static PlayerType getByIdentifier(String identifier) {
		return PLAYER_TYPE_MAPPER.getOrDefault(identifier.trim().toLowerCase(), HUMAN);
	}
	

}
