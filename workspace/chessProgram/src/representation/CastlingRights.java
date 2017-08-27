package representation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import boardFeatures.Side;
import boardFeatures.Square;
import gamePlaying.Color;
import lines.File;
import lines.Rank;
import support.Constants;

/**
 * Describes the important distinctions between the four different ways castling can be done
 * @author matthewslesinski
 *
 */
public enum CastlingRights {
	
	WHITE_KINGSIDE(Color.WHITE, Side.KINGSIDE, 'K'),
	WHITE_QUEENSIDE(Color.WHITE, Side.QUEENSIDE, 'Q'),
	BLACK_KINGSIDE(Color.BLACK, Side.KINGSIDE, 'k'),
	BLACK_QUEENSIDE(Color.BLACK, Side.QUEENSIDE, 'q');
	
	/** Each way of castling can only be done by one {@code Color} */
	private final Color color;
	
	/** Each way of castling is performed along some {@code Rank} */
	private final Rank rank;
	
	/** Each way of castling involves moving toward one {@code Side} */
	private final Side side;
	
	/** Each way of castling uses a rook that begins the move by sitting on some {@code File} */
	private final File rookFile;
	
	/** The {@code Square} the king starts on for each way of castling */
	private final Square kingSquare;
	
	/** The {@code Square} the rook starts on for each way of castling */
	private final Square rookSquare;
	
	/** Each way of castling moves the king to some {@code Square} */
	private final Square targetKingSquare;
	
	/** Each way of castling moves the rook to some {@code Square} */
	private final Square targetRookSquare;
	
	/** Queenside castling has an extra {@code Square} in the middle, the one on the b file, that can't have a piece on it */
	private final Square extraIntermediarySquare;
	
	private final char readableForm;
	
	
	private CastlingRights(Color color, Side side, char readableForm) {
		this.color = color;
		this.side = side;
		this.rank = color.isWhite() ? Rank.ONE : Rank.EIGHT;
		this.rookFile = side.getRookFile();
		this.kingSquare = Square.getByFileAndRank(Constants.KING_START_FILE, this.rank);
		this.rookSquare = Square.getByFileAndRank(side.getRookFile(), this.rank);
		this.targetKingSquare = Square.getByFileAndRank(side.isKingside() ? File.G : File.C, rank);
		this.targetRookSquare = Square.getByFileAndRank(side.isKingside() ? File.F : File.D, rank);
		this.extraIntermediarySquare = side.isKingside() ? null : Square.getByFileAndRank(File.B, rank);
		this.readableForm = readableForm;
	}
	
	/**
	 * Gets the index for these castling rights, which can be used for indexing into however a {@code Board} stores its castling rights
	 * @return The index
	 */
	public int getIndex() {
		return this.ordinal();
	}
	
	public char getReadableForm() {
		return readableForm;
	}
	
	/**
	 * Gets the color that these castling rights pertain to
	 * @return The {@code Color}
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Gets the {@code Rank} that these castling rights involve castling along
	 * @return the {@code Rank}
	 */
	public Rank getRank() {
		return this.rank;
	}
	
	/**
	 * Gets the {@code Side} that these castling rights involve castling to
	 * @return
	 */
	public Side getSide() {
		return this.side;
	}
	
	/**
	 * Gets the {@code File} the rook on this {@code Side} starts on
	 * @return the {@code File}
	 */
	public File getRookFile() {
		return this.rookFile;
	}
	
	/**
	 * Gets the {@code Square} the king of this {@code Color} starts on
	 * @return the {@code Square}
	 */
	public Square getKingSquare() {
		return this.kingSquare;
	}
	
	/**
	 * Gets the {@code Square} the rook of this {@code Color} and {@code Side} starts on
	 * @return the {@code Square}
	 */
	public Square getRookSquare() {
		return this.rookSquare;
	}
	
	/**
	 * Gets the {@code Square} the king of this {@code Color} would end on when castling
	 * @return the {@code Square}
	 */
	public Square getTargetKingSquare() {
		return this.targetKingSquare;
	}
	
	/**
	 * Gets the {@code Square} the rook of this {@code Color} and {@code Side} ends on when castling
	 * @return the {@code Square}
	 */
	public Square getTargetRookSquare() {
		return this.targetRookSquare;
	}
	
	/**
	 * Determines if this {@code Side} is the queenside and has an extra square in the way
	 * @return if it's the queenside
	 */
	public boolean hasExtraSquare() {
		return this.side == Side.QUEENSIDE;
	}
	
	/**
	 * Gets the extra {@code Square} on the b file that is in the way of castling this way 
	 * @return the {@code Square}
	 */
	public Square getQueensideExtraSquare() {
		return this.extraIntermediarySquare;
	}
	
	/**
	 * Retrieves the enum instance with the given {@code Color} and {@code Side}
	 * @param color The {@code Color}
	 * @param side The {@code Side}
	 * @return The {@code CastlingRights} with those values
	 */
	public static CastlingRights getByColorAndSide(Color color, Side side) {
		return values()[(color.isWhite() ? 0 : 2) + (side.isKingside() ? 0 : 1)];
	}
	
	/**
	 * Gets a collection of the rights that could involve a piece of the provided color starting at the provided square
	 * @param color The color
	 * @param square The square
	 * @return A collection of the rights for the given color, including both kingside and queenside if the square is
	 * that color's king start square, or just the side with the square if it's the rook start square for that color, or
	 * an empty list
	 */
	public static Collection<CastlingRights> getAffectedRightsByColorAndSquare(Color color, Square square) {
		if (square == color.getKingCastleSquare()) {
			return Arrays.asList(getByColorAndSide(color, Side.KINGSIDE), getByColorAndSide(color, Side.QUEENSIDE));
		}
		CastlingRights relevantRight = getByColorAndSide(color, Side.getByRelation(square));
		if (square == relevantRight.getRookSquare()) {
			return Collections.singleton(relevantRight);
		}
		return Collections.emptyList();
	}
	
	public static CastlingRights getByCharacter(char character) {
		switch (character) {
		case 'K':
			return WHITE_KINGSIDE;
		case 'Q':
			return WHITE_QUEENSIDE;
		case 'k':
			return BLACK_KINGSIDE;
		case 'q':
			return BLACK_QUEENSIDE;
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		return Character.toString(readableForm);
	}
}
