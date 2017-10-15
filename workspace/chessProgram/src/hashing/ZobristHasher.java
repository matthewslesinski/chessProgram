package hashing;

import java.util.LinkedList;
import java.util.List;

import boardFeatures.Square;
import lines.File;
import moves.Move;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;
import support.UtilityFunctions;

import static support.Constructors.*;

/**
 * Implements board hashing using Zobrist Hashing. It keeps track of 781 random longs, each of which signifies a possible feature on the board.
 * For instance, the first feature is if there is a white pawn on a1. The second is if there's a white knight there instead. The 13th is if there's
 * a white pawn on a2. The 769th feature is the color to move. The next is the castling rights for castling to the kingside as white. The 774th feature
 * is if en passant is allowed on the a file. Zobrist Hashing takes these random numbers associated with each feature on a board and XORs them together.
 * Furthermore, because XORing is reversible, to remove a feature, simply XOR that feature's number again, which makes it easy to get the hash for the
 * board that results from making a move on a preceding board. Just find the features that change and XOR their numbers on the hash of the preceding board.
 * 
 * For Zobrist Hashing, linear independence is important. It would be useful if the random number generator used in this class provides numbers that have
 * a high minimum for the size of the subset of numbers that are not linearly independent
 * @author matthewslesinski
 */
public class ZobristHasher extends Hasher {

	private static final int NUMBER_OF_FEATURES = 781;
	private static final long[] FEATURE_VALUES = RANDOM_NUMBER_GENERATOR.get().generateNumbers(NUMBER_OF_FEATURES);
	private static final long STARTER_CODE = 0L;
	private static final int NUMBER_OF_POSSIBLE_PIECES = 12;
	private static final int PIECE_INDEX_BASE = 0;
	private static final int COLOR_BASE = NUMBER_OF_POSSIBLE_PIECES * Square.values().length + PIECE_INDEX_BASE;
	private static final int CASTLING_RIGHTS_BASE = COLOR_BASE + 1;
	private static final int EN_PASSANT_BASE = CASTLING_RIGHTS_BASE + CastlingRights.values().length;
	
	public ZobristHasher() {}
	
	/**
	 * Retrieves the feature with a given index in the array of values
	 * @param index The index to retrieve
	 * @return The random number for that feature
	 */
	private static long getFeatureWithIndex(int index) {
		return FEATURE_VALUES[index];
	}
	
	/**
	 * Gets the list of indices for the zobrist features that are present in a {@code Board}
	 * @param board The board to get the features for
	 * @return The {@code List}, where each entry is an index referring to a number in {@code FEATURE_VALUES}
	 */
	private static List<Integer> getPresentFeatures(Board board) {
		List<Integer> presentFeatures = new LinkedList<>();
		for (Square square : Square.values()) {
			Piece occupant = board.getPieceAtSquare(square);
			if (occupant != null && occupant != Piece.NONE) {
				addPieceAtSquareFeature(square, occupant, presentFeatures);
			}
		}
		if (board.whoseMove().isWhite()) {
			addColorFeature(presentFeatures);
		}
		for (CastlingRights right : CastlingRights.values()) {
			if (board.canCastle(right)) {
				addCastlingRightsFeature(right, presentFeatures);
			}
		}
		File enPassantFile = board.enPassantCaptureFile();
		if (enPassantFile != null) {
			addEnPassantFileFeature(enPassantFile, presentFeatures);
		}
		return presentFeatures;
	}
	
	/**
	 * Gets the indices for the features that are changed by the given {@code Move}
	 * @param transition The {@code Move}
	 * @return The {@code List} of the indices in {@code FEATURE_VALUES} for the features that would get changed when performing the {@code Move}
	 */
	private static List<Integer> getChangedFeatures(Move transition) {
		List<Integer> changedFeatures = new LinkedList<>();
		addPieceMovement(transition, changedFeatures);
		if (transition.isCapture()) {
			Square captureSquare = transition.getCaptureSquare();
			addPieceAtSquareFeature(captureSquare,
					Piece.getByColorAndType(transition.getMovingColor().getOtherColor(),transition.getCapturedPieceType()), changedFeatures);
		}
		if (transition.isCastle()) {
			addPieceAtSquareFeature(transition.getSecondaryStartSquareForCastling(), transition.getSecondaryMovingPieceForCastling(), changedFeatures);
			addPieceAtSquareFeature(transition.getSecondaryEndSquareForCastling(), transition.getSecondaryMovingPieceForCastling(), changedFeatures);
		}
		addColorFeature(changedFeatures);
		for (CastlingRights right : transition.newlyDisabledCastlingRights()) {
			addCastlingRightsFeature(right, changedFeatures);
		}
		if (transition.removesEnPassantPrivileges()) {
			addEnPassantFileFeature(transition.removedEnPassantFile(), changedFeatures);
		}
		if (transition.allowsEnPassant()) {
			addEnPassantFileFeature(transition.allowedEnPassantFile(), changedFeatures);
		}
		return changedFeatures;
	}
	
	/**
	 * Adds to the {@code List} the features changed specifically by the moving of the piece from one square to another
	 * @param transition The {@code Move} describing the movement, among other things
	 * @param presentFeatures The list of feature indices to add to
	 */
	private static void addPieceMovement(Move transition, List<Integer> presentFeatures) {
		addPieceAtSquareFeature(transition.getStartSquare(), transition.getMovingPiece(), presentFeatures);
		addPieceAtSquareFeature(transition.getDestinationSquare(), transition.getEndPiece(), presentFeatures);
	}
	
	/**
	 * Adds the feature associated with having a piece at the given square
	 * @param square The square with the piece
	 * @param occupant The piece
	 * @param presentFeatures The list of feature indices to add to
	 */
	private static void addPieceAtSquareFeature(Square square, Piece occupant, List<Integer> presentFeatures) {
		int index = square.getIndex() * NUMBER_OF_POSSIBLE_PIECES + occupant.getBitRepresentation() - 1 + PIECE_INDEX_BASE;
		presentFeatures.add(index);
	}
	
	/**
	 * Adds the feature associated with the change of color of the moving player
	 * @param presentFeatures The list of feature indices to add to
	 */
	private static void addColorFeature(List<Integer> presentFeatures) {
		presentFeatures.add(COLOR_BASE);
	}
	
	/**
	 * Adds the feature associated with the ability to castle a certain way
	 * @param right The way of castling
	 * @param presentFeatures The list of feature indices to add to
	 */
	private static void addCastlingRightsFeature(CastlingRights right, List<Integer> presentFeatures) {
		int index = right.getIndex() + CASTLING_RIGHTS_BASE;
		presentFeatures.add(index);
	}
	
	/**
	 * Adds the feature associated with the given {@code File} and en passant
	 * @param enPassantFile The en passant {@code File}
	 * @param presentFeatures The list of feature indices to add to
	 */
	private static void addEnPassantFileFeature(File enPassantFile, List<Integer> presentFeatures) {
		int index = enPassantFile.getIndex() + EN_PASSANT_BASE;
		presentFeatures.add(index);
	}
	
	
	@Override
	public long getHash(Board board) { 
		return getPresentFeatures(board).stream()
				.mapToLong(ZobristHasher::getFeatureWithIndex)
				.reduce(STARTER_CODE, UtilityFunctions::xor);
	}
	
	@Override
	public long getNextHash(Board previous, Move transition) {
		return getChangedFeatures(transition).stream()
				.mapToLong(ZobristHasher::getFeatureWithIndex)
				.reduce(previous.getHashCode(), UtilityFunctions::xor);
	}

}
