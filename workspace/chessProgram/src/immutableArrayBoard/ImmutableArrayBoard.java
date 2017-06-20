package immutableArrayBoard;

import boardFeatures.Square;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;

public class ImmutableArrayBoard implements Board {

	/**
	 * The array containing the pieces, and so the actual internal representation of this {@code Board}
	 */
	private final Piece[] board = new Piece[64];
	
	/**
	 * Stores the various rights about this board that are not necessarily visible. These range from castling rights to
	 * en passant capabilities. The first four bits hold the castling rights, where whether castling is still possible (in the 
	 * sense that even if the player can't castle that way in the current position, they could in a later position) for a given
	 * way of castling is stored at that ways's ordinal's (as defined by the {@code CastlingRights} enum) bit. 
	 */
	private final byte rights = 0;
	
	public ImmutableArrayBoard() {
		
	}
	
	public ImmutableArrayBoard(Board board) {
		// TODO
	}
	
	/**
	 * Retrieves if a castling right is enabled
	 * @param right The right to check
	 * @return The boolean stating if the player can castle that way
	 */
	private boolean getCastlingRights(CastlingRights right) {
		// TODO
		return false;
	}
	
	@Override
	public Piece getPieceAtSquare(Square square) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
