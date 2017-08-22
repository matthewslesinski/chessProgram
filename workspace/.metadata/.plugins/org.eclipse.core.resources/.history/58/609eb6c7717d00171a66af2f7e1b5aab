package moves;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;

/**
 * Represents a {@code Set} of {@code Move} by compressing each move into an int
 * @author matthewslesinski
 *
 */
public class MoveSet<M extends Move> implements Set<M> {

	/**
	 * Stores the compressed moves
	 */
	protected int[] moveStore;
	
	/**
	 * Used to create moves from the ints stored in this class
	 */
	// TODO Find some way to not need to store this constructor
	protected final IntFunction<M> constructor;
	
	/**
	 * Initializes this {@code MoveSet} with all of its moves
	 * @param moves The moves to include in this {@code MoveSet}
	 * @param constructor The constructor for recreating {@code Move}s from the compressed versions
	 */
	public MoveSet(List<M> moves, IntFunction<M> constructor) {
		this.constructor = constructor;
		moveStore = new int[moves.size()];
		this.addAll(moves);
	}
	
	@Override
	public int size() {
		return moveStore.length;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<M> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		return Arrays.stream(moveStore).mapToObj(constructor).toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return Arrays.stream(moveStore).mapToObj(constructor).toArray(length -> a);
	}

	@Override
	public boolean add(Move e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends M> c) {
		boolean toReturn = false;
		for (Move m : c) {
			toReturn = toReturn || add(m);
		}
		return toReturn;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


}
