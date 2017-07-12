package representation;

import java.util.HashSet;
import java.util.Set;

import boardFeatures.File;
import boardFeatures.Rank;
import boardFeatures.Square;
import gamePlaying.Color;
import pieces.Piece;
import pieces.PieceType;

public abstract class BoardBuilder<B extends Board> {
	
	private static final int FEN_CHUNKS = 6;
	private static final int FEN_BOARD_ROWS = 8;
	
	protected BoardBuilder() {};
	
	/**
	 * Ensures that all {@code BoardBuilder}s can be instantiated from an array of {@code Piece}s
	 * @param pieces an array of {@code Piece}s
	 * @param whoToMove The {@code Color} of the player whose turn it is in this position
	 */
	protected BoardBuilder(Piece[] pieces, Color whoToMove) {
		for (Square square : Square.values()) {
			withPieceAtSquare(square.getValueOfSquareInArray(pieces), square);
		}
		withColorToMove(whoToMove);
	}
	
	/**
	 * Produces a {@code BoardBuilder} from a FEN string.
	 * @param fen: a FEN string representing the board.
	 */
	protected BoardBuilder(String fen) {
		String[] fenArray = fen.split(" ");
		if (fenArray.length != FEN_CHUNKS) {
			throw new IllegalArgumentException("Invalid FEN string!");
		}
		String[] boardArray = fenArray[0].split("/");
		if (boardArray.length != FEN_BOARD_ROWS) {
			throw new IllegalArgumentException("Invalid FEN string!");
		}
		
		// piece locations
		for (int i = 0; i < FEN_BOARD_ROWS; i++) {
			int rank = FEN_BOARD_ROWS - i;
			String rowPieces = boardArray[i];
			char file = 'a';
			for (int j = 0; j < rowPieces.length(); j++) {
				char piece = rowPieces.charAt(j);
				if (piece >= '1' && piece <= '8') {
					file += Integer.valueOf("" + piece);
					if (file > 'h') {
						break;
					}
				} else {
					Square newSquare = Square.getByFileAndRank(
							File.getByHumanReadableForm(file+""), 
							Rank.getByHumanReadableForm(rank+""));
					Piece newPiece = Piece.getByColorAndType(
							Color.getColor(piece < 'a'), PieceType.getByLetter(Character.toUpperCase(piece)+""));
					withPieceAtSquare(newPiece, newSquare);
				}
			}
		}
		
		// side to move
		if (!fenArray[1].equals("w") && !fenArray[1].equals("b")) {
			throw new IllegalArgumentException("Invalid FEN string!");
		}
		withColorToMove(Color.getColor(fenArray[1].equals("w")));
		
		// castling rights
		Set<CastlingRights> missingRights = new HashSet<>();
		for (CastlingRights right : CastlingRights.values()) {
			missingRights.add(right);
		}

		String fenRights = fenArray[2];
		for (int i = 0; i < fenRights.length(); i++) {
			switch (fenRights.charAt(i)) {
			case 'K': {
				missingRights.remove(CastlingRights.BLACK_KINGSIDE);
				withCastlingRight(CastlingRights.BLACK_KINGSIDE, true);
			}
			case 'Q': {
				missingRights.remove(CastlingRights.BLACK_QUEENSIDE);
				withCastlingRight(CastlingRights.BLACK_QUEENSIDE, true);
			}
			case 'k': {
				missingRights.remove(CastlingRights.WHITE_KINGSIDE);
				withCastlingRight(CastlingRights.WHITE_KINGSIDE, true);
			}
			case 'q': {
				missingRights.remove(CastlingRights.WHITE_QUEENSIDE);
				withCastlingRight(CastlingRights.WHITE_QUEENSIDE, true);
			}
			}
		}
		for (CastlingRights right : missingRights) {
			withCastlingRight(right, false);
		}
		
		// en passant
		String enPassantSquare = fenArray[3];
		String enPassantFile = enPassantSquare.substring(0, 1);
		withEnPassant(fenArray[3].equals("-") ? null : File.getByHumanReadableForm(enPassantFile));
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
	 * Builds the board from this builder
	 * @return The {@code ImmutableArrayBoard} instance
	 */
	public abstract B build();
}
