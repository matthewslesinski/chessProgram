package stringTranslators;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import boardFeatures.Square;
import lines.File;
import lines.Rank;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;
import support.Constants;
import support.UtilityFunctions;

/**
 * Turns a board into a fen string.
 * @author matthewslesinski
 *
 */
public class FENStringWriter {

	private static final List<Consumer<FENStringWriter>> actions = 
			Arrays.asList(expand(FENStringWriter::addPosition), expand(FENStringWriter::addColorToMove), expand(FENStringWriter::addCastlingRights),
					expand(FENStringWriter::addEnPassantRights), expand(FENStringWriter::addPliesSinceChange), FENStringWriter::addMoveNumber);
	
	/** The board that will be turned into a fen string */
	private final Board board;
	
	/** The moveNumber within the game */
	private final int moveNumber;
	
	/** A stringbuilder to build everything */
	private final StringBuilder builder;
	
	
	public FENStringWriter(Board board, int moveNumber) {
		this.builder = new StringBuilder();
		this.board = board;
		this.moveNumber = moveNumber;
		// Apply the arguments to the actions list
		List<Runnable> appliedActions = (List<Runnable>) actions.stream().map(action -> UtilityFunctions.bind(action, this)).collect(Collectors.toList());
		// There should be a space after each segment
		UtilityFunctions.joinActions(appliedActions, () -> builder.append(Constants.SINGLE_SPACE));
	}
	
	
	/**
	 * Compiles the already-calculated FEN string
	 * @return The FEN string
	 */
	public String makeFEN() {
		return builder.toString();
	}
	
	/**
	 * Adds a position to the FEN by adding to each of its board's ranks 
	 * @param board
	 * @param builder
	 */
	private static void addPosition(Board board, StringBuilder builder) {
		List<Runnable> actions = Arrays.stream(Rank.values()).map(rank -> (Runnable) () -> addFENForRank(board, rank, builder)).collect(Collectors.toList());
		UtilityFunctions.joinActions(UtilityFunctions.reverseList(actions), () -> builder.append(Constants.SLASH));
	}
	
	/**
	 * Adds the section of the FEN string that is specific to a {@code Rank} 
	 * @param board The board to perform this move in the context of
	 * @param rank The rank of the current row
	 * @param builder The string builder to add context to.
	 */
	private static void addFENForRank(Board board, Rank rank, StringBuilder builder) {
		int numSinceLastPiece = 0;
		for (File file : File.values()) {
			Square intersection = Square.getByFileAndRank(file, rank);
			Piece occupant = board.getPieceAtSquare(intersection);
			if (occupant != null && occupant != Piece.NONE) {
				addEmptySquares(numSinceLastPiece, builder);
				numSinceLastPiece = 0;
				builder.append(occupant.toFENCharacter());
			} else {
				numSinceLastPiece += 1;
			}
		}
		addEmptySquares(numSinceLastPiece, builder);
		numSinceLastPiece = 0;
	}
	
	private static void addEmptySquares(int numEmptySquares, StringBuilder builder) {
		if (numEmptySquares > 0) {
			builder.append(numEmptySquares);
		}
	}
	
	private static void addColorToMove(Board board, StringBuilder builder) {
		builder.append(board.whoseMove().toString().charAt(0));
	}
	
	private static void addCastlingRights(Board board, StringBuilder builder) {
		boolean addedCastleRight = false;
		for (CastlingRights right : CastlingRights.values()) {
			addedCastleRight |= addCastlingRight(board, builder, right);
		}
		if (!addedCastleRight) {
			builder.append(Constants.DASH);
		}
	}
	
	private static boolean addCastlingRight(Board board, StringBuilder builder, CastlingRights right) {
		boolean canCastle = board.canCastle(right);
		if (canCastle) {
			builder.append(right.toString());
		}
		return canCastle;
	}
	
	private static void addEnPassantRights(Board board, StringBuilder builder) {
		File enPassantFile = board.enPassantCaptureFile();
		if (enPassantFile != null) {
			builder.append(Square.getByFileAndRank(enPassantFile, board.whoseMove().getEnPassantDestinationRank()));
		} else {
			builder.append(Constants.DASH);
		}
	}
	
	private static void addPliesSinceChange(Board board, StringBuilder builder) {
		builder.append(String.valueOf(board.pliesSinceLastIrreversibleChange()));
	}
	
	private static void addMoveNumber(FENStringWriter writer) {
		writer.builder.append(String.valueOf(writer.moveNumber));
	}
	
	private static Consumer<FENStringWriter> expand(BiConsumer<Board, StringBuilder> originalAction) {
		return writer -> originalAction.accept(writer.board, writer.builder);
	}
}
