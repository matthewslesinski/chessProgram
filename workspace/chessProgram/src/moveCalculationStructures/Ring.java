package moveCalculationStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import boardFeatures.Square;
import independentDataStructures.AttackStructure;
import independentDataStructures.BreadthFirstTraversal;
import independentDataStructures.Cluster;
import independentDataStructures.EnumSequence;
import support.BadArgumentException;
import static support.UtilityFunctions.*;


/**
 * This class represents a ring of squares (such as a knight's possible moves), and it's ordered and arranged such that it can
 * return the closest one of its squares to any external square, as well as iterate over its squares in order of distance from
 * one of its squares. That way it can create an iterator of the squares closest to some external square, and ensure that each square
 * it iterates over to one side of the closest square will be further from the closest square than any of the previous.
 * @author matthewslesinski
 *
 * @param <E>
 */
public class Ring<E extends Enum<E>> extends EnumSequence<E> implements AttackStructure<E> {
	
	/**
	 * An implementation of the Ring class that is not generic and specific to Squares
	 * @author matthewslesinski
	 *
	 */
	public static class OfSquares extends Ring<Square> {

		/**
		 * Builds the ring from an {@code EvenlySpacedCircle}, since that class guarantees some of the qualities necessary for a Ring
		 * @param elements The set of squares in this ring
		 */
		public OfSquares(EvenlySpacedCircle elements) {
			super(elements, target -> friendFinder(target, elements));
		}
		
		/**
		 * Returns either one or two squares that are the closest ones in this ring to some target square. It uses an
		 * {@code EvenlySpacedCircle} used to build this instance to calculate that square.
		 * @param target The external square we want to be close to
		 * @param circle The circle. This is a necessary argument so that this function can be passed to super
		 * @return The nearest squares.
		 */
		private static List<Square> friendFinder(Square target, EvenlySpacedCircle circle) {
			return circle.getNearestSquares(target);
		}
	}
	
	private static final int LARGEST_EXPECTED_CLOSE_ELEMENTS = 2;
	private static final int SMALLEST_EXPECTED_CLOSE_ELEMENTS = 1;
	
	private final Supplier<Set<E>> setConstructor = () -> EnumSet.noneOf(this.type);
	private final Function<E, List<E>> friendFinder;
	
	/**
	 * Builds the ring from the set of elements in it and a function to get the nearest internal element(s) to some given external element
	 * @param elements The set of elements
	 * @param friendFinder The function to find the nearest element(s)
	 */
	public Ring(Collection<E> elements, Function<E, List<E>> friendFinder) {
		super(elements);
		this.friendFinder = friendFinder;
	}

	@Override
	public Iterator<E> getIteratorFromPerspective(Cluster<E> target, Predicate<E> shouldTraverse) {
		return new BreadthFirstTraversal<>(constructNeighborFunction(target.getCenter(), shouldTraverse), setConstructor);
	}
	
	@Override
	protected E getIndex(int index) {
		return super.getIndex(Math.floorMod(index, size()));
	}
	
	/**
	 * Builds the function that is used to determine the neighbor in the right direction to a particular element when moving around the ring
	 * @param target The element external to the ring that we want to find the closest one to
	 * @param shouldTraverse A boolean function to tell us whether we care about a particular element in the ring, or if we should stop there
	 * @return The function to iterate over the ring
	 */
	private Function<E, Collection<E>> constructNeighborFunction(E target, Predicate<E> shouldTraverse) {
		final Set<E> goingDown = setConstructor.get();
		final Set<E> goingUp = setConstructor.get();
		return current -> getNeighbors(target, shouldTraverse, goingDown, goingUp, current);
	}
	
	/**
	 * Determines the neighbors in this ring to the current element. If the current element is null, then find the closest one to the target
	 * @param target The target element to find the closest one to if current is null
	 * @param shouldTraverse A function to tell us if we care about an element
	 * @param goingDown The set of elements we've looked at already while we have been going leftwards around the ring
	 * @param goingUp The set of elements we've looked at already while we have been going rightwards around the ring
	 * @param current The current element being looked at
	 * @return A collection of the neighbors. This will be one element or two, ideally.
	 */
	private Collection<E> getNeighbors(E target, Predicate<E> shouldTraverse, Set<E> goingDown, Set<E> goingUp, E current) {
		if (current == null) {
			return getStartElement(target, shouldTraverse, goingDown, goingUp);
		}
		List<E> toReturn  = new LinkedList<>();
		E candidate;
		if (goingDown.contains(current)) {
			candidate = this.lower(current);
			if (shouldTraverse.test(candidate)) {
				toReturn.add(candidate);
				goingDown.add(candidate);
			}
		}
		if (goingUp.contains(current)) {
			candidate = this.higher(current);
			if (shouldTraverse.test(candidate)) {
				toReturn.add(candidate);
				goingDown.add(candidate);
			}
		}
		return toReturn;
	}
	
	/**
	 * Determines the closest element(s) to the target and adds it/them to the appropriate sets.
	 * @param target The target
	 * @param shouldTraverse Whether we care about the element(s)
	 * @param goingDown The set of elements covered as we go leftward around the ring
	 * @param goingUp The set of elements covered as we go rightward around the ring
	 * @return The list of closest element(s)
	 */
	private List<E> getStartElement(E target, Predicate<E> shouldTraverse, Set<E> goingDown, Set<E> goingUp) {
		// Determine the closest element(s)
		List<E> closestElements = getClosestElements(target);
		switch (closestElements.size()) {
		// If there's one element
		case SMALLEST_EXPECTED_CLOSE_ELEMENTS:
			E loneElement = closestElements.get(0);
			// If we care about it, add it to the sets and return it
			if (shouldTraverse.test(loneElement)) {
				goingDown.add(loneElement);
				goingUp.add(loneElement);
				return closestElements;
			}
			// If we don't care about it, ignore it
			return Collections.emptyList();
		// If there's two elements
		case LARGEST_EXPECTED_CLOSE_ELEMENTS:
			// Make sure they're in an order so that we can tell apart which one is which
			sortTwoElements(closestElements, this.comparator());
			E firstElement = closestElements.get(0);
			E secondElement = closestElements.get(1);
			// If we care about them, add them to the sets, otherwise get rid of them
			if (shouldTraverse.test(firstElement)) {
				goingDown.add(firstElement);
			} else {
				closestElements.remove(0);
			}
			if (shouldTraverse.test(secondElement)) {
				goingUp.add(secondElement);			
			} else {
				closestElements.remove(closestElements.size() - 1);
			}
			return closestElements;
		default:
			throw new BadArgumentException(closestElements, List.class, "Unexpected number of close elements");
		}
	}
	
	/**
	 * Determines the element(s) closest to the target
	 * @param target The target
	 * @return The closest elements
	 */
	private List<E> getClosestElements(E target) {
		return friendFinder.apply(target);
	}
	
	/**
	 * Determines if the first element is right below the second one in the ring
	 * @param first The first element
	 * @param second The second element
	 * @return If it's adjacent and below.
	 */
	private boolean isFirstLower(E first, E second) {
		int arg1Index = retrieveListIndex(first);
		int arg2Index = retrieveListIndex(second);
		return Math.floorMod(arg2Index - arg1Index, size()) == 1;
	}
	
	// Override the comparator so that the first element is greater than the last, to make it a connected ring. This will be used for sorting
	@Override
	public Comparator<? super E> comparator() {
		return (E arg1, E arg2) -> {
			if (arg1 == arg2) {
				return 0;
			}
			if (isFirstLower(arg1, arg2)) {
				return -1;
			}
			if (isFirstLower(arg2, arg1)) {
				return 1;
			}
			throw new BadArgumentException(Arrays.asList(arg1, arg2), arg1.getClass(), "The arguments, " + arg1 + " and " + arg2 + ", "
					+ "are not adjacent in this ring");
		};
	}
	
}
