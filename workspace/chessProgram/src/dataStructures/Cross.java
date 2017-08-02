//package dataStructures;
//
//import java.util.Collection;
//import java.util.EnumMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.function.BiFunction;
//import java.util.function.Predicate;
//
//import boardFeatures.Direction;
//import boardFeatures.Line;
//import boardFeatures.LineType;
//import boardFeatures.Square;
//import support.BadArgumentException;
//import support.UtilityFunctions;
//
//public class Cross<E extends Enum<E>> extends SquareSet<E> implements AttackStructure<E> {
//
//	public static class OfSquares extends Cross<Square> {
//		private OfSquares(Map<Direction, List<Square>> elements, Square center) {
//			super(Square.class, elements, center, (lineType, square) -> (Line) lineType.getLineBySquare(square));
//		}
//		
//		public static Cross<Square> build(Map<Direction, List<Square>> elements, Square center) {
//			return new OfSquares(elements, center);
//		}
//	}
//	
//	
//	private final Map<Direction, List<E>> DIRECTION_MAP;
//	
//	private Cross(Class<E> type, Map<Direction, List<E>> elements, E center, BiFunction<LineType, E, Line> relation) {
//		super(type, (Collection<E>) UtilityFunctions.concat(elements.values()), center, relation);
//		this.DIRECTION_MAP = new EnumMap<Direction, List<E>>(elements);
//	}
//	
//	
//	public static <T extends Enum<T>> Map<Direction, List<T>> addDirection(Direction dir, List<T> elements, Map<Direction, List<T>> directionMap) {
//		if (directionMap.containsKey(dir)) {
//			throw new BadArgumentException(dir, directionMap.getClass(), "The given direction was already added");
//		}
//		directionMap.put(dir, elements);
//		return directionMap;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static <T extends Enum<T>> Cross<T> build(Map<Direction, List<T>> elements, T center, BiFunction<LineType, T, Line> relation) {
//		return new Cross<T>((Class<T>) center.getClass(), elements, center, relation);
//	}
//	
//	@Override
//	public Iterator<E> getIteratorFromPerspective(Cluster<E> target, Predicate<E> shouldTraverse) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
