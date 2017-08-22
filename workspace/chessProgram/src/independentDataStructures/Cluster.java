package independentDataStructures;

import convenienceDataStructures.UnmodifiableWrappedSet;

/**
 * Some {@code Set} of elements that are clustered together in some way around a center
 * @author matthewslesinski
 *
 * @param <E> The type of the elements
 */
public interface Cluster<E> extends UnmodifiableWrappedSet<E>{

	/**
	 * Gets the center square in this cluster
	 * @return The {@code Square}
	 */
	public E getCenter();
}
