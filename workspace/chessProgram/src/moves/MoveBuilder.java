package moves;

import lines.File;
import pieces.PieceType;
import representation.Board;

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
	 * Records if the move should remove the ability to perform en passant on some {@code File}
	 * @param previousPrivileges
	 * @return This builder
	 */
	public MoveBuilder<M> withRemovedEnPassantPrivileges(File previousPrivileges);
	
	/**
	 * Records if the move should remove the ability for the moving color to castle kingside in the future
	 * @return This builder
	 */
	public MoveBuilder<M> withRemovedKingsideCastlingRights();

	/**
	 * Records if the move should remove the ability for the moving color to castle queenside in the future
	 * @return This builder
	 */
	public MoveBuilder<M> withRemovedQueensideCastlingRights();

	/**
	 * Records if the move should remove the ability for the not moving color to castle in some way in the future.
	 * That way can be inferred by the end square of this move
	 * @return This builder
	 */
	public MoveBuilder<M> withRemovedEnemyCastlingRights();

	/**
	 * Records the rights this move will take away from the given {@code Board}
	 * @param previousBoard The {@code Board} to determine the removed rights from
	 * @return This builder
	 */
	public MoveBuilder<M> withChangedRightsFromBoard(Board previousBoard);
	
	/**
	 * Hatches this cocoon into a {@code Move}. 
	 * @return The {@code Move}
	 */
	public M build();
}
