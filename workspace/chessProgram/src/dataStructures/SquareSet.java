package dataStructures;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import boardFeatures.Line;
import boardFeatures.LineType;
import boardFeatures.Square;

public class SquareSet implements Cluster<Square> {

	/** The set of squares in this cluster */
	private final Set<Square> squares;
	
	/** The center of this cluster */
	private final Square center;
	
	/** Stores the squares on each line of each type */
	private final Map<LineType, Map<Line, Set<Square>>> lineToSquares = new EnumMap<>(LineType.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SquareSet(Collection<Square> elementsWithoutCenter, Square center) {
		this.squares = EnumSet.copyOf(elementsWithoutCenter);
		for (Square square : elementsWithoutCenter) {
			for (LineType type : LineType.values()) {
				Line line = (Line) type.getLineBySquare(square);
				lineToSquares.putIfAbsent(type, new EnumMap((Class<? extends Enum<? extends Line>>) line.getClass()));
				Map<Line, Set<Square>> innerMap = lineToSquares.get(type);
				innerMap.putIfAbsent(line, EnumSet.noneOf(Square.class));
				innerMap.get(line).add(square);
			}
		}
		this.center = center;
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

}
