package representation;

import boardFeatures.Square;
import pieces.Piece;

public interface Board {

	/**
	 * Retrieves the {@code Piece} currently at a {@code Square}
	 * @param square The {@code Square} to retrieve the {@code Piece} at
	 * @return The {@code Piece} at that {@code Square}
	 */
	public Piece getPieceAtSquare(Square square);
	
	
	/**
	 * Determines if the piece that is currently at a square is the given piece
	 * @param piece The {@code Piece} to look for
	 * @param square The {@code Square} to check
	 * @return If the {@code Piece}
	 */
	public default boolean isPieceAtSquare(Piece piece, Square square) {
		return piece.equals(getPieceAtSquare(square));
	}
	
	
}
