package moves;

import java.util.List;
import java.util.Map;

import boardFeatures.File;
import boardFeatures.Rank;
import boardFeatures.Side;
import boardFeatures.Square;
import gamePlaying.Color;
import pieces.PieceType;

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
	
	/**
	 * Gets the string form of this move. Whether to include the start file or rank is inferred using
	 * the provided {@code Map}, which tells which moves involve moving a piece and end on a square.
	 * @param endSquareMap
	 * @return The string of the move
	 */
	public default String getMoveAsString(Map<PieceType, Map<Square, List<Move>>> endSquareMap) {
		boolean includeFile = false;
		boolean includeRank = false;
		File startFile = getStartSquare().getFile();
		Rank startRank = getStartSquare().getRank();
		List<Move> candidates = endSquareMap.get(this.getMovingPiece()).get(this.getEndSquare());
		if (candidates.size() > 1) {
			boolean otherCandidates = false;
			for (Move candidate : candidates) {
				if (!this.getStartSquare().equals(candidate.getStartSquare())) {
					
					// If the candidate has the same file or rank, then we need to include the corresponding bit of information.
					// Also, we've found some other square that has a piece of the same moving piece which can move to the end
					// square of this move
					otherCandidates = true;
					includeFile |= startFile == candidate.getStartSquare().getFile();
					includeRank |= startRank == candidate.getStartSquare().getRank();
				}
			}
			// If there would still be ambiguity about which square is moving to the end square, cut it off
			if (!includeFile && !includeRank && otherCandidates) {
				includeFile = true;
			}
		}
		return getMoveAsString(includeFile, includeRank);
	}
	
	/**
	 * Converts this move into a string
	 * @param includeFile Whether to include the start file
	 * @param includeRank Whether to include the start rank
	 * @return The string of the move
	 */
	public default String getMoveAsString(boolean includeFile, boolean includeRank) {
		if (isCastle()) {
			return Side.getByRelation(getEndSquare()).isKingside() ? "0-0" : "0-0-0";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(getMovingPiece());
		if (includeFile || (getMovingPiece() == PieceType.PAWN && isCapture())) {
			builder.append(getStartSquare().getFile().getHumanReadableForm());
		}
		if (includeRank) {
			builder.append(getStartSquare().getRank().getHumanReadableForm());
		}
		if (isCapture()) {
			builder.append("x");
		}
		builder.append(getEndSquare());
		if (isPromotion()) {
			builder.append("=").append(getPromotionPiece());
		}
		return builder.toString();
	}
	
}
