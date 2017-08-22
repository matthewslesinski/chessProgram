package independentDataStructures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An undirected linkage between two types of elements, such that each mapping from an element on the left to an element on the right
 * has a corresponding mapping from that element on the right to that element on the left, and vice versa.
 * @author matthewslesinski
 *
 * @param <E> The type of the leftward elements
 * @param <F> The type of the righward elements
 */
public interface Isomorphism<E, F> {
	
	/**
	 * Gets a map from the elements on the left to the elements on the right
	 * @return The map
	 */
	public Map<E, F> getLeftwardMap();
	
	/**
	 * Gets a map from the elements on the right to the elements on the left
	 * @return The map
	 */
	public Map<F, E> getRightwardMap();
	
	/**
	 * The size of the isomorphism
	 * @return The size
	 */
	public default int size() {
		return getLeftwardMap().size();
	}

	/**
	 * If there are any relations in this isomorphism
	 * @return true iff there are
	 */
	public default boolean isEmpty() {
		return getLeftwardMap().isEmpty();
	}

	/**
	 * returns true iff there's a relation where the element on the left is the key
	 * @param key The key to check for
	 * @return true iff the key's on the left
	 */
	public default boolean containsKeyOnLeft(Object key) {
		return getLeftwardMap().containsKey(key);
	}

	/**
	 * returns true iff there's a relation where the element on the right is the key
	 * @param key The key to check for
	 * @return true iff the key's on the right
	 */
	public default boolean containsKeyOnRight(Object key) {
		return getRightwardMap().containsKey(key);
	}

	/**
	 * Retrieves the element on the right given its associated element on the left, or null if they're not present
	 * @param key The key to retrieve
	 * @return The element on the right associated with the key
	 */
	public default F getByElementOnLeft(Object key) {
		return getLeftwardMap().get(key);
	}
	
	/**
	 * Retrieves the element on the left given its associated element on the right, or null if they're not present
	 * @param key The key to retrieve
	 * @return The element on the left associated with the key
	 */
	public default E getByElementOnRight(Object key) {
		return getRightwardMap().get(key);
	}

	/**
	 * Puts a relation in the isomorphism
	 * @param leftKey The element on the left in the relation
	 * @param rightKey The element on the right in the relation
	 * @return true iff there wasn't already relations containing either key, which allows the relation to be added
	 */
	public default boolean put(E leftKey, F rightKey) {
		Map<E, F> leftMap = getLeftwardMap();
		Map<F, E> rightMap = getRightwardMap();
		if (leftKey == null || rightKey == null || leftMap.containsKey(leftKey) || rightMap.containsKey(rightKey)) {
			return false;
		}
		leftMap.put(leftKey, rightKey);
		rightMap.put(rightKey, leftKey);
		return true;
	}

	/**
	 * Removes the relation involving the key on the left
	 * @param key The key to remove
	 * @return The element on the right associated with the key
	 */
	public default F removeFromLeft(Object key) {
		Map<E, F> leftMap = getLeftwardMap();
		Map<F, E> rightMap = getRightwardMap();
		F toReturn;
		if ((toReturn = leftMap.remove(key)) == null) {
			return null;
		}
		rightMap.remove(toReturn);
		return toReturn;
	}
	
	/**
	 * Removes the relation involving the key on the right
	 * @param key The key to remove
	 * @return The element on the left associated with the key
	 */
	public default E removeFromRight(Object key) {
		Map<E, F> leftMap = getLeftwardMap();
		Map<F, E> rightMap = getRightwardMap();
		E toReturn;
		if ((toReturn = rightMap.remove(key)) == null) {
			return null;
		}
		leftMap.remove(toReturn);
		return toReturn;
	}

	/**
	 * Puts the map into this isomorphism, where the map only describes the leftward relationships
	 * @param m The map from elements on the left to the right
	 */
	public default void putAllFromLeft(Map<? extends E, ? extends F> m) {
		m.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
	}
	
	/**
	 * Puts the map into this isomorphism, where the map only describes the rightward relationships
	 * @param m The map from elements on the right to the left
	 */
	public default void putAllFromRight(Map<? extends F, ? extends E> m) {
		m.entrySet().forEach(entry -> put(entry.getValue(), entry.getKey()));
	}

	/**
	 * Clears all relations
	 */
	public default void clear() {
		getLeftwardMap().clear();
		getRightwardMap().clear();
	}

	/**
	 * Retrieves a set of the elements on the left
	 * @return The {@code Set}
	 */
	public default Set<E> leftSet() {
		return getLeftwardMap().keySet();
	}

	/**
	 * Retrieves a set of the elements on the right
	 * @return The {@code Set}
	 */
	public default Collection<F> rightSet() {
		return getRightwardMap().keySet();
	}

	/**
	 * Retrieves a set of the relations, directed from left to right
	 * @return The {@code Set}
	 */
	public default Set<java.util.Map.Entry<E, F>> leftEntrySet() {
		return getLeftwardMap().entrySet();
	}
	
	/**
	 * Retrieves a set of the relations, directed from right to left
	 * @return The {@code Set}
	 */
	public default Set<java.util.Map.Entry<F, E>> rightEntrySet() {
		return getRightwardMap().entrySet();
	}
	
}
