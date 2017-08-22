package dataStructures;

import java.util.function.Function;
import java.util.function.Supplier;

import boardFeatures.Square;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * This class represents an iterator over a set of elements such that it traverses them in breadth first order.
 * @author matthewslesinski
 *
 * @param <E> The type of the elements to iterate over
 */
public class BreadthFirstTraversal<E> implements Iterator<E> {

	/**
	 * An subclass meant to hold {@code Square}s
	 * @author matthewslesinski
	 *
	 */
	public static class OfSquares extends BreadthFirstTraversal<Square> {
		public OfSquares(Function<Square, Collection<Square>> getNeighbors) {
			super(getNeighbors, () -> EnumSet.noneOf(Square.class));
		}
	}
	
	/** Holds the queue of elements to traverse */
	private final Queue<E> queue = new LinkedList<>();
	
	/** Holds the elements already visited, so that they don't get revisited and to avoid cycles */
	private final Set<E> visitedElements;
	
	/**
	 * The function that defines the edges in the graph being traversed. This function should return the
	 * root element(s) when null is passed to it
	 */
	private final Function<E, Collection<E>> getNeighbors;
	
	public BreadthFirstTraversal(Function<E, Collection<E>> getNeighbors, Supplier<Set<E>> setConstructor) {
		Collection<E> startNodes = getNeighbors.apply(null);
		this.visitedElements = setConstructor.get();
		visitedElements.addAll(startNodes);
		queue.addAll(startNodes);
		this.getNeighbors = getNeighbors;
	}
	
	
	
	@Override
	public boolean hasNext() {
		return !queue.isEmpty();
	}

	@Override
	public E next() {
		E currentElement = queue.poll();
		Iterable<E> neighbors = getNeighbors.apply(currentElement);
		neighbors.forEach(neighbor -> {
			if (!visitedElements.contains(neighbor)) {
				visitedElements.add(neighbor);
				queue.add(neighbor);
			}
		});
		return currentElement;
	}	
}
