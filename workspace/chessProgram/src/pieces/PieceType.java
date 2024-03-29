package pieces;

import java.util.function.Function;

import gamePlaying.Color;
import pieceUtilities.Bishop;
import pieceUtilities.King;
import pieceUtilities.Knight;
import pieceUtilities.Pawn;
import pieceUtilities.PieceUtility;
import pieceUtilities.Queen;
import pieceUtilities.Rook;
import support.BadArgumentException;
import static support.Constants.*;


/**
 * 
 * @author matthewslesinski
 *
 */
public enum PieceType {

	PAWN("pawn", "", "p", ESTIMATED_PAWN_MATERIAL_VALUE, Pawn::new),
	KNIGHT("knight", "N", "n", ESTIMATED_KNIGHT_MATERIAL_VALUE, color -> new Knight()),
	BISHOP("bishop", "B", "b", ESTIMATED_BISHOP_MATERIAL_VALUE, color -> new Bishop()),
	ROOK("rook", "R", "r", ESTIMATED_ROOK_MATERIAL_VALUE, color -> new Rook()),
	QUEEN("queen", "Q", "q", ESTIMATED_QUEEN_MATERIAL_VALUE, color -> new Queen()),
	KING("king", "K", "k", ESTIMATED_KING_MATERIAL_VALUE, King::new);
	
	private final String readableForm;
	private final String moveLetter;
	private final String pieceLetter;
	private final double conventionalEvaluation;
	private final static PieceType[] HORIZONTAL_MOVERS = {ROOK, QUEEN};
	private final static PieceType[] PROMOTION_PIECES = {KNIGHT, BISHOP, ROOK, QUEEN};
	private final static PieceType[] LINE_MOVERS = {BISHOP, ROOK, QUEEN};
	private final static PieceType[] NON_KNIGHTS = {PAWN, BISHOP, ROOK, QUEEN, KING};
	private final static PieceType[] EXPENDABLE_PIECES = {PAWN, KNIGHT, BISHOP, ROOK, QUEEN};
	
	/**
	 * Holds the utility methods calculating the legal moves for this piece
	 */
	private final Function<Color, PieceUtility> utilityInstanceConstructor;
	
	/**
	 * Describes a particular type of piece.
	 * @param readableForm How to describe this piece type in plain english
	 * @param moveLetter The letter used to represent this piece
	 * @param pieceLetter The letter used to abbreviate this piece type
	 * @param conventionalEvaluation The conventional values, in pawns, assumed to each piece type, when chess is taught to beginners (king's get 0)
	 * @param constructor A constructor for the utility class for this type of piece. A constructor
	 * is an argument here because the utility class can't be instantiated earlier, since its constructor
	 * takes this {@code PieceType} as an argument.
	 */
	private PieceType(String readableForm, String moveLetter, String pieceLetter, double conventionalEvaluation, Function<Color, PieceUtility> constructor) {
		this.readableForm = readableForm;
		this.utilityInstanceConstructor = constructor;
		this.moveLetter = moveLetter;
		this.pieceLetter = pieceLetter;
		this.conventionalEvaluation = conventionalEvaluation;
	}
	
	
	/**
	 * Gets the piece type given by the index
	 * @param index The index to get
	 * @return The {@code PieceType} at that index in the array of {@code PieceType}s
	 */
	public static PieceType getByIndex(int index) {
		return values()[index];
	}
	
	/**
	 * Gets the piece type given the corresponding letter in algebraic notation.
	 * @param letter: The letter used to represent the piece.
	 * @return The piece type corresponding to the input letter.
	 */
	public static PieceType getByLetter(String letter) {
		switch (letter.toUpperCase()) {
		case "":  // Alternate for a pawn 
		case "P": return PieceType.PAWN;
		case "N": return PieceType.KNIGHT;
		case "B": return PieceType.BISHOP;
		case "R": return PieceType.ROOK;
		case "Q": return PieceType.QUEEN;
		case "K": return PieceType.KING;
		default: throw new BadArgumentException(letter, String.class, String.format("Input string \"%s\" does not correspond to a piece.", letter));
		}
	}
	
	/**
	 * Detemrines if this piece can move far across the board in one turn.
	 * @return true iff it can
	 */
	public boolean isLongRange() {
		return this.ordinal() >= 2 && this.ordinal() <= 4;
	}
	
	/**
	 * Determines if the piece can be promoted to
	 * @return true iff so
	 */
	public boolean isPromotionPiece() {
		return this.ordinal() >= 1 && this.ordinal() <= 4;
	}
	
	/**
	 * Returns an array of the pieces that a pawn can promote to
	 * @return The array of {@code PieceType}s
	 */
	public static PieceType[] getPromotionPieces() {
		return PROMOTION_PIECES;
	}
	
	/**
	 * Returns an array of the pieces that move long range horizontally
	 * @return The array of {@code PieceType}s
	 */
	public static PieceType[] getHorizontalMovers() {
		return HORIZONTAL_MOVERS;
	}
	
	/**
	 * Returns an array of the pieces that move long range
	 * @return The array of {@code PieceType}s
	 */
	public static PieceType[] getLineMovers() {
		return LINE_MOVERS;
	}
	
	/**
	 * Returns an array of the pieces that move along some line
	 * @return The array of {@code PieceType}s
	 */
	public static PieceType[] getAllButKnight() {
		return NON_KNIGHTS;
	}
	
	/**
	 * Returns an array of the pieces that aren't the king
	 * @return The array of {@code PieceType}s
	 */
	public static PieceType[] getExpendablePieces() {
		return EXPENDABLE_PIECES;
	}
	
	/**
	 * Gets the utility class constructor for this type
	 * @return The {@code PieceUtility}'s constructor
	 */
	public Function<Color, PieceUtility> getUtilityInstanceConstructor() {
		return utilityInstanceConstructor;
	}
	
	/**
	 * Gets the string name of this piece, as a human would recognize it
	 * @return The name
	 */
	public String getName() {
		return this.readableForm;
	}
	
	/**
	 * Gets the letter that this piece type commonly gets abbreviated to
	 * @return The letter
	 */
	public String getAbbreviationLetter() {
		return pieceLetter;
	}
	
	/**
	 * Gets the conventional value attributed to pieces of this type, when chess is taught to beginners. Kings are
	 * given 0, so they don't overshadow the values of the other pieces
	 * @return The double representation of the value
	 */
	public double getConventionalEvaluation() {
		return conventionalEvaluation;
	}
	
	@Override
	public String toString() {
		return this.moveLetter;
	}
}
