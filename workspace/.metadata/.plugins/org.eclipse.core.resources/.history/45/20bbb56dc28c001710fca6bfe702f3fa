package moves;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import boardFeatures.Square;
import lines.Direction;
import lines.File;
import lines.Rank;
import moveCalculationStructures.KingMoveSet;
import pieces.Piece;
import pieces.PieceType;
import representation.Board;
import support.UtilityFunctions;

/**
 * Preprocesses the board in a lazy way. For each square that might be attacked, it looks for potential attackers and records all of the relevant squares
 * they attack. It doesn't use any special data structures to preprocess threats though, so each time it looks for a new attacker or if movement is blocked,
 * it needs to recalculate that.
 * @author matthewslesinski
 *
 * @param <B>
 */
public class LazyPreProcessing<B extends Board> extends StraightforwardPreProcessing<B> {
	
	/** Records the squares of pieces that can possible be a pinner. The direction key is the direction the pin would be from (starting at the king's square) */
	private final Map<Direction, Square> possiblePinners;
	
	public LazyPreProcessing(B board) {
		super(board);
		possiblePinners = getListOfSquaresForPiecesOfColor(toMove.getOtherColor(), PieceType.getLineMovers())
			.stream().filter(this::canAttackKing)
			.collect(Collectors.toMap(kingSquare::getDirectionToSquare, UtilityFunctions::identity, kingSquare::whichIsCloser, () -> new EnumMap<>(Direction.class)));
			
	}

	/**
	 * Inches in a direction from curr until either some piece is found or we've hit the end, after passing the required number of pieces
	 * @param curr The {@code Square} to start the search from
	 * @param direction The {@code Direction} to search in
	 * @param hitTheEnd This tells us if we have reached the end of where we need to search.
	 * @param numToPass The number of squares with pieces on them that must be passed. Any non-null square returned from this method will have
	 * this many pieces in between it and the start square {@code curr}
	 * @return The {@code Square} that we've found, or null
	 */
	private Square getNextSquareWithPieceUnderConstraints(Square curr, Direction direction, Predicate<Square> hitTheEnd, int numToPass) {
		int passed = 0;
		while (!hitTheEnd.test(curr = curr.getNeighbor(direction))) {
			if (!isEmptySquare(curr) && passed++ == numToPass) {
				return curr;
			}
		}
		// If hitTheEnd checked for something other than null, it's possible curr is not null. Return it in case it's what we're looking for
		// if we've passed the right number of pieces
		return passed == numToPass ? curr : null;
	}
	
	/**
	 * Searches for the next {@code Square} with a piece, or null
	 * @param curr The {@code Square} to start searching from
	 * @param direction The {@code Direction} to search in
	 * @param hitTheEnd If we know we no longer need to search and can just end the search
	 * @return The {@code Square} with a piece on it, or null
	 */
	private Square getNextSquareWithPieceUnderConstraint(Square curr, Direction direction, Predicate<Square> hitTheEnd) {
		return getNextSquareWithPieceUnderConstraints(curr, direction, hitTheEnd, 0);
	}
	
	/**
	 * Checks each successive {@code Square} in the given {@code Direction} until a {@code Square} with a {@code Piece} is found or we fall off the board
	 * @param curr The {@code Square} to search from
	 * @param direction The {@code Direction} to search
	 * @return The next {@code Square} in that {@code Direction} with a {@code Piece}, or null if none
	 */
	private Square getNextSquareWithPiece(Square curr, Direction direction) {
		return getNextSquareWithPieceUnderConstraint(curr, direction, UtilityFunctions::isNull);
	}
	
	@Override
	protected void calculateSquareAttackers(Square potentiallyAttackedSquare, KingMoveSet kingMoves,
			Set<Square> coveredAttackers, Set<Square> squaresToIgnore) {
		/*
		 * The way this method works is as follows: We're given a square that the king is on or could potentially move to (potentiallyAttackedSquare).
		 * If it's attacked by a knight, we won't get here. If it's got a piece of the moving side's color, we ignore it because the king can't move there.
		 * We look in all directions for a potential attacker. If we find a piece there are three cases:
		 * Case 1: It is an enemy attacker or friend that we have seen. In this case, do nothing
		 * Case 2: It is a friend. In this case we check for pins by seeing if the next piece in the direction from the king is one of the possible pinners
		 * Case 3: It is an enemy attacker that we haven't seen. In this case, we want to record all of its attacks on any of the squares the king can move
		 * to or is on. Furthermore, if it's a long range piece, we keep track of a map, unblockedDirections, to figure out if the attacking piece is
		 * blocked in particular directions. We enumerate through all the squares the king can move to or is on that the attacker can attack, and if they're
		 * not blocked we add them to the set of squares attacking that particular square. Furthermore, we keep track of if the initial potentiallyAttackedSquare
		 * is attacked, and if it is, we stop looking in other directions for potential attackers
		 */
		// Keeps track of if the initial potentiallyAttackedSquare is atacked
		boolean squareIsAttacked = false;
		Piece potentiallyAttackedOccupant = getPieceAtSquare(potentiallyAttackedSquare);
		// If the attacked square houses a friend, we won't be able to move there anyway, so ignore it
		if (isNotSameColor(potentiallyAttackedOccupant)) {
			for (Direction direction : Direction.getOutwardDirections()) {
				Square nextWithOccupant = getNextSquareWithPiece(potentiallyAttackedSquare, direction);
				Piece occupant = getPieceAtSquare(nextWithOccupant);
				if (!isNotAPiece(occupant) || coveredAttackers.contains(nextWithOccupant)) {
					continue;
				}
				// If the next along the line is an enemy we haven't seen yet, find the squares it attacks
				if (occupant.getColor() != toMove) {
					coveredAttackers.add(nextWithOccupant);
					boolean lineMover = occupant.getType().isLongRange();
					Map<Direction, Boolean> unblockedDirections = null;
					// If the attacker is a long range piece, keep track of the directions that are unblocked. Also, record the current direction as unblocked
					if (lineMover) {
						unblockedDirections = new EnumMap<>(Direction.class);
						unblockedDirections.put(nextWithOccupant.getDirectionToSquare(potentiallyAttackedSquare), true);
					}
					// For each square around the king that the attacker could attack
					for (Square attackedSquare : kingMoves.getAttackedSquares(nextWithOccupant, this::getPieceAtSquare)) {
						// Check if the attack is blocked and record that in the map
						Direction toAttackedSquare = nextWithOccupant.getDirectionToSquare(attackedSquare);
						if (lineMover && !unblockedDirections.containsKey(toAttackedSquare)) {
							unblockedDirections.put(toAttackedSquare, !isMovementBlocked(nextWithOccupant, attackedSquare));
						}
						if (!lineMover || unblockedDirections.get(toAttackedSquare)) {
							// record if this attack is on the poteniallyAttackedSquare
							if (attackedSquare == potentiallyAttackedSquare) {
								squareIsAttacked = true;
							}
							Set<Square> attackersOfSquare = attackedSquaresAroundKing.getOrDefault(attackedSquare, EnumSet.noneOf(Square.class));
							attackedSquaresAroundKing.putIfAbsent(attackedSquare, attackersOfSquare);
							attackersOfSquare.add(nextWithOccupant);
						}
					}
					// We don't need to keep looking in more directions for attackers because we already found one, unless we're looking at
					// the king's square, in which case we want to know all the attackers.
					if (squareIsAttacked && potentiallyAttackedSquare != kingSquare) {
						return;
					}
				// If the found piece is a friend, figure out if it's pinned
				} else {
					Direction fromKing = kingSquare.getDirectionToSquare(nextWithOccupant);
					// If the direction from the king is the same we're looking at, and there's a possible piece to pin nextWithOccupant, and it's the
					// next in the line from the king, the piece is pinned, and record it
					if (fromKing == direction && possiblePinners.containsKey(fromKing) &&
							getNextSquareWithPiece(nextWithOccupant, fromKing) == possiblePinners.get(fromKing)) {
						coveredAttackers.add(nextWithOccupant);
						pins.put(nextWithOccupant, fromKing);
					}
				}
			}
		}
	}
	
	@Override
	protected void calculateKingAttackers(Set<Square> coveredAttackers) {
		// Nothing needs to be done here that wasn't done in calculateSquareAttackers
		return;
	}

	@Override
	public boolean isMovementBlocked(Square start, Square end) {
		Direction dir = end.getDirectionToSquare(start);
		if (dir == Direction.NONE) {
			return false;
		}
		return getNextSquareWithPieceUnderConstraint(end, dir, ((Predicate<Square>) this::isEmptySquare).negate().or(Predicate.isEqual(start))) != start;
	}

	@Override
	public boolean isEnPassantPinned(File movingPawnFile) {
		File captureFile = getEnPassantFile();
		// If en passant isn't allowed, it can't be pinned
		if (captureFile == null) {
			return false;
		}
		Rank enPassantRank = toMove.getEnPassantCaptureRank();
		// The king must be on the same rank for this special pin
		if (kingSquare.getRank() != enPassantRank) {
			return false;
		}
		Square pawnSquare = Square.getByFileAndRank(captureFile, enPassantRank);
		// Get the possible rooks or queens that might pin the pawn, which means they're the closest one in the same direction from the king as the pawn
		Direction fromKing = kingSquare.getDirectionToSquare(pawnSquare);
		Optional<Square> attackingHorizontalMoverOptional = getListOfSquaresForPiecesOfColor(toMove.getOtherColor(), PieceType.getHorizontalMovers()).stream()
			.filter(square -> kingSquare.getDirectionToSquare(square) == fromKing).reduce(kingSquare::whichIsCloser);
		// If there's no possible pinning rook/queen, the en passant isn't pinned
		if (!attackingHorizontalMoverOptional.isPresent()) {
			return false;
		}
		Square pinningPiece = attackingHorizontalMoverOptional.get();
		// If the pinning piece is closer than the en passant pawn, en passant can't be pinned
		if (kingSquare.whichIsCloser(pinningPiece, pawnSquare) == pinningPiece) {
			return false;
		}
		Square curr = kingSquare;
		// This will get the next square, with 2 in between, from the king in the given direction, such that the returned
		// square has some piece on it, and is no further than the pinning piece's square.
		return getNextSquareWithPieceUnderConstraints(curr, fromKing, Predicate.isEqual(pinningPiece), 2) == pinningPiece;

	}
}
