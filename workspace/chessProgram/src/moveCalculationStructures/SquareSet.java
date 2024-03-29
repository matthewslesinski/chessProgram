package moveCalculationStructures;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import boardFeatures.Square;
import independentDataStructures.Cluster;
import lines.Direction;
import lines.Line;
import lines.LineType;
import static support.UtilityFunctions.*;

/**
 * A set of {@code Square}s that all revolve around a center. Each {@code Square} is put in a list of all the {@code Square}s
 * along the same {@code Direction} from the center. Furthermore, all the {@code Square}s are put in a mapping that records
 * all of the {@code Line}s they're on. This indexing allows for easy retrieval of the {@code Square}s along a {@code Direction}
 * and falling on any {@code Line}.
 * @author matthewslesinski
 *
 */
public class SquareSet implements Cluster<Square> {

	/** The set of squares in this cluster */
	protected final Set<Square> squares;
	
	/** The center of this cluster */
	protected final Square center;
	
	/** Stores the squares on each line of each type */
	private final Map<LineType, Map<Line, Set<Square>>> lineToSquares = new EnumMap<>(LineType.class);
	private final Map<Direction, List<Square>> outwardLines = new EnumMap<>(Direction.class);
	
	public SquareSet(Collection<Square> elementsWithoutCenter, Square center) {
		this(elementsWithoutCenter, center, bind(SquareSet::getDirectionFromCenter, center));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SquareSet(Collection<Square> elementsWithoutCenter, Square center, Function<Square, Direction> directionGetter) {
		this.center = center;
		this.squares = elementsWithoutCenter.isEmpty() ? EnumSet.noneOf(Square.class) : EnumSet.copyOf(elementsWithoutCenter);
		for (Square square : elementsWithoutCenter) {
			// Add the square to the list of squares in its direction from the center
			Direction directionFromCenter = directionGetter.apply(square);
			List<Square> squaresInDirection = outwardLines.getOrDefault(directionFromCenter, new LinkedList<>());
			outwardLines.putIfAbsent(directionFromCenter, squaresInDirection);
			addSquareToListInOrder(square, squaresInDirection);
			for (LineType type : LineType.values()) {
				// record the square as occurring on each of the lines it occurs on
				Line line = type.getLineBySquare(square);
				lineToSquares.putIfAbsent(type, new EnumMap(line.getClass()));
				Map<Line, Set<Square>> innerMap = lineToSquares.get(type);
				innerMap.putIfAbsent(line, EnumSet.noneOf(Square.class));
				innerMap.get(line).add(square);
			}
		}
	}
	
	/**
	 * Gets the direction from the center to the given square
	 * @param center the center
	 * @param square The square to get the direction for
	 * @return The direction
	 */
	private static Direction getDirectionFromCenter(Square center, Square square) {
		return center.getDirectionToSquare(square);
	}
	
	@Override
	public Set<Square> getWrappedSet() {
		return squares;
	}

	@Override
	public Square getCenter() {
		return center;
	}
	
	/**
	 * Retrieves the set of squares in this cluster that are on a line
	 * @param line The line to retrieve squares on
	 * @return The set of squares
	 */
	public Set<Square> getSquaresOnLine(Line line) {
		return lineToSquares.get(line.getType()).getOrDefault(line, EnumSet.noneOf(Square.class));
	}
	
	/**
	 * Gets the list of included squares, radiating outward from {@code center}, in a particular direction
	 * @param dir The direction to get
	 * @return The squares in that direction
	 */
	public List<Square> getSquaresInDirectionFromCenter(Direction dir) {
		return outwardLines.getOrDefault(dir, Collections.emptyList());
	}
	
	/**
	 * Adds the square to the list, but makes that the closer elements to the center are sorted earlier in the list
	 * @param toAdd The square to add
	 * @param list The list to put it in
	 */
	private void addSquareToListInOrder(Square toAdd, List<Square> list) {
		int index = list.size();
		while (index > 0 && center.whichIsCloser(list.get(index - 1), toAdd) == toAdd) {
			index -= 1;
		}
		list.add(index, toAdd);
	}
	
	@Override
	public String toString() {
		return toStringImpl(element -> element.toString());
	}

}
