package moveCalculationStructures;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import boardFeatures.Square;
import boardFeatures.StaticInitializer;
import pieces.Piece;
import static support.UtilityFunctions.*;

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
	private final Map<Piece, Map<Square, BiFunction<Function<Square, Piece>, Square, Square>>> potentialThreats = new EnumMap<>(Piece.class);
		
	/**
	 * Constructs this square by calculating all of the necessary functions and putting all the squares, including the center, into the set of squares
	 * @param elementsWithoutCenter All the squares besides the center
	 * @param center The center square
	 */
	public KingMoveSet(Collection<Square> elementsWithoutCenter, Square center) {
		super(elementsWithoutCenter, center);
		// This must be done later to avoid circular dependencies
		StaticInitializer.addAction(this::calculateThreats);
	}
	
	/**
	 * Calculates the {@code Square}s in this {@code KingMoveSet} that are threatened by any {@code Piece} at any {@code Square}.
	 */
	private void calculateThreats() {
		Set<Square> elements = EnumSet.copyOf(concat(this.squares, Collections.singleton(center)));
		for (Piece piece : Piece.realPieces()) {
			Map<Square, BiFunction<Function<Square, Piece>, Square, Square>> innerMap = new EnumMap<>(Square.class);
			potentialThreats.put(piece, innerMap);
			for (Square remoteSquare : Square.values()) {
				innerMap.put(remoteSquare, remoteSquare.pieceThreats(piece, elements));
			}
		}
	}
	
	/**
	 * Builds an {@code Iterable} for the squares in this set that are attacked by a piece
	 * @param perspective The square containing the attacking piece
	 * @param occupants A function describing which piece is on a square
	 * @return The iterator
	 */
	public Iterable<Square> getAttackedSquares(Square perspective, Function<Square, Piece> occupants) {
		
		Piece occupant = occupants.apply(perspective);
		Function<Square, Square> getNextFunction = bind(potentialThreats.get(occupant).get(perspective), occupants);
		return new Iterable<>() {

			@Override
			public Iterator<Square> iterator() {
				Iterator<Square> iterator = new Iterator<>() {
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
			
		};
	}
}
