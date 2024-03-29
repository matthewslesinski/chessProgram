package convenienceDataStructures;

import java.util.Collection;

import moveCalculationStructures.KingMoveSet;
import support.BadArgumentException;

/**
 * This represents a {@code Collection} that can't have any of its elements removed once they've been added
 * @author matthewslesinski
 *
 * @param <E> The elements' type
 */
public interface IrreversibleWrappedCollection<E> extends ModifiableWrappedCollection<E> {

	public final static String MODIFICATION_MESSAGE = "Modification after creation is not allowed";
	
	@Override
	public default boolean remove(Object o) {
		throw new BadArgumentException(this, KingMoveSet.class, MODIFICATION_MESSAGE);
	}
	
	@Override
	public default boolean retainAll(Collection<?> c) {
		throw new BadArgumentException(this, KingMoveSet.class, MODIFICATION_MESSAGE);
	}

	@Override
	public default boolean removeAll(Collection<?> c) {
		throw new BadArgumentException(this, KingMoveSet.class, MODIFICATION_MESSAGE);
	}

	@Override
	public default void clear() {
		throw new BadArgumentException(this, KingMoveSet.class, MODIFICATION_MESSAGE);
	}
}
