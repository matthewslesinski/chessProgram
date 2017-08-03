package pieces;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import boardFeatures.Square;
import gamePlaying.Color;
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
	public final PieceType pieceType;
	
	public final int MAX_ATTACK_DISTANCE;
	
	/**
	 * Constructor for pieces in general. Establishes what piece this is for
	 * @param color
	 */
	protected PieceUtility() {
		this.pieceType = determinePieceType();
		this.MAX_ATTACK_DISTANCE = determineMaxAttackDistance();
	}
	
	public abstract Collection<Square> getPossibleSquaresToThreaten(Color color, Square fromSquare);
	
	/**
	 * Gets the {@code Set} of moves for this piece
	 * @param square The current square containing this piece
	 * @param board The current board requesting the legal moves for this piece
	 * @return The {@code Set} of {@code Move} at a {@code Square} on the {@code Board}
	 */
	public Set<Move> getLegalMoves(Square square, Board board, Color toMove) {
		return getSquaresToMoveTo(square, board, toMove).stream()
				.map(moveSquare -> convertSquareToMove(square, moveSquare, board))
				.collect(Collectors.toCollection(this::setConstructor));
	}
	
	/**
	 * Constructs a new {@code Set} to hold moves
	 * @return The {@code Set}
	 */
	protected Set<Move> setConstructor() {
		// TODO
		return null;
	}
	
	/**
	 * Gets the type of piece this is
	 * @return The {@code PieceType} describing this piece
	 */
	protected abstract PieceType determinePieceType();
	
	/**
	 * Determines the furthest manhattan distance removed an attacked square can be from this piece
	 * @return The distance
	 */
	protected abstract int determineMaxAttackDistance();
	
	/**
	 * Builds a move out of the motion from square to square on a given board
	 * @param fromSquare The square the piece was originally on
	 * @param toSquare The square the piece ends up on
	 * @param board The board the move happens on
	 * @return The Move represented
	 */
	protected Move convertSquareToMove(Square fromSquare, Square toSquare, Board board) {
		// TODO
		return null;
	}
	
	/**
	 * Gets the legal squares for the piece to move to
	 * @param square The start square for the piece
	 * @param board The board the piece is moving in
	 * @param toMove The color of the player making the move
	 * @return The List of squares to move to
	 */
	protected abstract List<Square> getSquaresToMoveTo(Square square, Board board, Color toMove);
	
	/**
	 * Determines if the piece (or absense of a piece) at a square is not of the same color as {@code toCheck}
	 * @param square The square to check
	 * @param board The board containing the position
	 * @param toCheck The color to compare to
	 * @return If toCheck is not the same as the color (or lack thereof) at {@code square} on {@code board}
	 */
	protected static boolean colorIsNotAtSquare(Square square, Board board, Color toCheck) {
		Piece occupier = board.getPieceAtSquare(square);
		return occupier == Piece.NONE || occupier.getColor() != toCheck;
	}
	 
}
