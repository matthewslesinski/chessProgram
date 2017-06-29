package representation;

import boardFeatures.File;
import boardFeatures.Rank;
import boardFeatures.Side;
import boardFeatures.Square;
import gamePlaying.Color;

public enum CastlingRights {

	WHITE_KINGSIDE(Color.WHITE, Side.KINGSIDE),
	WHITE_QUEENSIDE(Color.WHITE, Side.QUEENSIDE),
	BLACK_KINGSIDE(Color.BLACK, Side.KINGSIDE),
	BLACK_QUEENSIDE(Color.BLACK, Side.QUEENSIDE);
	
	private final Color color;
	private final Rank rank;
	private final Side side;
	private final File rookFile;
	private final Square kingSquare;
	private final Square rookSquare;
	private final Square targetKingSquare;
	private final Square targetRookSquare;
	private final Square extraIntermediarySquare;
	
	
	private CastlingRights(Color color, Side side) {
		this.color = color;
		this.side = side;
		this.rank = color.isWhite() ? Rank.ONE : Rank.EIGHT;
		this.rookFile = side.getRookFile();
		this.kingSquare = Square.getByFileAndRank(File.E, this.rank);
		this.rookSquare = Square.getByFileAndRank(rookFile, this.rank);
		this.targetKingSquare = Square.getByFileAndRank(side.isKingside() ? File.G : File.C, rank);
		this.targetRookSquare = Square.getByFileAndRank(side.isKingside() ? File.F : File.D, rank);
		this.extraIntermediarySquare = side.isKingside() ? null : Square.getByFileAndRank(File.B, rank);
	}
	
	/**
	 * Gets the index for these castling rights, which can be used for indexing into however a {@code Board} stores its castling rights
	 * @return The index
	 */
	public int getIndex() {
		return this.ordinal();
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
		return values()[color.isWhite() ? 0 : 2 + (side.isKingside() ? 0 : 1)];
	}
}