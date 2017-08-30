package representation;

import boardFeatures.Square;
import gamePlaying.Color;
import lines.File;
import moves.Move;
import pieces.Piece;
import stringTranslators.FENStringParser;

public abstract class BoardBuilder<B extends Board> {
	
	
	
	/**
	 * Ensures that all {@code BoardBuilder}s can be instantiated from an array of {@code Piece}s
	 * @param pieces an array of {@code Piece}s
	 * @param whoToMove The {@code Color} of the player whose turn it is in this position
	 * @param builder The {@code BoardBuilder} being created
	 */
	protected static <T extends Board> BoardBuilder<T> fromBoard(Piece[] pieces, Color whoToMove, BoardBuilder<T> builder) {
		for (Square square : Square.values()) {
			builder.withPieceAtSquare(square.getValueOfSquareInArray(pieces), square);
		}
		return builder.withColorToMove(whoToMove);
	}
	
	/**
	 * Produces a {@code BoardBuilder} from a FEN string.
	 * @param fen: a FEN string representing the board.
	 */
	protected static <T extends Board> BoardBuilder<T> fromFen(String fen, BoardBuilder<T> boardBuilder) {
		return new FENStringParser<T>(boardBuilder).parse(fen);
	}
	
	/**
	 * Sets the color for this board to the given {@code Color}
	 * @param color the {@code Color} to set
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withColorToMove(Color color);
	
	/**
	 * Puts a piece on a square
	 * @param piece The piece to put
	 * @param square The square for the piece
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withPieceAtSquare(Piece piece, Square square);
	
	/**
	 * Sets the given {@code CastlingRight} to either allowed or not, depending on enabled
	 * @param castlingRight The right to set
	 * @param enabled Whether or not that type of castling should be allowed
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withCastlingRight(CastlingRights castlingRight, boolean enabled);
	
	/**
	 * Sets the en passant bit representation to the given file and a 1 in front if enabled
	 * @param file If the last move was a pawn push 2 squares ahead, the file of that pawn, else null
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withEnPassant(File file);

	/**
	 * Builds the board with the specified count towards the fifty move rule
	 * @param count The count
	 * @return The builder, with the recorded count
	 */
	public abstract BoardBuilder<B> withFiftyMoveRuleCount(int count);
	
	/**
	 * Passes the board that occurred previous to this board to the new one
	 * and sets the move made to reach this board to the given {@code Move}
	 * @param board The previous board
	 * @param move The {@code Move} to get to this board
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withPreviousBoardAndLastMove(Board previousBoard, Move move);

	/**
	 * Builds the board from this builder
	 * @return The {@code ImmutableArrayBoard} instance
	 */
	public abstract B build();
}
