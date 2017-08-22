package dataStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Spliterator;
import java.util.function.IntFunction;

import convenienceDataStructures.UnmodifiableWrappedList;
import support.BadArgumentException;
import support.UtilityFunctions;

/**
 * A list of elements, but it uses maps to allow the neighboring elements to be retrieved for each element
 * @author matthewslesinski
 *
 * @param <E> The type of the elements
 */
public class ListBackedByMaps<E extends Enum<E>> implements Isomorphism<E, E>, FixedOrderingSet<E>, UnmodifiableWrappedList<E> {

	/** The {@code Map} from each element to its next */
	private final Map<E, E> forwards;
	
	/** The {@code Map} from each element to the previous one */
	private final Map<E, E> backwards;
	
	/** A straightforward list of the elements in this list */
	private final List<E> elements;

	/**
	 * Builds the list from no elements, so the type of the non-existent elements is necessary.
	 * @param type The type of elements
	 */
	public ListBackedByMaps(Class<E> type) {
		this(Collections.emptyList(), type);
	}

	/**
	 * Builds the list from a collection. Since a type isn't provided, the collection can't be empty
	 * @param collection The collection
	 */
	@SuppressWarnings("unchecked")
	public ListBackedByMaps(Collection<E> collection) {
		this(checkNonEmpty(collection), (Class<E>) collection.stream().findFirst().get().getClass());
	}
	
	/**
	 * Builds the list from some collection, and uses the type for the elements to create the requisite {@code Map}s
	 * @param collection The {@code Collection} of elements
	 * @param type The type of the elements
	 */
	private ListBackedByMaps(Collection<E> collection, Class<E> type) {
		this.elements = new ArrayList<>(collection);
		this.forwards = new EnumMap<>(type);
		this.backwards = new EnumMap<>(type);
		E previous = null;
		for (E element : elements) {
			if (previous != null) {
				put(previous, element);
			}
			previous = element;
		}
	}
	
	/**
	 * Ensures a collection is non-empty, and if it is, throws an exception. 
	 * @param collection The collection to check
	 * @return Also returns that collection
	 */
	private static <T> Collection<T> checkNonEmpty(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) {
			throw new BadArgumentException(collection, collection.getClass(), "Expected a non-empty collection");
		}
		return collection;
	}
	
	/**
	 * Gets the element at some index
	 * @param index The index to retrieve
	 * @return The element at that index
	 */
	private E getFromList(int index) {
		if (index >= elements.size() || index < 0) {
			return null;
		}
		return elements.get(index);
	}
	
	/**
	 * Gets the next element in this list
	 * @param element The element to retrieve the one next to it for
	 * @return The next element
	 */
	public E getNext(E element) {
		return element == null ? getFromList(0) : getByElementOnLeft(element);
	}
	
	/**
	 * Gets the previous element in this list
	 * @param element The element to retrieve the one previous to it for
	 * @return The previous element
	 */
	public E getPrevious(E element) {
		return element == null ? getFromList(size() - 1) : getByElementOnRight(element);
	}

	@Override
	public Map<E, E> getLeftwardMap() {
		return forwards;
	}


	@Override
	public Map<E, E> getRightwardMap() {
		return backwards;
	}



	@Override
	public Comparator<? super E> comparator() {
		return new Comparator<E>() {
			@Override
			public int compare(E o1, E o2) {
				return o1.ordinal() - o2.ordinal();
			}
		};
	}


	@Override
	public NavigableSet<E> descendingSet() {
		return new ListBackedByMaps<E>(UtilityFunctions.reverseList(elements));
	}


	@Override
	public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
		int fromIndex = indexOf(fromElement);
		int toIndex = indexOf(toElement);
		if (!fromInclusive) {
			fromIndex += 1;
		}
		if (!toInclusive) {
			toIndex += 1;
		}
		return new ListBackedByMaps<E>(elements.subList(fromIndex, toIndex));
	}


	@Override
	public E retrieveOffsetFromElement(E element, int offset) {
		int index = indexOf(element) + offset;
		return index >= 0 && index < size() ? get(indexOf(element) + offset) : null;
	}


	@Override
	public IntFunction<E> getIndexFunction() {
		return this::get;
	}


	@Override
	public void clear() {
		UnmodifiableWrappedList.super.clear();
	}


	@Override
	public int size() {
		return UnmodifiableWrappedList.super.size();
	}


	@Override
	public boolean isEmpty() {
		return UnmodifiableWrappedList.super.isEmpty();
	}



	@Override
	public boolean containsAll(Collection<?> c) {
		return FixedOrderingSet.super.containsAll(c);
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		return UnmodifiableWrappedList.super.retainAll(c);
	}


	@Override
	public boolean remove(Object o) {
		return UnmodifiableWrappedList.super.remove(o);
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		return UnmodifiableWrappedList.super.removeAll(c);
	}


	@Override
	public Iterator<E> iterator() {
		return UnmodifiableWrappedList.super.iterator();
	}
	
	@Override
	public List<E> getWrappedList() {
		return elements;
	}


	@Override
	public boolean contains(Object o) {
		return containsKeyOnLeft(o) || containsKeyOnRight(o);
	}


	@Override
	public Spliterator<E> spliterator() {
		return UnmodifiableWrappedList.super.spliterator();
	}


	@Override
	public Object[] toArray() {
		return UnmodifiableWrappedList.super.toArray();
	}


	@Override
	public <T> T[] toArray(T[] a) {
		return UnmodifiableWrappedList.super.toArray(a);
	}
	
	@Override
	public String toString() {
		return toStringImpl(element -> element.toString());
	}
}
