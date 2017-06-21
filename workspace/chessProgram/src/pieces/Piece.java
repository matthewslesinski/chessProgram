package pieces;

import java.util.List;

import boardFeatures.Square;
import gamePlaying.Color;
import representation.Board;
import support.BadArgumentException;

public abstract class Piece {
	
	/**
	 * The color, white or black, of this piece
	 */
	protected final Color color;
	
	/**
	 * The type of piece this is
	 */
	protected final PieceType pieceType;
	
	/**
	 * The byte that represents this piece 
	 */
	protected final int bitRepresentation;
	
	/**
	 * Constructor for pieces in general. Establishes the color of the piece
	 * @param color
	 */
	public Piece(Color color, PieceType pieceType, int bitRepresentation) {
		this.color = color;
		this.pieceType = pieceType;
		this.bitRepresentation = bitRepresentation;
	}
	
	/**
	 * Gets this piece's color
	 * @return the {@code Color}
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Gets the type of piece this is
	 * @return The {@code PieceType} describing this piece
	 */
	public PieceType getType() {
		return this.pieceType;
	}
	
	/**
	 * Returns the bit representation of this piece
	 * @return The byte with the bits
	 */
	public int getBitRepresentation() {
		return this.bitRepresentation;
	}
	
	/**
	 * Gets the {@code List} of moves for this piece
	 * @return The {@code List} of {@code Square} at a {@code Square} on the {@code Board}
	 */
	public abstract List<Square> getLegalMoves(Square square, Board board);
	
	/**
	 * Calculates which piece is represented by the bit string, which should have only its first four bits set to anything.
	 * If the bit string is 0, the piece is empty. If it's between 1 and 6 inclusive, it's a white piece. And if it's between
	 * 7 and 12 it's a black piece
	 * @param bits The bits representing the piece
	 * @return The piece represented by the bits
	 */
	public static Piece getPieceByBits(int bits) {
		if (bits > 12) {
			throw new BadArgumentException(bits, int.class, "The bit string cannot represent a piece because it is too high");
		}
		if (bits == 0) {
			return null;
		}
		return getPieceByTypeAndColor(PieceType.getByIndex((bits - 1) % 6), Color.getColor(bits <= 6));
	}
	
	/**
	 * Retrieves a {@code Piece} by its type and its color
	 * @param type The type of piece
	 * @param color Its color
	 * @return The physical piece
	 */
	public static Piece getPieceByTypeAndColor(PieceType type, Color color) {
		if (type == null || color == null) {
			return null;
		}
		// TODO
		return null;
	}
	
	
	@Override
	public String toString() {
		return this.pieceType.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((pieceType == null) ? 0 : pieceType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Piece))
			return false;
		Piece other = (Piece) obj;
		return color == other.color && pieceType == other.pieceType;
	}
}
