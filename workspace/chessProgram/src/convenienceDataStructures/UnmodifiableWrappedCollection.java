package convenienceDataStructures;

import java.util.Collection;

import moveCalculationStructures.KingMoveSet;
import support.BadArgumentException;

/**
 * Represents an {@code IrreversibleWrappedCollection} that also can't have any elements added to it. It is assumed
 * any elements that should be added will be passed to a constructor. 
 * @author matthewslesinski
 *
 * @param <E> The type of elements
 */
public interface UnmodifiableWrappedCollection<E> extends IrreversibleWrappedCollection<E> {
	
	@Override
	public default boolean add(E e) {
		throw new BadArgumentException(this, KingMoveSet.class, MODIFICATION_MESSAGE);
	}

	@Override
	public default boolean addAll(Collection<? extends E> c) {
		throw new BadArgumentException(this, KingMoveSet.class, MODIFICATION_MESSAGE);
	}
}
