package dataStructures;

import java.util.List;

import boardFeatures.Square;
import convenienceDataStructures.UnmodifiableWrappedCollection;

public interface EvenlySpacedCircle extends UnmodifiableWrappedCollection<Square>{

	/**
	 * Determines the nearest square(s) in the circle's surface to the remote square
	 * @param remote An external square
	 * @return The nearest square(s)
	 */
	List<Square> getNearestSquares(Square remote);
}
