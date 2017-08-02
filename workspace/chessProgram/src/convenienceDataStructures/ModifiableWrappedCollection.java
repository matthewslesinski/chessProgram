package convenienceDataStructures;

import java.util.Collection;
import java.util.Iterator;

public interface ModifiableWrappedCollection<E> extends Collection<E> {

	/**
	 * Gets the collection that this wraps
	 * @return The collection
	 */
	public Collection<E> getWrappedCollection();
	
	@Override
	public default int size() {
		return getWrappedCollection().size();
	}

	@Override
	public default boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public default boolean contains(Object o) {
		return getWrappedCollection().contains(o);
	}

	@Override
	public default Iterator<E> iterator() {
		return getWrappedCollection().iterator();
	}

	@Override
	public default Object[] toArray() {
		return getWrappedCollection().toArray();
	}

	@Override
	public default <T> T[] toArray(T[] a) {
		return getWrappedCollection().toArray(a);
	}
	
	@Override
	public default boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public default boolean addAll(Collection<? extends E> c) {
		boolean toReturn = false;
		for (E e : c) {
			toReturn |= add(e);
		}
		return toReturn;
	}

}