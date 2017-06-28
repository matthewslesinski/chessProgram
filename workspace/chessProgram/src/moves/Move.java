package moves;

import boardFeatures.Side;
import boardFeatures.Square;
import pieces.PieceType;

/**
 * Represents a move in the game. There are various default implementations in this interface.
 * They should be overridden where necessary by implementing classes
 * @author matthewslesinski
 *
 */
public interface Move {

	/**
	 * Returns the {@code PieceType} that gets moved
	 * @return the {@code PieceType} that moves
	 */
	public PieceType getMovingPiece();
	
	/**
	 * Gets the {@code Square} the {@code Piece} making this move starts from
	 * @return The {@code Square}
	 */
	public Square getStartSquare();
	
	/**
	 * Gets the {@code Square} the {@code Piece} making this move ends at
	 * @return The {@code Square}
	 */
	public Square getEndSquare();
	
	/**
	 * Does this piece involve capturing another piece?
	 * @return if it involves capturing
	 */
	public default boolean isCapture() {
		return false;
	}
	
	/**
	 * Gets the type of captured piece for this move.
	 * @return The {@code PieceType}
	 */
	public default PieceType getCapturedPiece() {
		return null;
	}
	
	/**
	 * Determines if this move is an en passant
	 * @return true iff it is
	 */
	public default boolean isEnPassant() {
		return false;
	}
	
	/**
	 * Gets the square that the en passant capture happens on, if this is an en passant move, otherwise null
	 * @return The {@code Square}
	 */
	public default Square getEnPassantCaptureSquare() {
		if (!isEnPassant()) {
			return null;
		}
		return Square.getByFileAndRank(getEndSquare().getFile(), getStartSquare().getRank());
	}
	
	/**
	 * Determines if this move involves castling or not
	 * @return true iff it does
	 */
	public default boolean isCastle() {
		return false;
	}
	
	/**
	 * If this move is castling, determines which of the {@code Side}s of the board the castling is towards.
	 * @return The {@code Side} that represent which side this castling is towards.
	 */
	public default Side getCastlingSide() {
		if (!isCastle()) {
			return null;
		}
		return Side.getByBoolean(getEndSquare().getFile().getIndex() > getStartSquare().getFile().getIndex());
	}
	
	/**
	 * Determines if this move involves promoting
	 * @return true iff it does
	 */
	public default boolean isPromotion() {
		return false;
	}
	
	/**
	 * Determines what type of piece the pawn promotes to with this move
	 * @return The {@code PieceType}
	 */
	public default PieceType getPromotionPiece() {
		return null;
	}
	
	/**
	 * Compresses this {@code Move} into an int
	 * @return The compressed move in int form
	 */
	public int compress();
	
	
}
