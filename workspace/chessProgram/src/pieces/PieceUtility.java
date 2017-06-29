package pieces;

import java.util.Set;

import boardFeatures.Square;
import moves.Move;
import representation.Board;

/**
 * The classes that extend this abstract class hold the utility method(s) for calculating
 * the legal moves for a type of piece on a square in a position.
 * @author matthewslesinski
 *
 */
public abstract class PieceUtility {
	
	/**
	 * The type of piece this is
	 */
	protected final PieceType pieceType;
	
	/**
	 * Constructor for pieces in general. Establishes what piece this is for
	 * @param color
	 */
	public PieceUtility(PieceType type) {
		this.pieceType = type;
	}
	
	
	/**
	 * Gets the type of piece this is
	 * @return The {@code PieceType} describing this piece
	 */
	public PieceType getType() {
		return this.pieceType;
	}
	
	
	/**
	 * Gets the {@code Set} of moves for this piece
	 * @return The {@code Set} of {@code Move} at a {@code Square} on the {@code Board}
	 */
	public abstract Set<Move> getLegalMoves(Square square, Board board);
	
}