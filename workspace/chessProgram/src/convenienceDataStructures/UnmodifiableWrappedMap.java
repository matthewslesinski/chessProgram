package convenienceDataStructures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Wraps a {@code Map} that shouldn't be modified. This is similar to one of the interfaces that extend {@code UnmodifiableWrappedCollection},
 * except that since {@code Map} doesn't extend {@code Collection} it can't actually extend {@code UnmodifiabledWrappedCollection} 
 * @author matthewslesinski
 *
 * @param <E> The key type for the map
 * @param <F> The value type for the map
 */
public interface UnmodifiableWrappedMap<E, F> extends Map<E, F> {

	

	public Map<E, F> getWrappedMap();
	
	@Override
	public default int size() {
		return getWrappedMap().size();
	}

	@Override
	public default boolean isEmpty() {
		return getWrappedMap().isEmpty();
	}

	@Override
	public default boolean containsKey(Object key) {
		return getWrappedMap().containsKey(key);
	}

	@Override
	public default boolean containsValue(Object value) {
		return getWrappedMap().containsValue(value);
	}

	@Override
	public default F get(Object key) {
		return getWrappedMap().get(key);
	}

	@Override
	public default F put(E key, F value) {
		return getWrappedMap().put(key, value);
	}

	@Override
	public default F remove(Object key) {
		return getWrappedMap().remove(key);
	}

	@Override
	public default void putAll(Map<? extends E, ? extends F> m) {
		getWrappedMap().putAll(m);
	}

	@Override
	public default void clear() {
		getWrappedMap().clear();
	}

	@Override
	public default Set<E> keySet() {
		return getWrappedMap().keySet();
	}

	@Override
	public default Collection<F> values() {
		return getWrappedMap().values();
	}

	@Override
	public default Set<java.util.Map.Entry<E, F>> entrySet() {
		return getWrappedMap().entrySet();
	}
	
}
