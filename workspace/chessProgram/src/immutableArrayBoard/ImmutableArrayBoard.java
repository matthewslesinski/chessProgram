package immutableArrayBoard;

import boardFeatures.File;
import boardFeatures.Square;
import gamePlaying.Color;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;

public class ImmutableArrayBoard implements Board {

	private static final int FIRST_CASTLING_RIGHT_BIT = 0;
	
	private static final int RIGHTS_INDEX = 8;
	
	private static final int ARRAY_SIZE = 9;
	
	private static final int COLOR_INDEX = 0x100;
	
	private static final int EN_PASSANT_INDEX = 4;
	
	private static final int NUMBER_OF_BITS_PER_PIECE = 4;
	
	/**
	 * The array containing the pieces and other board information, and so the actual internal representation of this {@code Board}.
	 * The first 8 indices contain ints representing the file with that index. Each of those ints is divided into 8 groups of four bits.
	 * A piece an be encoded in 4 bits. So that's what's stored in each group of 4 bits: the piece stored at that square when indexing into
	 * the file to get the rank. The last index of the array holds the extra information about board state, such as who's to move. This int
	 * says what castling is potentially allowed, using the first four bits, now or in a deeper position from this one. It also uses second 4 bits
	 * to say what file a pawn could take a pawn from through en passant, and it uses the first bit of the next quarter of the int to say whose move it is.
	 */
	private final int[] board;
	
	
	private ImmutableArrayBoard(int[] board) {
		this.board = board;
	}
		
	
	@Override
	public Piece getPieceAtSquare(Square square) {
		return Piece.getPieceByBits(getBitsAtSquare(square, board));
	}
	
	public static class Builder {
		
		private int[] board = null;
		
		/**
		 * Initializes the board based on another board
		 * @param boardContainer The {@code ImmutableArrayBoard} to base this one off of
		 * @return This builder
		 */
		public Builder(int[] board) {
			this.board = board.clone();
		}
		
		/**
		 * Initializes the board based on another board
		 * @param boardContainer The {@code ImmutableArrayBoard} to base this one off of
		 * @return This builder
		 */
		public Builder(ImmutableArrayBoard boardContainer) {
			this(boardContainer.board.clone());
		}
		
		/**
		 * Initializes the board based on an array of pieces. This does not set any rights except whose move.
		 * @param pieces The array of pieces to put on the board
		 * @param whoToMove Whose current move it is
		 */
		public Builder(Piece[] pieces, boolean whoToMove) {
			this.board = new int[ARRAY_SIZE];
			for (Square square : Square.values()) {
				int bitRepresentation = square.getValueOfSquareInArray(pieces).getBitRepresentation();
				setPieceAtSquare(bitRepresentation, square);
			}
			setRightsByBitMask(COLOR_INDEX, whoToMove);
			
		}
		
		/**
		 * Sets the color for this board to the given {@code Color}
		 * @param color the {@code Color} to set
		 * @return This builder
		 */
		public Builder withColorToMove(Color color) {
			setRightsByBitMask(COLOR_INDEX, color.isWhite());
			return this;
		}
		
		/**
		 * Puts a piece on a square
		 * @param piece The piece to put
		 * @param square The square for the piece
		 * @return This builder
		 */
		public Builder withPieceAtSquare(Piece piece, Square square) {
			setPieceAtSquare(piece.getBitRepresentation(), square);
			return this;
		}
		
		/**
		 * Sets the given {@code CastlingRight} to either allowed or not, depending on enabled
		 * @param castlingRight The right to set
		 * @param enabled Whether or not that type of castling should be allowed
		 * @return This builder
		 */
		public Builder withCastlingRight(CastlingRights castlingRight, boolean enabled) {
			setRightsByBitMask(0x1 << (castlingRight.ordinal() + FIRST_CASTLING_RIGHT_BIT), enabled);
			return this;
		}
		
		/**
		 * Sets the en passant bit representation to the given file and a 1 in front if enabled
		 * @param file If the last move was a pawn push 2 squares ahead, the file of that pawn, else null
		 * @return This builder
		 */
		public Builder withEnPassant(File file) {
			setRightsByBitMask(file != null ? (file.getIndex() | 0b1000) << EN_PASSANT_INDEX : ~0b11110000, file != null);
			return this;
		}
		
		/**
		 * Builds the board from this builder
		 * @return The {@code ImmutableArrayBoard} instance
		 */
		public ImmutableArrayBoard build() {
			return new ImmutableArrayBoard(board);
		}
		
		/**
		 * Sets the rights for the board to have a certain mask
		 * @param mask The mask
		 * @param orBoolean Whether the mask should be ORed, or the reverse of the mask should be ANDed
		 */
		private void setRightsByBitMask(int mask, boolean orBoolean) {
			board[RIGHTS_INDEX] = orBoolean ? board[RIGHTS_INDEX] | mask : board[RIGHTS_INDEX] & ~mask;
		}
		
		/**
		 * Sets a bit representation of a piece at the bit placement for the given square
		 * @param pieceBits The bit representation of the piece
		 * @param square The square the piece is on
		 */
		private void setPieceAtSquare(int pieceBits, Square square) {
			int fileIndex = square.getFile().getIndex();
			board[fileIndex] = board[fileIndex] | (pieceBits << (square.getRank().getIndex() * NUMBER_OF_BITS_PER_PIECE));
		}
	}
	
	/**
	 * Gets the bit representation of the piece at a square
	 * @param square The square to check
	 * @param board The board containing the piece
	 * @return The bit representation, where 0 is nothing, 1-6 is a white piece, and 7-12 is a black piece
	 */
	private int getBitsAtSquare(Square square, int[] board) {
		return (board[square.getFile().getIndex()] >>> (square.getRank().getIndex() * NUMBER_OF_BITS_PER_PIECE)) & 0b1111;
	}

	
}
