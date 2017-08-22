package moves;

// This interface was written and then I realized it wasn't necessary. I'm still keeping it up in case it becomes useful
@Deprecated
public interface Capture extends Move {

	@Override
	public default boolean isCapture() {
		return true;
	}
}
