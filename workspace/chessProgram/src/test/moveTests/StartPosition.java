package test.moveTests;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.File;
import boardFeatures.Rank;
import boardFeatures.Square;
import immutableArrayBoard.ImmutableArrayBoard;
import representation.Board;

/**
 * Simple class to test out moves on some positions.
 * TODO: create more comprehensive tests using JUnit.
 */
public class StartPosition {
	
	static void tester() {
		String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

		File aFile = File.A;
		
//		List<Square> containedSquares = Arrays.asList(File.values()).stream()
//				.map(file -> Square.getByFileAndRank(file, Rank.EIGHT))
//				.collect(Collectors.toList());
//		System.exit(1);
//		Board startPosition = new ImmutableArrayBoard.Builder(startFen).build();
//		System.out.println(startPosition);
	}
	
	public static void main(String[] args) {
		tester();
	}
}