package moves;

import java.util.List;
import java.util.Set;

import boardFeatures.Square;
import gamePlaying.Color;
import lines.Direction;
import lines.File;
import pieces.Piece;
import pieces.PieceType;
import representation.Board;
import representation.CastlingRights;

/**
 * Used to preprocess a board before calculating the moves, so that calculating them is easier than accessing the board directly
 */
public interface ProcessedBoard<B extends Board> {

	
	/**
	 * Gets the color to move
	 * @return the {@code Color}
	 */
	public Color whoseMove();
	
	/**
	 * Can castling be done the particular way, without considering the specifics of the position?
	 * @param right The castling way
	 * @return if it can be done
	 */
	public boolean canCastle(CastlingRights right);
	
	/**
	 * What file can an en passant capture be onto? If none, this is null
	 * @return The {@code File}
	 */
	public File getEnPassantFile();
	
	/**
	 * Gets the list of squares instances of this piece are on
	 * @param piece The piece to get the list for
	 * @return The list of squares
	 */
	public List<Square> getListOfSquaresForPiece(Piece piece);
	
	/**
	 * Gets the piece that is at a square
	 * @param square The square with the piece
	 * @return The {@code Piece}
	 */
	public Piece getPieceAtSquare(Square square);
	
	/**
	 * Gets the square that the king of the color to move is on
	 * @return The {@code Square}
	 */
	public default Square getKingSquare() {
		return getListOfSquaresForPiece(Piece.getByColorAndType(whoseMove(), PieceType.KING)).get(0);
	}
	
	/**
	 * Determines if there is no piece at the square
	 * @param square The square to check
	 * @return If there's no piece there
	 */
	public default boolean isEmptySquare(Square square) {
		Piece piece = getPieceAtSquare(square);
		return isNotAPiece(piece);
	}
	
	/**
	 * Checks if the given piece is meaningful or not
	 * @param piece The piece to check
	 * @return true iff the given piece is null/NONE
	 */
	public default boolean isNotAPiece(Piece piece) {
		return piece == null || piece == Piece.NONE;
	}
	
	/**
	 * Determines if the square does not hold a piece of the same color as the player to move
	 * @param square The square to check
	 * @return true iff there is no piece at that square or the piece that is there is the other color
	 */
	public default boolean isNotSameColor(Square square) {
		Piece piece = getPieceAtSquare(square);
		return isNotSameColor(piece);
	}
	
	/**
	 * Checks if the piece (can be null) is not the same color as the moving side
	 * @param piece The piece to check
	 * @return true iff the given piece is null/NONE or of the other color
	 */
	public default boolean isNotSameColor(Piece piece) {
		return isNotAPiece(piece) || piece.getColor() != whoseMove();
	}
	
	/**
	 * Figures out if the king is in check, squares around it it can move to safely, and what pins are present
	 * @param The possible squares that the king could move to if they're safe
	 */
	public void calculateKingSafety();
	
	/**
	 * Retrieves the list of squares with pieces that are giving check
	 * @return The list of squares
	 */
	public Set<Square> whoIsAttackingTheKing();
	
	/**
	 * Returns the list of the squares around the king that are safe to move to
	 * @return The list of safe squares
	 */
	public Set<Square> getSafeKingDestinations();
	
	/**
	 * Determines if there is a piece in the way that would block movement from one square to another
	 * @param start The start square for the movement
	 * @param end The end square for the movement
	 * @return Iff there's a piece in the way
	 */
	public boolean isMovementBlocked(Square start, Square end);
	
	/**
	 * Determines if a piece on a square is pinned to the king. If so, the direction to the pinning piece will be returned, otherwise NONE
	 * @param square The square of the possibly pinned piece
	 * @return The direction to the pinning piece
	 */
	public Direction isPiecePinned(Square square);
	
	/**
	 * Determines if en passant is disallowed because moving the pawn and removing the opposing pawn would allow the king to be captured
	 * @param movingPawnFile The file of the pawn performing the en passant
	 * @return true iff it's pinned
	 */
	public boolean isEnPassantPinned(File movingPawnFile);
	
}
