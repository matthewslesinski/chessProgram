package moves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import boardFeatures.Direction;
import boardFeatures.DownRightDiagonal;
import boardFeatures.File;
import boardFeatures.Line;
import boardFeatures.LineType;
import boardFeatures.Rank;
import boardFeatures.Square;
import boardFeatures.UpRightDiagonal;
import dataStructures.EnumSequence;
import dataStructures.FixedOrderingSet;
import dataStructures.KingMoveSet;
import gamePlaying.Color;
import pieces.Piece;
import pieces.PieceType;
import representation.Board;
import representation.CastlingRights;
import support.BadArgumentException;
import support.UtilityFunctions;

/**
 * Used to preprocess a board before calculating the moves, so that calculating them is easier. The main benefits of this class
 * are that it can provide the basic info about the board, such as which piece is on a square, the player to move, castling abilities,
 * and en passant abilities, but it can also list all the squares a piece of particular type and color is on, as well as efficiently
 * retrieving information about which king move is safe, whether the king is in check, and if pieces are pinned
 * @author matthewslesinski
 *
 */
public class SolidPreProcessing<B extends Board> implements ProcessedBoard<B> {
	
	/** The color to move */
	private final Color toMove;
	
	/** The opposite color of the one to move */
	private final Color oppositeColor;
		
	/** Array of the booleans for each castling right, for if it's allowed without accounting for specifics of the position */
	private final Map<CastlingRights, Boolean> castlingRights = new EnumMap<>(CastlingRights.class);
	
	/** The file to capture onto with en passant, or null if not allowed */
	private final File enPassantFile;
	
	/** Stores the pieces on the board in the most intuitive arrangement. Each index of the array will be the piece at the square with that index as its index */
	private final Piece[] pieces = new Piece[Square.values().length];
	
	/**
	 * Stores the pieces on the board in another intuitive arrangement. Each index of the array
	 * will be the list with the squares that the piece with that ordinal (minus 1 because Piece.NONE is not included) is on
	 */
	private final Map<Piece, List<Square>> piecesToSquares = new EnumMap<>(Piece.class);
	
	/** The four following arrays of lists contain the squares with pieces on them for each of the instances of a line type */
	@SuppressWarnings("rawtypes")
	private final FixedOrderingSet[][] preProcessedLines = {
		new FixedOrderingSet[File.values().length],	
		new FixedOrderingSet[Rank.values().length],
		new FixedOrderingSet[UpRightDiagonal.values().length],														
		new FixedOrderingSet[DownRightDiagonal.values().length]
	};
	
	private final Square kingSquare;
	
	private final Map<Square, Set<Square>> safeSquaresAroundKing = new EnumMap<>(Square.class);
	
	private final Map<Square, Direction> pins = new EnumMap<>(Square.class);
	
	
	/**
	 * Initiates the preprocessing
	 * @param board The board to decompress
	 */
	public SolidPreProcessing(B board) {
		toMove = board.whoseMove();
		oppositeColor = toMove.getOtherColor();
		for (CastlingRights right : CastlingRights.values()) {
			castlingRights.put(right, board.canCastle(right));
		}
		enPassantFile = board.enPassantCaptureFile();
		
		initializeLists(piecesToSquares, () -> (List<Square>) new ArrayList<Square>(10), Piece.realPieces());
		parseBoard(board);
		initializeLines();
		kingSquare = getListOfSquaresForPiece(Piece.getByColorAndType(toMove, PieceType.KING)).get(0);
		
	}
	
	@SuppressWarnings("unchecked")
	private FixedOrderingSet<Square>[] retrievePreProcessingForType(LineType type) {
		return UtilityFunctions.getValueFromArray(preProcessedLines, type);
	}
	
	/**
	 * Parses a board into a state where it is easy to see which piece is on which square and also which squares have particular pieces
	 * @param board The board to parse
	 */
	private void parseBoard(B board) {
		for (Square square : Square.values()) {
			Piece piece = board.getPieceAtSquare(square);
			if (piece != null && piece != Piece.NONE) {
				pieces[square.getIndex()] = piece;
				piecesToSquares.get(piece).add(square);
			}
		}
	}
	
	/**
	 * Initializes all list in the appropriate array, using the index of the array that each list will be in
	 * @param arr The array to put the lists in
	 * @param constructor How to initialize each list
	 */
	private <S, T> void initializeLists(Map<S, T> map, Supplier<T> constructor, S[] keys) {
		for (S key : keys) {
			map.put(key, constructor.get());
		}
	}
	
	
	
	@Override
	public Square getKingSquare() {
		return kingSquare;
	}

	/**
	 * Initializes the preprocessing for a line and adds all the pieces in that line to the preprocessing list
	 * @param line The line
	 * @param arr The array to put the preprocessing in for storage
	 */
	private void preProcessLine(Line line, FixedOrderingSet<Square>[] arr) {
		FixedOrderingSet<Square> list = new EnumSequence<Square>(Square.class, line.getLength(),
										square -> square.getNeighbor(line.getForwardDirection().getOppositeDirection()), line::containsSquare);
		line.getContainedSquares().forEach(square -> {
			Piece piece = square.getValueOfSquareInArray(pieces);
			if (piece != null && piece != Piece.NONE) {
				list.add(square);
			}
		});
		arr[line.getIndex()] = list;
	}
	
	/**
	 * Performs the preprocessing
	 */
	private void initializeLines() {
		for (PieceType lineMover: PieceType.getLineMovers()) {
			Piece relevantPiece = Piece.getByColorAndType(toMove.getOtherColor(), lineMover);
			List<Square> relevantSquares = getListOfSquaresForPiece(relevantPiece);
			relevantSquares.forEach(square -> {
				for (LineType type : LineType.values()) {
					preProcessLine(type.getLineBySquare(square), retrievePreProcessingForType(type));
				}		
			});
		}
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
		if (square == null) {
			return null;
		}
		return square.getValueOfSquareInArray(pieces);
	}
	
	/**
	 * Retrieves the pre processed line containing a square and covering a line of a particular type
	 * @param containedSquare The square contained in the line
	 * @param lineType The type of line
	 * @return The pre processing for that line
	 */
	private FixedOrderingSet<Square> getListOfImportantSquares(Square containedSquare, LineType lineType) {
		int indexOfPreProcessing = lineType.getLineBySquare(containedSquare).getIndex();
		return retrievePreProcessingForType(lineType)[indexOfPreProcessing];
	}
	
	/**
	 * Retrieves the pre processed line containing a square and that extends in a particular direction and/or its opposite
	 * @param square The square in the line
	 * @param dir The direction from the square
	 * @return The preprocessed line containing it, or null
	 */
	private FixedOrderingSet<Square> getListOfImportantSquaresByDirection(Square square, Direction dir) {
		LineType lineType = dir.getContainingLineType();
		return getListOfImportantSquares(square, lineType);
	}
	
	/**
	 * Retrieves the piece that is n pieces away from the current square in a direction
	 * @param square The current square
	 * @param dir The direction to look
	 * @param n How many pieces away
	 * @return The square containing the piece or null
	 */
	public Square getNPiecesAway(Square square, Direction dir, int n) {
		if (n < 0) {
			throw new BadArgumentException(n, Integer.class, "Steps away can't be negative");
		}
		int sign = dir.getMovement().getIncrement();
		int numberOfSteps = n * sign;
		LineType lineType = dir.getContainingLineType();
		FixedOrderingSet<Square> listOfSquares = getListOfImportantSquares(square, lineType);
		if (listOfSquares == null) {
			Square possibleSquare = square.getNeighbor(dir, n);
			Piece occupant = getPieceAtSquare(possibleSquare);
			if (occupant == null || occupant == Piece.NONE) {
				return null;
			}
			return possibleSquare;
		}
		return listOfSquares.retrieveOffsetFromElement(square, numberOfSteps);
	}

	/**
	 * Gets the square with a piece on it that is next along the direction
	 * @param square The current square
	 * @param dir The direction to travel
	 * @return The {@code Square}, or null if none
	 */
	public Square getNextSquareWithPiece(Square square, Direction dir) {
		return getNPiecesAway(square, dir, 1);
	}
	
	/**
	 * Gets the square with a piece on it that is next along the direction
	 * @param square The current square
	 * @param dir The direction to travel
	 * @param squaresToExclude The set of squares to gloss over and ignore
	 * @return The {@code Square}, or null if none
	 */
	public Square getNextSquareWithPiece(Square square, Direction dir, Set<Square> squaresToExclude) {
		do {
			square = getNextSquareWithPiece(square, dir);
		} while (squaresToExclude.contains(square));
		return square;
	}
	
	/**
	 * Records all of the attacks of the enemy knights on the king and his surrounding squares
	 * @param kingMoves The set of squares around the king, including his square
	 * @param coveredAttackers The set of squares of attackers that have already been looked at
	 */
	private void calculateKnightAttacks(KingMoveSet kingMoves, Set<Square> coveredAttackers) {
		Piece opposingKnight = Piece.getByColorAndType(oppositeColor, PieceType.KNIGHT);
		for (Square knightSquare : piecesToSquares.get(opposingKnight)) {
			coveredAttackers.add(knightSquare);
			for (Square attackedSquare : kingMoves.getAttackedSquares(knightSquare, this::getPieceAtSquare)) {
				Set<Square> attackers = safeSquaresAroundKing.getOrDefault(attackedSquare, EnumSet.noneOf(Square.class));
				attackers.add(knightSquare);
				safeSquaresAroundKing.putIfAbsent(attackedSquare, attackers);
			}
		}
	}
	
	/**
	 * Determines if the piece at a square can attack the target square, assuming nothing in the way
	 * @param possibleAttacker The possible square with the attacker
	 * @return true iff it could
	 */
	private boolean canAttackSquare(Square possibleAttacker, Square target) {
		return possibleAttacker.getPossibleThreatsByPiece(getPieceAtSquare(possibleAttacker)).contains(target);
	}
	
	/**
	 * Determines if the piece at a square is of the opposite color from the current player
	 * @param potentialAttacker The square of the potential attacking piece to determine if its color is threatening
	 * @return true iff it is
	 */
	private boolean isThreat(Square potentialAttacker) {
		Piece attacker = getPieceAtSquare(potentialAttacker);
		return attacker != null && attacker.getColor() == oppositeColor;
	}
	
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
	private void calculateSquareAttackers(Square potentiallyAttackedSquare, KingMoveSet kingMoves, Set<Square> coveredAttackers, Set<Square> squaresToIgnore) {
		boolean squareIsAttacked = false;
		for (Direction dir : Direction.getOutwardDirections()) {
			Square neighbor = getNextSquareWithPiece(potentiallyAttackedSquare, dir, squaresToIgnore);
			if (neighbor != null && isThreat(neighbor) && !coveredAttackers.contains(neighbor)) {
				coveredAttackers.add(neighbor);
				for (Square attackedSquare : kingMoves.getAttackedSquares(neighbor, this::getPieceAtSquare)) {
					if (attackedSquare == potentiallyAttackedSquare) {
						squareIsAttacked = true;
					}
					Set<Square> attackersOfSquare = safeSquaresAroundKing.getOrDefault(attackedSquare, EnumSet.noneOf(Square.class));
					safeSquaresAroundKing.putIfAbsent(attackedSquare, attackersOfSquare);
					attackersOfSquare.add(neighbor);
				}
				if (squareIsAttacked) {
					return;
				}
			}
		}
	}
	
	/**
	 * Determines if the piece at a square can attack the king's square, assuming nothing in the way
	 * @param possibleAttacker The possible square with the attacker
	 * @return true iff it could
	 */
	private boolean canAttackKing(Square possibleAttacker) {
		return canAttackSquare(possibleAttacker, kingSquare);
	}
	
	
	/**
	 * Determines if the square is pinned to the king
	 * @param possiblePinnedPiece The square to consider
	 * @param listOfSquares The sequence of pieces including the king and the possibly pinned piece
	 */
	private void checkForPin(Square possiblePinnedPiece, FixedOrderingSet<Square> listOfSquares) {
		Direction fromKing = kingSquare.getDirectionToSquare(possiblePinnedPiece);
		Square pinner = listOfSquares.getThirdInSequence(kingSquare, possiblePinnedPiece);
		if (pinner != null && isThreat(pinner) && canAttackKing(pinner)) {
			pins.put(possiblePinnedPiece, fromKing);
		}
	}
	
	/**
	 * Determines if a threat can attack the king, and if so, records it as a check
	 * @param threat The possible threat
	 * @param coveredAttackers The set of squares that have already been accounted for
	 */
	private void recordThreat(Square threat, Set<Square> coveredAttackers) {
		if (!coveredAttackers.contains(threat) && canAttackKing(threat)) {
			Set<Square> kingAttackers = safeSquaresAroundKing.getOrDefault(kingSquare, EnumSet.noneOf(Square.class));
			safeSquaresAroundKing.putIfAbsent(kingSquare, kingAttackers);
			kingAttackers.add(threat);
		}
	}
	
	/**
	 * Finds the rest of the attackers not yet discovered who attack the king, and also records pins
	 * @param coveredAttackers Attackers that have already been discovered
	 */
	private void calculateKingAttackers(Set<Square> coveredAttackers) {
		for (Direction dir : Direction.getOutwardDirections()) {
			Square neighbor = getNextSquareWithPiece(kingSquare, dir);
			if (neighbor == null) {
				continue;
			}
			if (isThreat(neighbor)) {
				recordThreat(neighbor, coveredAttackers);
			} else {
				FixedOrderingSet<Square> listOfSquares = getListOfImportantSquares(kingSquare, dir.getContainingLineType());
				if (listOfSquares != null) {
					checkForPin(neighbor, listOfSquares);
				}
			}
		}
	}
	
	@Override
	public void calculateKingSafety() {
		// Set up
		KingMoveSet kingMoves = kingSquare.getKingMoves(toMove);
		Piece king = pieces[kingSquare.getIndex()];
		pieces[kingSquare.getIndex()] = Piece.NONE;
		Set<Square> squareToIgnore = Collections.singleton(kingSquare);
		Set<Square> coveredAttackers = EnumSet.noneOf(Square.class);
		// Calculate
		calculateKnightAttacks(kingMoves, coveredAttackers);
		for (Square square : kingMoves) {
			if (safeSquaresAroundKing.get(square) == null) {
				calculateSquareAttackers(square, kingMoves, coveredAttackers, squareToIgnore);
			}
		}
		calculateKingAttackers(coveredAttackers);
		// reset the board
		pieces[kingSquare.getIndex()] = king;
	}
	
	@Override
	public Set<Square> whoIsAttackingTheKing() {
		return safeSquaresAroundKing.getOrDefault(kingSquare, Collections.emptySet());
	}

	@Override
	public Set<Square> getSafeKingDestinations() {
		return kingSquare.getKingMoves(toMove).stream()
				.filter(square -> safeSquaresAroundKing.get(square) == null)
				.collect(Collectors.toCollection(() -> EnumSet.noneOf(Square.class)));
	}

	
	@Override
	public boolean isMovementBlocked(Square start, Square end) {
		Direction dir = end.getDirectionToSquare(start);
		if (dir == Direction.NONE) {
			return false;
		}
		FixedOrderingSet<Square> listOfSquares = getListOfImportantSquaresByDirection(start, dir);
		if (listOfSquares == null) {
			Square intermediate = end;
			while ((intermediate = intermediate.getNeighbor(dir)) != start) {
				if (!isEmptySquare(intermediate)) {
					return true;
				}
			}
			return false;
		}
		return listOfSquares.retrieveOffsetFromElement(end, dir.getMovement().getIncrement()) != start;
	}
	
	@Override
	public Direction isPiecePinned(Square square) {
		return pins.getOrDefault(square, Direction.NONE);
	}

	@Override
	public boolean isEnPassantPinned(File movingPawnFile) {
		File captureFile = getEnPassantFile();
		if (captureFile == null) {
			return false;
		}
		Rank enPassantRank = toMove.getEnPassantCaptureRank();
		if (kingSquare.getRank() != enPassantRank) {
			return false;
		}
		FixedOrderingSet<Square> listOfSquares = getListOfImportantSquares(kingSquare, LineType.RANK);
		if (listOfSquares == null) {
			return false;
		}
		Square pawnSquare = Square.getByFileAndRank(captureFile, enPassantRank);
		Square enemy = listOfSquares.retrieveOffsetFromElement(kingSquare, 3);
		return enemy != null && isThreat(enemy) && canAttackKing(enemy) && kingSquare.whichIsCloser(pawnSquare, enemy) == pawnSquare;
	}

}
