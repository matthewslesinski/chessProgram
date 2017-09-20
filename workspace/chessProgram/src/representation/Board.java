package representation;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import boardFeatures.Square;
import gamePlaying.GameState;
import gamePlaying.State;
import lines.File;
import moves.Move;
import pieces.Piece;
import stringUtilities.BoardStringifier;
import support.Constructors;

/**
 * Instances of this class represent a board position at a given point in time. As opposed to {@code State},
 * which describes a general game state not specific to chess, {@code Board}s are specifically chess boards. 
 * @author matthewslesinski
 *
 */
public abstract class Board implements State {
	
	/**
	 * Stores the legal moves this board supports. If null, the moves haven't been calculated yet. If empty, there are no moves.
	 */
	protected Set<Move> legalMoves = null;
	
	/**
	 * Calculates the legal moves for this position and stores them in the {@code legalMoves Set}.
	 * If the game is over, the {@code legalMoves Set} will be empty
	 */
	public abstract void calculateMoves();
	
	/**
	 * Retrieves the {@code Piece} currently at a {@code Square}
	 * @param square The {@code Square} to retrieve the {@code Piece} at
	 * @return The {@code Piece} at that {@code Square}
	 */
	public abstract Piece getPieceAtSquare(Square square);
	
	/**
	 * Determines if the piece that is currently at a square is the given piece
	 * @param piece The {@code Piece} to look for
	 * @param square The {@code Square} to check
	 * @return If the {@code Piece}
	 */
	public boolean isPieceAtSquare(Piece piece, Square square) {
		return piece.equals(getPieceAtSquare(square));
	}
	
	/**
	 * Determines if castling in the specified way is allowed in this position
	 * @param right the way of castling
	 * @return If it's allowed
	 */
	public abstract boolean canCastle(CastlingRights right);

	/**
	 * Gets the {@code File} the current player can capture onto with an en passant, or null if none/not allowed
	 * @return The destination {@code File} for the pawn
	 */
	public abstract File enPassantCaptureFile();
	
	/**
	 * Returns the number of plies since the last move that involved either a pawn move or a capture
	 * @return The number of plies
	 */
	public abstract int pliesSinceLastIrreversibleChange();

	/**
	 * Returns a {@code Piece[]} that describes the arrangement of the pieces on this board
	 * @return The {@code Piece[]}
	 */
	public Piece[] toPieceArray() {
		return Arrays.asList(Square.values()).stream()
				.map(square -> this.getPieceAtSquare(square))
				.collect(Collectors.toList()).toArray(new Piece[Square.values().length]);
	}
	
	/**
	 * Gets the last move that was made to get to this board
	 * @return The move
	 */
	public abstract Move lastMove();
	
	/**
	 * Retrieves the long used as the hashcode for this board
	 * @return The long
	 */
	public abstract long getHashCode();
	
	@Override
	public Set<Move> getLegalMoves() {
		if (legalMoves == null) {
			calculateMoves();
		}
		return legalMoves;
	}
	
	/**
	 * Says whether or not the king of the player whose move it is is in check
	 * @return true iff the player's king is in check
	 */
	public abstract boolean isInCheck();
	
	@Override
	public GameState getState() {
		return GameState.getByBoard(this);
	}
	
	@Override
	public boolean isOver() {
		return getLegalMoves().isEmpty();
	}
	
	@Override
	public double evaluate() {
		GameState state = getState();
		if (state != GameState.STILL_GOING) {
			return state.getEvaluation();
		}
		else return Constructors.EVALUATOR_CONSTRUCTOR.get().evaluateBoard(this);
	}
	
	@Override
	public String toString() {
		return new BoardStringifier<>(this).stringify();
	}
	
	@Override
	public int hashCode() {
		return (int) getHashCode();
	}
}
