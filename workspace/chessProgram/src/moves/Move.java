package moves;

import java.util.LinkedList;
import java.util.List;

import boardFeatures.Side;
import boardFeatures.Square;
import gamePlaying.Color;
import lines.File;
import pieces.Piece;
import pieces.PieceType;
import representation.CastlingRights;

/**
 * Represents a move in the game. There are various default implementations in this interface.
 * They should be overridden where necessary by implementing classes
 * @author matthewslesinski
 *
 */
public interface Move {

	/**
	 * Gets the {@code Color} of the player making this move
	 * @return The {@code Color}
	 */
	public Color getMovingColor();
	
	/**
	 * Returns the {@code PieceType} that gets moved
	 * @return the {@code PieceType} that moves
	 */
	public PieceType getMovingPieceType();
	
	/**
	 * Gets the {@code Piece} that the movement is performed on
	 * @return The {@code Piece}
	 */
	public default Piece getMovingPiece() {
		return Piece.getByColorAndType(getMovingColor(), getMovingPieceType());
	}
	
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
	public default PieceType getCapturedPieceType() {
		return null;
	}
	
	/**
	 * Gets the {@code Piece} that gets captured by this move
	 * @return The {@code Piece}
	 */
	public default Piece getCapturedPiece() {
		return Piece.getByColorAndType(getMovingColor().getOtherColor(), getCapturedPieceType());
	}
	
	/**
	 * Gets the {@code Square} that the captured {@code Piece} is on
	 * @return The {@code Square}
	 */
	public default Square getCaptureSquare() {
		return isEnPassant() ? getEnPassantCaptureSquare() : getEndSquare();
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
	 * Gets the square that the en passant pawn moves to, if this is an en passant move, otherwise null
	 * @return The {@code Square}
	 */
	public default Square getEnPassantDestinationSquare() {
		if (!isEnPassant()) {
			return null;
		}
		return Square.getByFileAndRank(getEndSquare().getFile(), getMovingColor().getEnPassantDestinationRank());
	}
	
	/**
	 * Gets the {@code Square} that the moving piece lands on
	 * @return The {@code Square}
	 */
	public default Square getDestinationSquare() {
		return isEnPassant() ? getEnPassantDestinationSquare() : getEndSquare();
	}
	
	/**
	 * Determines if this move involves castling or not
	 * @return true iff it does
	 */
	public default boolean isCastle() {
		return false;
	}
	
	/**
	 * Gets the {@code CastlingRights} that represent the way of castling that is done, assuming this move is a castle
	 * @return The {@code CastlingRights}
	 */
	public default CastlingRights getUsedCastlingRight() {
		return CastlingRights.getByColorAndSide(getMovingColor(), Side.getByRelation(getEndSquare()));
	}
	
	/**
	 * Gets the {@code Piece} representing a rook of the moving color if this move is a castle, or null otherwise
	 * @return The {@code Piece}
	 */
	public default Piece getSecondaryMovingPieceForCastling() {
		return isCastle() ? Piece.getByColorAndType(getMovingColor(), PieceType.ROOK) : null;
	}
	
	/**
	 * Gets the {@code Square} the rook starts on when castling, assuming this move is a castle
	 * @return The {@code Square}
	 */
	public default Square getSecondaryStartSquareForCastling() {
		return getUsedCastlingRight().getRookSquare();
	}
	
	/**
	 * Gets the {@code Square} the rook ends on when castling, assuming this move is a castle
	 * @return The {@code Square}
	 */
	public default Square getSecondaryEndSquareForCastling() {
		return getUsedCastlingRight().getTargetRookSquare();
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
	public default PieceType getPromotionPieceType() {
		return null;
	}
	
	/**
	 * Gets the {@code Piece} that this move promotes to
	 * @return The {@code Piece}
	 */
	public default Piece getPromotionPiece() {
		return Piece.getByColorAndType(getMovingColor(), getPromotionPieceType());
	}
	
	/**
	 * Gets the {@code Piece} that ends up on the {@code Square} this move ends on
	 * @return The {@code Piece}
	 */
	public default Piece getEndPiece() {
		return isPromotion() ? getPromotionPiece() : getMovingPiece();
	}
	
	/**
	 * Tells if this move takes away the ability to en passant
	 * @return true iff it does
	 */
	public default boolean removesEnPassantPrivileges() {
		return false;
	}
	
	/**
	 * Tells the {@code File} that en passant privileges gets removed for capturing ont
	 * @return The {@code File}
	 */
	public default File removedEnPassantFile() {
		return null;
	}
	
	/**
	 * Tells if this takes away the ability for white to castling kingside
	 * @return true iff it does
	 */
	public default boolean preventsWhiteKingsideCastling() {
		return false;
	}
	
	/**
	 * Tells if this takes away the ability for white to castling queenside
	 * @return true iff it does
	 */
	public default boolean preventsWhiteQueensideCastling() {
		return false;
	}
	
	/**
	 * Tells if this takes away the ability for black to castling kingside
	 * @return true iff it does
	 */
	public default boolean preventsBlackKingsideCastling() {
		return false;
	}
	
	/**
	 * Tells if this takes away the ability for black to castling queenside
	 * @return true iff it does
	 */
	public default boolean preventsBlackQueensideCastling() {
		return false;
	}
	
	/**
	 * Tells if this move will allow the opponent to potentially move en passant
	 * @return true iff it does
	 */
	public default boolean allowsEnPassant() {
		return getMovingPieceType() == PieceType.PAWN && getStartSquare().getRank() == getMovingColor().getPawnStartRank() &&
				getEndSquare().getRank() == getMovingColor().getOtherColor().getEnPassantCaptureRank();
	}
	
	/**
	 * Assuming this move potentially allows the opponent to perform en passant, this tells which {@code File} the en passant would capture onto
	 * @return The {@code File}
	 */
	public default File allowedEnPassantFile() {
		return getStartSquare().getFile();
	}
	
	/**
	 * Returns the list of {@code CastlingRights} that get disabled by making this move
	 * @return The {@code List} of {@code CastlingRights}
	 */
	public default List<CastlingRights> newlyDisabledCastlingRights() {
		List<CastlingRights> newlyDisabledRights = new LinkedList<>();
		if (preventsWhiteKingsideCastling()) {
			newlyDisabledRights.add(CastlingRights.WHITE_KINGSIDE);
		}
		if (preventsWhiteQueensideCastling()) {
			newlyDisabledRights.add(CastlingRights.WHITE_QUEENSIDE);
		}
		if (preventsBlackKingsideCastling()) {
			newlyDisabledRights.add(CastlingRights.BLACK_KINGSIDE);
		}
		if (preventsBlackQueensideCastling()) {
			newlyDisabledRights.add(CastlingRights.BLACK_QUEENSIDE);
		}
		return newlyDisabledRights;
	}
	
	/**
	 * Compresses this {@code Move} into an int
	 * @return The compressed move in int form
	 */
	public int compress();
	
}
