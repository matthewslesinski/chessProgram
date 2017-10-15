package stringUtilities;

import java.util.HashSet;
import java.util.Set;

import boardFeatures.Square;
import gamePlaying.Color;
import lines.File;
import lines.Rank;
import pieces.Piece;
import pieces.PieceType;
import representation.Board;
import representation.BoardBuilder;
import representation.CastlingRights;
import support.BadArgumentException;

/**
 * This class can be used to parse a FEN string into a BoardBuilder.
 * @author matthewslesinski
 * 
 * TODO clean up
 *
 * @param <B> The type of board that the resulting BoardBuilder is building
 */
public class FENStringParser<B extends Board> {
	
	private static final int FEN_CHUNKS = 6;
	private static final int FEN_BOARD_ROWS = 8;
	private final BoardBuilder<B> boardBuilder;
	
	public FENStringParser(BoardBuilder<B> boardBuilder) {
		this.boardBuilder = boardBuilder;
	}
	
	
	/**
	 * parses the part of the fen string containing information about only one rank
	 * @param rankArrangement The part of the fen for the rank
	 * @param rank The index of the rank
	 */
	private void setRank(String rankArrangement, int rank) {
		// piece locations
		char file = 'a';
		for (int j = 0; j < rankArrangement.length(); j++) {
			char piece = rankArrangement.charAt(j);
			if (piece >= '1' && piece <= '8') {
				file += Integer.valueOf("" + piece);
				if (file > 'h') {
					break;
				}
			} else {
				Square newSquare = Square.getByFileAndRank(
						File.getByHumanReadableForm(file++ +""), 
						Rank.getByIndex(rank));
				Piece newPiece = Piece.getByColorAndType(
						Color.getColor(piece < 'a'), PieceType.getByLetter(Character.toUpperCase(piece)+""));
				boardBuilder.withPieceAtSquare(newPiece, newSquare);
			}
		}
	}
	
	/**
	 * Sets the pieces on the board from the fen string
	 * @param boardArrangement The string describing the whole board's piece layout
	 */
	private void setPieces(String boardArrangement) {
		String[] boardArray = boardArrangement.split("/");
		
		if (boardArray.length != FEN_BOARD_ROWS) {
			throw new BadArgumentException(boardArrangement, String.class, "Invalid FEN string!");
		}
		
		// piece locations
		for (int i = 0; i < FEN_BOARD_ROWS; i++) {
			int rank = FEN_BOARD_ROWS - i - 1;
			String rowPieces = boardArray[i];
			setRank(rowPieces, rank);
		}
	}
	
	/**
	 * Sets the color for the player to move
	 * @param color
	 */
	private void setColorToMove(String color) {
		if (!color.equals("w") && !color.equals("b")) {
			throw new BadArgumentException(color, String.class, "Invalid FEN string!");
		}
		boardBuilder.withColorToMove(Color.getColor(color.equals("w")));
	}
	
	/**
	 * Sets the castling rights for each side
	 * @param fenRights The fen string describing castling rights
	 */
	private void setCastlingRights(String fenRights) {
		// castling rights
		Set<CastlingRights> missingRights = new HashSet<>();
		for (CastlingRights right : CastlingRights.values()) {
			missingRights.add(right);
		}

		for (int i = 0; i < fenRights.length(); i++) {
			switch (fenRights.charAt(i)) {
			case 'K': {
				missingRights.remove(CastlingRights.BLACK_KINGSIDE);
				boardBuilder.withCastlingRight(CastlingRights.BLACK_KINGSIDE, true);
				break;
			}
			case 'Q': {
				missingRights.remove(CastlingRights.BLACK_QUEENSIDE);
				boardBuilder.withCastlingRight(CastlingRights.BLACK_QUEENSIDE, true);
				break;
			}
			case 'k': {
				missingRights.remove(CastlingRights.WHITE_KINGSIDE);
				boardBuilder.withCastlingRight(CastlingRights.WHITE_KINGSIDE, true);
				break;
			}
			case 'q': {
				missingRights.remove(CastlingRights.WHITE_QUEENSIDE);
				boardBuilder.withCastlingRight(CastlingRights.WHITE_QUEENSIDE, true);
				break;
			}
			default:
				// TODO handle wtf
				break;
			}
		}
		for (CastlingRights right : missingRights) {
			boardBuilder.withCastlingRight(right, false);
		}
	}
	
	/**
	 * Sets the file en passant can be performed to capture onto
	 * @param enPassantSquare The square the pawn will end up on, or "-" if none
	 */
	private void setEnPassant(String enPassantSquare) {
		String enPassantFile = enPassantSquare.substring(0, 1);
		boardBuilder.withEnPassant(enPassantSquare.equals("-") ? null : File.getByHumanReadableForm(enPassantFile));		
	}
	
	/**
	 * Parses the whole fen string
	 * @param fen The string describing the board
	 * @return The {@code BoardBuilder} built from the fen string
	 */
	public BoardBuilder<B> parse(String fen) {
		String[] fenArray = fen.split(" ");
		if (fenArray.length != FEN_CHUNKS) {
			throw new BadArgumentException(fen, String.class, "Invalid FEN string!");
		}
		setPieces(fenArray[0]);
		
		// side to move
		setColorToMove(fenArray[1]);
		
		setCastlingRights(fenArray[2]);
		
		// en passant
		setEnPassant(fenArray[3]);
		return boardBuilder;
	}
}
