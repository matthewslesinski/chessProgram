package moves;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import boardFeatures.Square;
import convenienceDataStructures.UnmodifiableWrappedSet;
import pieces.PieceType;
import support.Constructors;

/**
 * Represents a {@code Set} of {@code Move} by compressing each move into an int
 * @author matthewslesinski
 *
 */
public class MoveSet implements UnmodifiableWrappedSet<Move> {

	/** Stores the compressed moves */
	protected int[] moveStore;
	
	
	/**
	 * Initializes this {@code MoveSet} with all of its moves
	 * @param moves The moves to include in this {@code MoveSet}
	 */
	public MoveSet(Collection<Move> moves) {
		moveStore = new int[moves.size()];
		int index = 0;
		Iterator<Move> iterator = moves.iterator();
		while (iterator.hasNext()) {
			moveStore[index++] = iterator.next().compress();
		}
	}
	
	@Override
	public int size() {
		return moveStore.length;
	}

	@Override
	public boolean contains(Object o) {
		for (Move move : this) {
			if (o.equals(move)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<Move> iterator() {
		return new Iterator<Move>() {

			private int index = 0;
			@Override
			public boolean hasNext() {
				return index < moveStore.length;
			}

			@Override
			public Move next() {
				return Constructors.MOVE_DECOMPRESSOR.apply(moveStore[index++]);
			}
			
		};
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!(o instanceof Move)) {
				return false;
			}
			Move move = (Move) o;
			if (!contains(move)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<Move> getWrappedSet() {
		return this;
	}
	
	/**
	 * Maps each relevant piece/square combination to the list of moves involving that piece moving to that square.
	 * This is important for figuring out how much information to include in the strings of moves that get printed.
	 * @param moves The {@code Set} of {@code Move}s that can be made
	 * @return
	 */
	public static Map<PieceType, Map<Square, List<Move>>> mapEndSquaresToMovesForPieces(Set<Move> moves) {
		Map<PieceType, Map<Square, List<Move>>> map = new EnumMap<>(PieceType.class);
		moves.forEach(move -> {
			PieceType piece = move.getMovingPieceType();
			Map<Square, List<Move>> squareMapping = map.getOrDefault(piece, new EnumMap<>(Square.class));
			map.putIfAbsent(piece, squareMapping);
			Square endSquare = move.getEndSquare();
			List<Move> movesWithRelevantDestination = squareMapping.getOrDefault(endSquare, new LinkedList<>());
			squareMapping.putIfAbsent(endSquare, movesWithRelevantDestination);
			movesWithRelevantDestination.add(move);
		});
		return map;
	}
	
	@Override
	public String toString() {
		Map<PieceType, Map<Square, List<Move>>> endSquareMap = mapEndSquaresToMovesForPieces(this);
		return toStringImpl(move -> move.getMoveAsString(endSquareMap));
	}

}
