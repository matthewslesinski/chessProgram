package convenienceDataStructures;

import java.util.Collection;

import dataStructures.SquareSet;
import support.BadArgumentException;

public interface IrreversibleWrappedCollection<E> extends ModifiableWrappedCollection<E> {

	public final static String REMOVAL_MESSAGE = "Modification after creation is not allowed";
	
	@Override
	public default boolean remove(Object o) {
		throw new BadArgumentException(this, SquareSet.class, REMOVAL_MESSAGE);
	}
	
	@Override
	public default boolean retainAll(Collection<?> c) {
		throw new BadArgumentException(this, SquareSet.class, REMOVAL_MESSAGE);
	}

	@Override
	public default boolean removeAll(Collection<?> c) {
		throw new BadArgumentException(this, SquareSet.class, REMOVAL_MESSAGE);
	}

	@Override
	public default void clear() {
		throw new BadArgumentException(this, SquareSet.class, REMOVAL_MESSAGE);
	}
}