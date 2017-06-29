package moves;

import java.util.List;

import boardFeatures.Direction;
import boardFeatures.File;
import boardFeatures.Square;
import gamePlaying.Color;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;

/**
 * Used to preprocess a board before calculating the moves, so that calculating them is easier than accessing the board directly
 */
public interface DecompressedBoard<B extends Board> {

	
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
	 * Gets the square with a piece on it that is n squares with pieces on them away from the provided one along the direction
	 * @param square The current square
	 * @param dir The direction to travel
	 * @param n How many steps away. This must not be negative
	 * @return The {@code Square}, or null if none
	 */
	public Square getNPiecesAway(Square square, Direction dir, int n);
	
	/**
	 * Gets the square with a piece on it that is next along the direction
	 * @param square The current square
	 * @param dir The direction to travel
	 * @return The {@code Square}, or null if none
	 */
	public default Square getNextSquareWithPiece(Square square, Direction dir) {
		return getNPiecesAway(square, dir, 1);
	}
}
