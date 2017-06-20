package support;

public class BadArgumentException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Object argument;
	private Class<?> type;
	
	/**
	 * Creates an exception when an improper argument was used
	 * @param argument The actual argument
	 * @param type The class of the argument
	 * @param message The message related to this exception
	 */
	public BadArgumentException(Object argument, Class<?> type, String message) {
		super(message);
		this.argument = argument;
		this.type = type;
	}
	
	/**
	 * Creates an exception when an improper argument was used
	 * @param argument The actual argument
	 * @param type The class of the argument
	 */
	public BadArgumentException(Object argument, Class<?> type) {
		this(argument, type, "The provided argument was not expected");
	}
	
	
	/**
	 * Gets the argument that was bad
	 * @return The {@code Object} that is the argument
	 */
	public Object getArgument() {
		return argument;
	}
	
	/**
	 * Returns the type of the bad argument
	 * @return The {@code Class} of the argument
	 */
	public Class<?> getType() {
		return type;
	}
	
	
}
