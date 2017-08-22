package dataStructures;

import java.util.List;

import boardFeatures.Square;
import convenienceDataStructures.UnmodifiableWrappedCollection;

/**
 * A circle of {@code Square}s that are evenly spaced, like the {@code Square}s a king or knight can move to. Since it is spaced
 * evenly around a center, some of its {@code Square}s are inevitably closer to some remote {@code Square} than the others. 
 * Therefore, an instance of this interface can find the nearest {@code Square} (or {@code Square}s if there's a tie) to that
 * remote {@code Square} 
 * @author matthewslesinski
 *
 */
public interface EvenlySpacedCircle extends UnmodifiableWrappedCollection<Square> {

	/**
	 * Determines the nearest square(s) in the circle's surface to the remote square
	 * @param remote An external square
	 * @return The nearest square(s)
	 */
	List<Square> getNearestSquares(Square remote);
}
