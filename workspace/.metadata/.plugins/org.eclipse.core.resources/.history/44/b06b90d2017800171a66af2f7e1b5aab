package dataStructures;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import boardFeatures.Square;
import pieces.Piece;
import support.UtilityFunctions;

/**
 * Contains a clumped set of squares, such as the squares around a king. This class can also build an iterator over the squares in it that
 * are attacked by a particular piece.
 * @author matthewslesinski
 *
 */
public class KingMoveSet extends SquareSet {
	
	/**
	 * This maps each possible combination of a piece on a square to a function that takes a square and a boolean function and returns the next
	 * square to consider.
	 */
	private final Map<Piece, Map<Square, BiFunction<Predicate<Square>, Square, Square>>> potentialThreats = new EnumMap<>(Piece.class);
	
	/**
	 * Constructs this square by calculating all of the necessary functions and putting all the squares, including the center, into the set of squares
	 * @param elementsWithoutCenter All the squares besides the center
	 * @param center The center square
	 */
	public KingMoveSet(Collection<Square> elementsWithoutCenter, Square center) {
		super(elementsWithoutCenter, center);
		Set<Square> elements = EnumSet.copyOf(UtilityFunctions.concat(elementsWithoutCenter, Collections.singleton(center)));
		for (Piece piece : Piece.realPieces()) {
			Map<Square, BiFunction<Predicate<Square>, Square, Square>> innerMap = new EnumMap<>(Square.class);
			potentialThreats.put(piece, innerMap);
			for (Square remoteSquare : Square.values()) {
				innerMap.put(remoteSquare, remoteSquare.pieceThreats(piece, elements));
			}
		}
	}
	
	/**
	 * Builds an iterator for the squares in this set that are attacked by a piece
	 * @param perspective The square containing the attacking piece
	 * @param occupant The attacker
	 * @param shouldTraverse A predicate detailing if squares should be considered (for instance, if they're not blocked)
	 * @return The iterator
	 */
	public Iterator<Square> getRelevantSquares(Square perspective, Piece occupant, Predicate<Square> shouldTraverse) {
		Function<Square, Square> getNextFunction = UtilityFunctions.bind(potentialThreats.get(occupant).get(perspective), shouldTraverse);
		Iterator<Square> iterator = new Iterator<Square>() {
			Square curr = getNextFunction.apply(null);
			@Override
			public boolean hasNext() {
				return curr != null;
			}

			@Override
			public Square next() {
				Square toReturn = curr;
				curr = getNextFunction.apply(curr);
				return toReturn;
			}
			
		};
		return iterator;
	}

}
