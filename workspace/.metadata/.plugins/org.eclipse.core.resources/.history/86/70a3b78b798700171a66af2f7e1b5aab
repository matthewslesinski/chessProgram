package dataStructures;

import java.util.Collection;

import boardFeatures.Direction;
import boardFeatures.Square;
import gamePlaying.Color;

/**
 * This is an extension of {@code SquareSet}s that alters the way that directions are calculated between {@code Square}s.
 * The goal is to treat every {@code Square} combination the same, but en passants differently. En passants moves in a sense
 * have two destination squares, and so it is more convenient for calculations to represent the move with the end square
 * being the one with the captured piece. However, the actual direction of the movement is along the diagonal. So for en
 * passants, direction calculation is done by changing the destination to be the en passant destination square, instead
 * of the expected capture square.
 * @author matthewslesinski
 *
 */
public class PawnMoveSet extends SquareSet {
	
	
	/**
	 * Creates a {@code SquareSet} with everything the same except the slightly varied {@code Direction} calculating method
	 * @param elementsWithoutCenter The moves that the pawn can move to
	 * @param center The start {@code Square} for the pawn moves
	 * @param colorToMove The color of the moving pawn
	 */
	public PawnMoveSet(Collection<Square> elementsWithoutCenter, Square center, Color colorToMove) {
		super(elementsWithoutCenter, center, square -> getDirectionFromCenter(center, square, colorToMove));
	}
	
	/**
	 * This returns the direction from the center to the square, unless it's an en passant, in which case instead it returns
	 * the direction to the destination square
	 * @param center The start square
	 * @param square The destination
	 * @param colorToMove The color of the moving piece
	 * @return The direction
	 */
	private static Direction getDirectionFromCenter(Square center, Square square, Color colorToMove) {
		Direction trueDirection = center.getDirectionToSquare(square);
		// Only en passant moves will have a rank delta of 0
		if (trueDirection.getRankDelta() == 0) {
			// Change the direction to be to the en passant destination
			return Direction.getByDeltas(trueDirection.getFileDelta(), colorToMove.getPawnPushDirection().getRankDelta());
		}
		return trueDirection;
	}
	
}
