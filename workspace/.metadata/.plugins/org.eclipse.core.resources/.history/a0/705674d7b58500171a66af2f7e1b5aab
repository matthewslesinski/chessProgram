package convenienceDataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public interface UnmodifiableWrappedSet<E> extends Set<E>, UnmodifiableWrappedCollection<E> {

	
	/**
	 * Retrieves the {@code Set} this wraps
	 * @return The {@code Set}
	 */
	public Set<E> getWrappedSet();
	
	@Override
	public default Collection<E> getWrappedCollection() {
		return this.getWrappedSet();
	}

	@Override
	public default int size() {
		return UnmodifiableWrappedCollection.super.size();
	}

	@Override
	public default boolean isEmpty() {
		return UnmodifiableWrappedCollection.super.isEmpty();
	}

	@Override
	public default boolean contains(Object o) {
		return UnmodifiableWrappedCollection.super.contains(o);
	}

	@Override
	public default Iterator<E> iterator() {
		return UnmodifiableWrappedCollection.super.iterator();
	}

	@Override
	public default Object[] toArray() {
		return UnmodifiableWrappedCollection.super.toArray();
	}

	@Override
	public default <T> T[] toArray(T[] a) {
		return UnmodifiableWrappedCollection.super.toArray(a);
	}

	@Override
	public default boolean add(E e) {
		return UnmodifiableWrappedCollection.super.add(e);
	}

	@Override
	public default boolean remove(Object o) {
		return UnmodifiableWrappedCollection.super.remove(o);
	}

	@Override
	public default boolean containsAll(Collection<?> c) {
		return UnmodifiableWrappedCollection.super.containsAll(c);
	}

	@Override
	public default boolean addAll(Collection<? extends E> c) {
		return UnmodifiableWrappedCollection.super.addAll(c);
	}

	@Override
	public default boolean retainAll(Collection<?> c) {
		return UnmodifiableWrappedCollection.super.retainAll(c);
	}

	@Override
	public default boolean removeAll(Collection<?> c) {
		return UnmodifiableWrappedCollection.super.removeAll(c);
	}

	@Override
	public default void clear() {
		UnmodifiableWrappedCollection.super.clear();
	}
	
}
