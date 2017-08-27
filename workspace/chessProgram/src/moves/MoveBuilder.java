package moves;

import pieces.PieceType;

public interface MoveBuilder<M extends Move> {

	
	/**
	 * Specifies the type of piece that this move captures. While en passants involve capturing, though, this should not be called for an en passant
	 * @param capturedPiece The type of captured piece
	 * @return This builder
	 */
	public MoveBuilder<M> withCapture(PieceType capturedPiece);
	
	/**
	 * Specifies the type of piece that the move promotes to
	 * @param promotionPiece The {@code PieceType}
	 * @return This builder
	 */
	public MoveBuilder<M> withPromotion(PieceType promotionPiece);
	
	/**
	 * Hatches this cocoon into a {@code Move}. 
	 * @return The {@code Move}
	 */
	public M build();
}