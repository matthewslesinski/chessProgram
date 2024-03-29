package support;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class ComparisonUtilityFunctions extends MathUtilityFunctions {

	/**
	 * Sorts two elements naively, since there's only two so the complications of logarithmic sort are unnecessary
	 * @param list The list to sort
	 * @param comparator A comparator for sorting
	 */
	public static <T> void sortTwoElements(List<T> list, Comparator<? super T> comparator) {
		if (list.size() != 2) {
			throw new BadArgumentException(list, List.class, "Expected list of size 2");
		}
		if (comparator.compare(list.get(0), list.get(1)) > 1) {
			Collections.reverse(list);
		}
	}
	
	/**
	 * Returns the argument that translates, via the translator function, to the largest value according to the comparator
	 * @param comparator The ordering for the type of the translated values
	 * @param translator The mapping function that turns the arguments into something comparable by the comparator
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the larger translated value
	 */
	public static <T, U> T argmax(Comparator<? super U> comparator, Function<T, U> translator, T arg1, T arg2) {
		if (comparator.compare(translator.apply(arg1), translator.apply(arg2)) < 0) {
			return arg2;
		} 
		return arg1;
	}
	
	/**
	 * Returns the argument that translates, via the translator function, to the largest value according to the natural ordering
	 * @param translator The mapping function that turns the arguments into something comparable
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the larger translated value
	 */
	public static <T, U extends Comparable<U>> T argmax(Function<T, U> translator, T arg1, T arg2) {
		return argmax(Comparator.naturalOrder(), translator, arg1, arg2);
	}
	
	/**
	 * Returns the argument that translates, via the translator function, to the largest value according to the natural ordering or doubles
	 * @param translator The mapping function that turns the arguments into a double
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the larger translated value
	 */
	public static <T> T argmax(ToDoubleFunction<T> translator, T arg1, T arg2) {
		if (Double.compare(translator.applyAsDouble(arg1), translator.applyAsDouble(arg2)) < 0) {
			return arg2;
		}
		return arg1;
	}
	
	/**
	 * Returns the argument that translates, via the translator function, to the largest value according to the natural ordering or ints
	 * @param translator The mapping function that turns the arguments into an int
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the larger translated value
	 */
	public static <T> T argmax(ToIntFunction<T> translator, T arg1, T arg2) {
		if (Integer.compare(translator.applyAsInt(arg1), translator.applyAsInt(arg2)) < 0) {
			return arg2;
		}
		return arg1;
	}
	
	/**
	 * Returns the argument that translates, via the translator function, to the smallest value according to the comparator
	 * @param comparator The ordering for the type of the translated values
	 * @param translator The mapping function that turns the arguments into something comparable by the comparator
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the smaller translated value
	 */
	public static <T, U> T argmin(Comparator<? super U> comparator, Function<T, U> translator, T arg1, T arg2) {
		if (comparator.compare(translator.apply(arg1), translator.apply(arg2)) > 0) {
			return arg2;
		}
		return arg1;
	}
	/**
	 * Returns the argument that translates, via the translator function, to the smallest value according to the natural ordering
	 * @param translator The mapping function that turns the arguments into something comparable
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the smaller translated value
	 */
	public static <T, U extends Comparable<U>> T argmin(Function<T, U> translator, T arg1, T arg2) {
		return argmin(Comparator.naturalOrder(), translator, arg1, arg2);
	}
	
	/**
	 * Returns the argument that translates, via the translator function, to the smallest value according to the natural ordering or doubles
	 * @param translator The mapping function that turns the arguments into a double
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the smaller translated value
	 */
	public static <T> T argmin(ToDoubleFunction<T> translator, T arg1, T arg2) {
		if (Double.compare(translator.applyAsDouble(arg1), translator.applyAsDouble(arg2)) > 0) {
			return arg2;
		}
		return arg1;
	}
	
	/**
	 * Returns the argument that translates, via the translator function, to the smallest value according to the natural ordering or ints
	 * @param translator The mapping function that turns the arguments into an int
	 * @param arg1 The first argument
	 * @param arg2 The second argument
	 * @return The argument with the smaller translated value
	 */
	public static <T> T argmin(ToIntFunction<T> translator, T arg1, T arg2) {
		if (Integer.compare(translator.applyAsInt(arg1), translator.applyAsInt(arg2)) > 0) {
			return arg2;
		}
		return arg1;
	}
}
