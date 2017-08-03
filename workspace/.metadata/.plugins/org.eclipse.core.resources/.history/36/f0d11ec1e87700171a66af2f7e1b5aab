package convenienceDataStructures;

import java.util.Collection;

import dataStructures.SquareSet;
import support.BadArgumentException;

public interface UnmodifiableWrappedCollection<E> extends IrreversibleWrappedCollection<E> {
	
	@Override
	public default boolean add(E e) {
		throw new BadArgumentException(this, SquareSet.class, REMOVAL_MESSAGE);
	}

	@Override
	public default boolean addAll(Collection<? extends E> c) {
		throw new BadArgumentException(this, SquareSet.class, REMOVAL_MESSAGE);
	}
}
