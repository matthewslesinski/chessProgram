package support;

/**
 * Holds a bunch of constants
 * @author matthewslesinski
 *
 */
public class Constants {

	/** Whether or not the instance of this program is running from eclipse */
	public static final boolean RUNNING_FROM_ECLIPSE = Boolean.getBoolean("eclipse");
	
	/** The newline character in string form */
	public static final String NEWLINE = "\n";
	
	/** The escape character (ESC, not \) in character form */
	public static final String ESCAPE_CHARACTER = Character.toString((char) 27);
	
	/**
	 * The ansi string to color code the background for dark squares. Ironically, this code colors the background
	 * what is supposed to be white, but it looks darker.
	 */
	public static final String ANSI_DARK_SQUARE = "[47m";
	
	/**
	 * The ansi code to reset the current set of attributes. This can be used to get rid of any previously set
	 * color codes.
	 */
	public static final String ANSI_RESET_ATTRIBUTES = "[0m";
	
	/** A space */
	public static final String SINGLE_SPACE = " ";
	
	/** Two spaces */
	public static final String DOUBLE_SPACE = "  ";

	/** Three spaces */
	public static final String TRIPLE_SPACE = "   ";
}