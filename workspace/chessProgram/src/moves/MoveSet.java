package moves;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import boardFeatures.Square;
import convenienceDataStructures.UnmodifiableWrappedSet;
import pieces.PieceType;
import stringUtilities.MoveWriter;
import static support.Constructors.*;
import static support.UtilityFunctions.*;

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
		return new Iterator<>() {

			private int index = 0;
			@Override
			public boolean hasNext() {
				return index < moveStore.length;
			}

			@Override
			public Move next() {
				return MOVE_DECOMPRESSOR.apply(moveStore[index++]);
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
	
	@Override
	public String toString() {
		Map<PieceType, Map<Square, List<Move>>> endSquareMap = MoveWriter.mapEndSquaresToMovesForPieces(this);
		return toStringImpl(bind(MoveWriter::getMoveAsString, endSquareMap));
	}

}
