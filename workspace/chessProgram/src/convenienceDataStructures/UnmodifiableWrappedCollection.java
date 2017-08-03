package convenienceDataStructures;

import java.util.Collection;

import dataStructures.KingMoveSet;
import support.BadArgumentException;

public interface UnmodifiableWrappedCollection<E> extends IrreversibleWrappedCollection<E> {
	
	@Override
	public default boolean add(E e) {
		throw new BadArgumentException(this, KingMoveSet.class, REMOVAL_MESSAGE);
	}

	@Override
	public default boolean addAll(Collection<? extends E> c) {
		throw new BadArgumentException(this, KingMoveSet.class, REMOVAL_MESSAGE);
	}
}
