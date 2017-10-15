package convenienceDataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a {@code List} that can't be modified. Therefore, any elements that should go in this list should be passed
 * to a constructor
 * @author matthewslesinski
 *
 * @param <E> The elements' type
 */
public interface UnmodifiableWrappedList<E> extends List<E>, UnmodifiableWrappedCollection<E> {
	
	/**
	 * Retrieves the {@code List} this wraps
	 * @return The {@code List}
	 */
	public List<E> getWrappedList();
	
	@Override
	public default Collection<E> getWrappedCollection() {
		return this.getWrappedList();
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
	public default void add(int index, E e) {
		UnmodifiableWrappedCollection.super.add(e);
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
	
	@Override
	public default boolean addAll(int index, Collection<? extends E> c) {
		return addAll(c);
	}


	@Override
	public default E get(int index) {
		return getWrappedList().get(index);
	}

	@Override
	public default E set(int index, E element) {
		add(index, element);
		return remove(index + 1);
	}


	@Override
	public default E remove(int index) {
		E toReturn = get(index);
		remove(toReturn);
		return toReturn;
	}

	@Override
	public default int indexOf(Object o) {
		return getWrappedList().indexOf(o);
	}

	@Override
	public default int lastIndexOf(Object o) {
		return getWrappedList().lastIndexOf(o);
	}

	@Override
	public default ListIterator<E> listIterator() {
		return getWrappedList().listIterator();
	}

	@Override
	public default ListIterator<E> listIterator(int index) {
		return getWrappedList().listIterator(index);
	}

	@Override
	public default List<E> subList(int fromIndex, int toIndex) {
		return new UnmodifiableWrappedList<>() {
			@Override
			public List<E> getWrappedList() {
				return getWrappedList().subList(fromIndex, toIndex);
			}
		};
	}
}
