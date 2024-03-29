package moves;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import boardFeatures.Square;
import gamePlaying.Color;
import lines.Direction;
import lines.File;
import moveCalculationStructures.KingMoveSet;
import pieces.Piece;
import pieces.PieceType;
import representation.Board;
import representation.CastlingRights;
import support.BadArgumentException;
import support.BadBoardException;
import support.UtilityFunctions;

import static support.UtilityFunctions.*;

public abstract class StraightforwardPreProcessing<B extends Board> implements ProcessedBoard<B> {

	/** The {@code Board} this preprocessing is for */
	protected final B originalBoard;
	
	/** The color to move */
	protected final Color toMove;
	
	/** The opposite color of the one to move */
	protected final Color oppositeColor;
	
	/** Array of the booleans for each castling right, for if it's allowed without accounting for specifics of the position */
	protected final Map<CastlingRights, Boolean> castlingRights = new EnumMap<>(CastlingRights.class);
	
	/** The file to capture onto with en passant, or null if not allowed */
	protected final File enPassantFile;
	
	/** Stores which {@code Piece} is at which {@code Square} */
	protected final Map<Square, Piece> pieces = new EnumMap<>(Square.class);
	
	/**
	 * Stores the pieces on the board in another intuitive arrangement. Each index of the array
	 * will be the list with the squares that the piece with that ordinal (minus 1 because Piece.NONE is not included) is on
	 */
	protected final Map<Piece, List<Square>> piecesToSquares = new EnumMap<>(Piece.class);
	
	/** The square with the king of the player to move */
	protected final Square kingSquare;
	
	/** For each {@code Square} around the king, this holds the {@code Square}s of the pieces attacking it */
	protected final Map<Square, Set<Square>> attackedSquaresAroundKing = new EnumMap<>(Square.class);
	
	/** For each {@code Square} with a {@code Piece} that is pinned, this holds the {@code Direction} from that {@code Square} to the attacking {@code Piece}'s {@code Square} */
	protected final Map<Square, Direction> pins = new EnumMap<>(Square.class);
	
	/** This holds the {@code Square}s the king can safely move to. It is lazily instantiated */
	protected Set<Square> safeKingDestinations = null;
	
	/**
	 * Initiates the preprocessing
	 * @param board The board to decompress
	 */
	public StraightforwardPreProcessing(B board) {
		this.originalBoard = board;
		toMove = board.whoseMove();
		oppositeColor = toMove.getOtherColor();
		for (CastlingRights right : CastlingRights.values()) {
			castlingRights.put(right, board.canCastle(right));
		}
		enPassantFile = board.enPassantCaptureFile();
		initializeLists(piecesToSquares, () -> new LinkedList<>(), Piece.realPieces());
		parseBoard(board);
		try {
			kingSquare = getListOfSquaresForPiece(Piece.getByColorAndType(toMove, PieceType.KING)).get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new BadBoardException(board, "No king");
		}
	}
	
	/**
	 * Initializes all list in the appropriate array, using the index of the array that each list will be in
	 * @param arr The array to put the lists in
	 * @param constructor How to initialize each list
	 */
	private static <S, T> void initializeLists(Map<S, T> map, Supplier<T> constructor, S[] keys) {
		for (S key : keys) {
			map.put(key, constructor.get());
		}
	}
	
	/**
	 * Parses a board into a state where it is easy to see which piece is on which square and also which squares have particular pieces
	 * @param board The board to parse
	 */
	protected void parseBoard(B board) {
		for (Square square : Square.values()) {
			Piece piece = board.getPieceAtSquare(square);
			if (!isNotAPiece(piece)) {
				pieces.put(square, piece);
				piecesToSquares.get(piece).add(square);
			}
		}
	}
	
	/**
	 * Returns the squares that contain any of the pieces of the types specified and the color specified
	 * @param color The color for the pieces
	 * @param types The types they can be
	 * @return All the squares containing one of them
	 */
	protected List<Square> getListOfSquaresForPiecesOfColor(Color color, PieceType... types) {
		return Arrays.stream(types)
			.map(bind(Piece::getByColorAndType, color).andThen(this::getListOfSquaresForPiece))
			.reduce(Collections.emptyList(), UtilityFunctions::concat);
	}
	
	/**
	 * Determines if the piece at a square can attack the target square, assuming nothing in the way
	 * @param possibleAttacker The possible square with the attacker
	 * @return true iff it could
	 */
	protected boolean canEverAttackSquare(Square possibleAttacker, Square target) {
		return possibleAttacker.getPossibleThreatsByPiece(getPieceAtSquare(possibleAttacker)).contains(target);
	}
	
	/**
	 * Determines if the piece at a square is of the opposite color from the current player
	 * @param potentialAttacker The square of the potential attacking piece to determine if its color is threatening
	 * @return true iff it is
	 */
	protected boolean isThreat(Square potentialAttacker) {
		Piece attacker = getPieceAtSquare(potentialAttacker);
		return !isNotAPiece(attacker) && attacker.getColor() == oppositeColor;
	}
	
	/**
	 * Determines if the piece at a square can attack the king's square, assuming nothing in the way
	 * @param possibleAttacker The possible square with the attacker
	 * @return true iff it could
	 */
	protected boolean canAttackKing(Square possibleAttacker) {
		return canEverAttackSquare(possibleAttacker, kingSquare);
	}
	
	/**
	 * Records all of the attacks of the enemy knights on the king and his surrounding squares
	 * @param kingMoves The set of squares around the king, including his square
	 * @param coveredAttackers The set of squares of attackers that have already been looked at
	 */
	protected void calculateKnightAttacks(KingMoveSet kingMoves, Set<Square> coveredAttackers) {
		Piece opposingKnight = Piece.getByColorAndType(oppositeColor, PieceType.KNIGHT);
		for (Square knightSquare : piecesToSquares.get(opposingKnight)) {
			coveredAttackers.add(knightSquare);
			for (Square attackedSquare : kingMoves.getAttackedSquares(knightSquare, this::getPieceAtSquare)) {
				Set<Square> attackers = attackedSquaresAroundKing.getOrDefault(attackedSquare, EnumSet.noneOf(Square.class));
				attackers.add(knightSquare);
				attackedSquaresAroundKing.putIfAbsent(attackedSquare, attackers);
			}
		}
	}
	
	/**
	 * Finds the rest of the attackers not yet discovered who attack the king, and also records pins
	 * @param coveredAttackers Attackers that have already been discovered
	 */
	protected abstract void calculateKingAttackers(Set<Square> coveredAttackers);
	
	/**
	 * Given a square around the king, this method finds a threat from any direction, by determining the nearest piece in that direction and determining if it's
	 * threatening, and for each of that threatening piece's threats near the king, the threatened square near the king records that threatening square as an attacker.
	 * Once the currently inspected square has been attacked, this method returns, since we only care if the square's attacked at least once.
	 * @param potentiallyAttackedSquare The square to inspect
	 * @param kingMoves The set of squares around the king, in a structure that provides utility methods for finding just the squares in the set that are threatened
	 * by a particular piece
	 * @param coveredAttackers All the threats that have already been discovered and don't need to be looked at again
	 * @param squaresToIgnore Squares to not consider as blocking to threats along a line possibly containing those squares
	 */
	protected abstract void calculateSquareAttackers(Square potentiallyAttackedSquare, KingMoveSet kingMoves, Set<Square> coveredAttackers, Set<Square> squaresToIgnore);
	
	@Override
	public Square getKingSquare() {
		return kingSquare;
	}
	
	@Override
	public B getOriginalBoard() {
		return this.originalBoard;
	}
	
	@Override
	public Color whoseMove() {
		return toMove;
	}

	@Override
	public boolean canCastle(CastlingRights right) {
		return castlingRights.getOrDefault(right, false);
	}

	@Override
	public File getEnPassantFile() {
		return enPassantFile;
	}

	@Override
	public List<Square> getListOfSquaresForPiece(Piece piece) {
		if (piece == Piece.NONE) {
			throw new BadArgumentException(piece, Piece.class, "An absent piece cannot be associated with a square");
		}
		return piecesToSquares.get(piece);
	}

	@Override
	public Piece getPieceAtSquare(Square square) {
		return pieces.getOrDefault(square, Piece.NONE);
	}
	

	@Override
	public void calculateKingSafety() {
		// Set up
		KingMoveSet kingMoves = kingSquare.getKingMoves(toMove);
		Piece king = getPieceAtSquare(kingSquare);
		pieces.put(kingSquare, Piece.NONE);
		Set<Square> squareToIgnore = Collections.singleton(kingSquare);
		Set<Square> coveredAttackers = EnumSet.noneOf(Square.class);
		// Calculate
		calculateKnightAttacks(kingMoves, coveredAttackers);
		for (Square square : kingMoves) {
			if (attackedSquaresAroundKing.get(square) == null) {
				calculateSquareAttackers(square, kingMoves, coveredAttackers, squareToIgnore);
			}
		}
		calculateKingAttackers(coveredAttackers);
		// reset the board
		pieces.put(kingSquare, king);
	}

	@Override
	public Set<Square> whoIsAttackingTheKing() {
		return attackedSquaresAroundKing.getOrDefault(kingSquare, Collections.emptySet());
	}

	@Override
	public Set<Square> getSafeKingDestinations() {
		if (safeKingDestinations == null) {
			safeKingDestinations = kingSquare.getKingMoves(toMove).stream()
				.filter(square -> attackedSquaresAroundKing.get(square) == null)
				.collect(Collectors.toCollection(() -> EnumSet.noneOf(Square.class)));
		}
		return safeKingDestinations;
	}
	
	@Override
	public Direction isPiecePinned(Square square) {
		return pins.getOrDefault(square, Direction.NONE);
	}
}
