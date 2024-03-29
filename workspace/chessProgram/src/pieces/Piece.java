package pieces;

import java.util.Arrays;
import java.util.List;

import boardFeatures.Square;
import gamePlaying.Color;
import lines.Direction;
import moveCalculationStructures.SquareSet;
import moves.Move;
import moves.ProcessedBoard;
import pieceUtilities.PieceUtility;
import support.BadArgumentException;
import static support.Constants.*;

public enum Piece {

	
	NONE(RUNNING_FROM_ECLIPSE ? 0x3000 : ' '),
	WHITE_PAWN(0x2659),
	WHITE_KNIGHT(0x2658),
	WHITE_BISHOP(0x2657),
	WHITE_ROOK(0x2656),
	WHITE_QUEEN(0x2655),
	WHITE_KING(0x2654),
	BLACK_PAWN(0x265F),
	BLACK_KNIGHT(0x265E),
	BLACK_BISHOP(0x265D),
	BLACK_ROOK(0x265C),
	BLACK_QUEEN(0x265B),
	BLACK_KING(0x265A);
	
	private final static Piece[] realPieces = Arrays.copyOfRange(values(), 1, values().length);
	private final PieceType type;
	private final Color color;
	private final String stringPicture;
	private final PieceUtility utilityInstance;
	
	private Piece(int picture) {
		if (this.ordinal() == 0) {
			this.type = null;
			this.color = null;
			this.utilityInstance = null;
		} else {
			this.type = PieceType.values()[(this.ordinal() - 1) % 6];
			this.color = Color.getColor(this.ordinal() < 7);
			this.utilityInstance = this.type.getUtilityInstanceConstructor().apply(this.color);
		}
		stringPicture = Character.toString((char) picture);
	}
	
	/**
	 * Gets the type of piece this is
	 * @return the {@code PieceType} of this piece
	 */
	public PieceType getType() {
		return type;
	}
	
	/**
	 * Gets the color of this piece
	 * @return the {@code Color}
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * How to represent this {@code Piece} in bits
	 * @return The representation
	 */
	public int getBitRepresentation() {
		return this.ordinal();
	}
	
	/**
	 * Gets the utility class for this piece
	 * @return The utility instance
	 */
	public PieceUtility getUtilityInstance() {
		return utilityInstance;
	}
	
	/**
	 * Gets the {@code Square}s this {@code Piece} can threaten from some {@code Square}
	 * @param square The {@code Square} from which this can threaten
	 * @return The {@code SquareSet} containing the threats
	 */
	public SquareSet getPossibleThreatsFromSquare(Square square) {
		return new SquareSet(getUtilityInstance().calculatePossibleSquaresToThreaten(square), square);
	}
	
	/**
	 * Gets the {@code Square}s this {@code Piece} can move to from some {@code Square}
	 * @param square The {@code Square} from which this can move
	 * @return The {@code SquareSet} containing the destinations. Note, this may return a {@code SquareSet}, {@code PawnMoveSet}, or {@code KingMoveSet}
	 */
	public SquareSet getPossibleMovesFromSquare(Square square) {
		return getUtilityInstance().calculatePossibleSquaresToMoveTo(square);
	}
	
	/**
	 * Determines if this piece type can move in a particular direction
	 * @param dir The direction
	 * @return true iff it can
	 */
	public boolean movesInDirection(Direction dir) {
		return getUtilityInstance().movesInDirection(dir);
	}
	
	/**
	 * Determines if this piece type can threaten in a particular direction
	 * @param dir The direction
	 * @return true iff it can
	 */
	public boolean threatensInDirection(Direction dir) {
		return getUtilityInstance().movesInDirection(dir);
	}
	
	/**
	 * Returns the piece that is represented by these bits
	 * @param bits The bits representing a piece
	 * @return The {@code Piece}
	 */
	public static Piece getPieceByBits(int bits) {
		if (bits >= values().length) {
			throw new BadArgumentException(bits, int.class, "The bit string cannot represent a piece because it is too high");
		}
		return values()[bits];
	}
	
	/**
	 * An array of all the pieces besides 'NONE'
	 * @return The array of pieces
	 */
	public static Piece[] realPieces() {
		return realPieces;
	}
	
	/**
	 * Gets the piece that has a color and a type
	 * @param color The {@code Color} of the {@code Piece}
	 * @param type The {@code PieceType} of the {@code Piece}
	 * @return The {@code Piece} with that color and type
	 */
	public static Piece getByColorAndType(Color color, PieceType type) {
		if (color == null || type == null) {
			return NONE;
		}
		return values()[(color.isWhite() ? 1 : 7) + type.ordinal()];
	}
	
	
	/**
	 * Gets the legal moves of this piece in any situation.
	 * @param square The {@code Square} mapping to this piece in the {@code Board}
	 * @param board The {@code Board} to get the moves from
	 * @param toMove The {@code Color} whose move it is
	 * @return The {@code Set} of the legal {@code Move}s
	 */
	public List<Move> getLegalMoves(Square square, ProcessedBoard<?> board) {
		if (board.getPieceAtSquare(square) != this) {
			throw new BadArgumentException(square, Square.class, "Can't get legal moves for a different piece than what is on the provided square");
		}
		if (board.whoseMove() != this.color) {
			throw new BadArgumentException(this, Piece.class, "Can't calculate legal moves for a piece of the wrong color");
		}
		if (this == NONE) {
			throw new BadArgumentException(this, Piece.class, "Can't calculate legal moves for an empty square");
		}
		return getUtilityInstance().getLegalMoves(square, board);
	}
	
	/**
	 * Turns this piece into the character that represents it in a FEN string
	 * @return The representation for the FEN string
	 */
	public String toFENCharacter() {
		return this.color.adjustCapitalization(this.type.getAbbreviationLetter());
	}
	
	@Override
	public String toString() {
		return this.stringPicture;
	}
	
}
