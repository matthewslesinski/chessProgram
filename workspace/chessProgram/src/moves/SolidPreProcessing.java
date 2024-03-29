package moves;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import boardFeatures.Square;
import independentDataStructures.EnumSequence;
import independentDataStructures.FixedOrderingSet;
import lines.Direction;
import lines.File;
import lines.Line;
import lines.LineType;
import lines.Rank;
import moveCalculationStructures.KingMoveSet;
import pieces.Piece;
import pieces.PieceType;
import representation.Board;
import support.BadArgumentException;

/**
 * Used to preprocess a board before calculating the moves, so that calculating them is easier. The main benefits of this class
 * are that it can provide the basic info about the board, such as which piece is on a square, the player to move, castling abilities,
 * and en passant abilities, but it can also list all the squares a piece of particular type and color is on, as well as efficiently
 * retrieving information about which king move is safe, whether the king is in check, and if pieces are pinned
 * @author matthewslesinski
 *
 */
@Deprecated
public class SolidPreProcessing<B extends Board> extends StraightforwardPreProcessing<B> {
	
	/** The values of the values of this map are the preprocessed lists of just the squares with pieces on them for each line */
	private final Map<LineType, Map<Line, FixedOrderingSet<Square>>> preProcessedLines = new EnumMap<>(LineType.class);
		
	/**
	 * Initiates the preprocessing
	 * @param board The board to decompress
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SolidPreProcessing(B board) {
		super(board);
		for (LineType type : LineType.values()) {
			preProcessedLines.put(type, new EnumMap(type.getType()));
		}
		initializeLines();
	}
	
	/**
	 * Gets the inner map with the preprocessed {@code Line}s for a particular {@code LineType}
	 * @param type The {@code LineType}
	 * @return The map of each {@code Line} of that type to the preprocessed version of that {@code Line}, or null otherwise
	 */
	private Map<Line, FixedOrderingSet<Square>> retrievePreProcessingForType(LineType type) {
		return preProcessedLines.get(type);
	}

	/**
	 * Initializes the preprocessing for a line and adds all the pieces in that line to the preprocessing list
	 * @param line The line
	 * @param arr The array to put the preprocessing in for storage
	 */
	private void preProcessLine(Line line, Map<Line, FixedOrderingSet<Square>> map) {
		FixedOrderingSet<Square> list = new EnumSequence<>(Square.class, line.getLength(),
										square -> square.getNeighbor(line.getForwardDirection().getOppositeDirection()), line::containsSquare);
		line.getContainedSquares().forEach(square -> {
			Piece piece = getPieceAtSquare(square);
			if (!isNotAPiece(piece)) {
				list.add(square);
			}
		});
		map.put(line, list);
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
	
	/**
	 * Retrieves the pre processed line containing a square and covering a line of a particular type
	 * @param containedSquare The square contained in the line
	 * @param lineType The type of line
	 * @return The pre processing for that line
	 */
	private FixedOrderingSet<Square> getListOfImportantSquares(Square containedSquare, LineType lineType) {
		Line line = lineType.getLineBySquare(containedSquare);
		return retrievePreProcessingForType(lineType).get(line);
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
			if (isNotAPiece(occupant)) {
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
	
	@Override
	protected void calculateSquareAttackers(Square potentiallyAttackedSquare, KingMoveSet kingMoves, Set<Square> coveredAttackers, Set<Square> squaresToIgnore) {
		boolean squareIsAttacked = false;
		for (Direction dir : Direction.getOutwardDirections()) {
			Square neighbor = getNextSquareWithPiece(potentiallyAttackedSquare, dir, squaresToIgnore);
			if (neighbor != null && isThreat(neighbor) && !coveredAttackers.contains(neighbor)) {
				coveredAttackers.add(neighbor);
				for (Square attackedSquare : kingMoves.getAttackedSquares(neighbor, this::getPieceAtSquare)) {
					if (attackedSquare == potentiallyAttackedSquare) {
						squareIsAttacked = true;
					}
					Set<Square> attackersOfSquare = attackedSquaresAroundKing.getOrDefault(attackedSquare, EnumSet.noneOf(Square.class));
					attackedSquaresAroundKing.putIfAbsent(attackedSquare, attackersOfSquare);
					attackersOfSquare.add(neighbor);
				}
				if (squareIsAttacked) {
					return;
				}
			}
		}
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
			Set<Square> kingAttackers = attackedSquaresAroundKing.getOrDefault(kingSquare, EnumSet.noneOf(Square.class));
			attackedSquaresAroundKing.putIfAbsent(kingSquare, kingAttackers);
			kingAttackers.add(threat);
		}
	}
	
	@Override
	protected void calculateKingAttackers(Set<Square> coveredAttackers) {
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
