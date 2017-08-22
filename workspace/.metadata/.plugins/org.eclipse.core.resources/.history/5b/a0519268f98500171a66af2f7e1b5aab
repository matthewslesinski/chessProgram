package dataStructures;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author matthewslesinski
 *
 * @param <E>
 */
public interface AttackStructure<E> extends Set<E> {

	
	/**
	 * Traverses the elements in this set in order of their closeness to the target element, only caring about elements
	 * that {@code shouldTraverse} returns true for
	 * @param target The target element
	 * @param shouldTraverse A function that tells us if we care about an element, or if it's too far away
	 * @return An instance of {@code Iterable} to iterate over
	 */
	public default Iterable<E> traverseRelevantElements(Cluster<E> target, Predicate<E> shouldTraverse) {
		return new Iterable<E>() {

			@Override
			public Iterator<E> iterator() {
				return getIteratorFromPerspective(target, element -> target.contains(element) && shouldTraverse.test(element));
			}
			
		};
	}
	
	/**
	 * Actually calculates the {@code Iterator} that traverses the closest elements
	 * @param target The element from which we are viewing the convex shape
	 * @param shouldTraverse A function that tells if the element is close enough to care about
	 * @return The {@code Iterator for the elements}
	 */
	public Iterator<E> getIteratorFromPerspective(Cluster<E> target, Predicate<E> shouldTraverse);
}
