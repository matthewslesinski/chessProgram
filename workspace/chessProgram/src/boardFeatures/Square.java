package boardFeatures;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import support.BadArgumentException;
import support.UtilityFunctions;

public enum Square {

	A1,
	A2,
	A3,
	A4,
	A5,
	A6,
	A7,
	A8,
	B1,
	B2,
	B3,
	B4,
	B5,
	B6,
	B7,
	B8,
	C1,
	C2,
	C3,
	C4,
	C5,
	C6,
	C7,
	C8,
	D1,
	D2,
	D3,
	D4,
	D5,
	D6,
	D7,
	D8,
	E1,
	E2,
	E3,
	E4,
	E5,
	E6,
	E7,
	E8,
	F1,
	F2,
	F3,
	F4,
	F5,
	F6,
	F7,
	F8,
	G1,
	G2,
	G3,
	G4,
	G5,
	G6,
	G7,
	G8,
	H1,
	H2,
	H3,
	H4,
	H5,
	H6,
	H7,
	H8;
	
	private final File file;
	private final Rank rank;
	private final UpRightDiagonal upRightDiagonal;
	private final DownRightDiagonal downRightDiagonal;
	private final List<Square>[] surroundingLines;
	
	private final List<Square> knightJumps = new LinkedList<>();
	
	private Square() {
		int file = this.ordinal() / 8;
		int rank = this.ordinal() % 8;
		File fileValue = null;
		Rank rankValue = null;
		try {
			fileValue = File.getByIndex(file);
			rankValue = Rank.getByIndex(rank);
		} catch (BadArgumentException e) {
			// TODO log this
			System.exit(1);
		}
		this.file = fileValue;
		this.rank = rankValue;
		this.upRightDiagonal = UpRightDiagonal.getBySquare(this);
		this.downRightDiagonal = DownRightDiagonal.getBySquare(this);
		this.surroundingLines = determineSurroundingLines();
		
		calculateKnightMoves();
		
	}
	
	/**
	 * Calculates all the lists of squares that go outward from the current square along a {@code Line}
	 * @return The array of lists of squares
	 */
	@SuppressWarnings("unchecked")
	private List<Square>[] determineSurroundingLines() {
		return (List<Square>[]) Arrays.asList(Direction.values()).stream()
			.map(dir ->	dir.getMovement().getSquaresToMoveThrough(this, dir.getContainingLineType()))
			.collect(Collectors.toList()).toArray();
	}
	
	/**
	 * Stores in {@code knightJumps} the list of squares a knight can jump to from this one
	 */
	private void calculateKnightMoves() {
		List<List<Integer>> setOfDifferences = Arrays.asList(
			Arrays.asList(2, 1),
			Arrays.asList(2, -1),
			Arrays.asList(1, -2),
			Arrays.asList(-1, -2),
			Arrays.asList(-2, -1),
			Arrays.asList(-2, 1),
			Arrays.asList(-1, 2),
			Arrays.asList(1, 2)
		);
		
		setOfDifferences.forEach(list -> {
			byte fileOffset = (byte) (int) list.get(0);
			byte rankOffset = (byte) (int) list.get(1);
			Square jump = getSquareByOffset(fileOffset, rankOffset);
			if (jump != null) {
				knightJumps.add(jump);
			}
		});
		
	}
	
	/**
	 * Gets the number square this is, in the order it appears in the enum
	 * @return The index
	 */
	public int getIndex() {
		return this.ordinal();
	}
	
	/**
	 * Gets the {@code File} containing this square
	 * @return the {@code File}
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Gets the {@code Rank} containing this square
	 * @return The {@code Rank}
	 */
	public Rank getRank() {
		return this.rank;
	}
	
	/**
	 * Gets the {@code UpRightDiagonal} containing this square
	 * @return the {@code UpRightDiagonal}
	 */
	public UpRightDiagonal getUpRightDiagonal() {
		return this.upRightDiagonal;
	}
	
	/**
	 * Gets the {@code DownRightDiagonal} containing this square
	 * @return The {@code DownRightDiagonal}
	 */
	public DownRightDiagonal getDownRightDiagonal() {
		return this.downRightDiagonal;
	}
	
	/**
	 * Gets the square neighboring the current square in a particular direction
	 * Returns null if the square is out of bounds
	 * @param dir The {@code Direction} the square is in
	 * @return The neighbor or null
	 */
	public Square getNeighbor(Direction dir) {
		return getSquareByOffset(dir.getFileDelta(), dir.getRankDelta());
	}
	
	/**
	 * Returns the square that is {@code fileOffset} files away and {@code rankOffset}
	 * ranks away, or null if it's off the board
	 * @param fileOffset How many files over
	 * @param rankOffset How many ranks over
	 * @return The {@code Square}
	 */
	private Square getSquareByOffset(int fileOffset, int rankOffset) {
		int fileIndex = this.file.getIndex() + fileOffset;
		int rankIndex = this.rank.getIndex() + rankOffset;
		if (!areRealSquareIndices(fileIndex, rankIndex)) {
			return null;
		}
		File file;
		Rank rank;
		try {
			file = File.getByIndex(fileIndex);
			rank = Rank.getByIndex(rankIndex);
		} catch (BadArgumentException e) {
			// TODO log this
			e.printStackTrace();
			System.exit(1);
			return null;
		}
		return getByFileAndRank(file, rank);
	}

	/**
	 * Provides access to the list of squares a knight's jump away from this one
	 * @return The squares
	 */
	public List<Square> getKnightJumps() {
		return knightJumps;
	}
	
	
	/**
	 * Gets the direction traveled to get from this square to another
	 * @param that The target square
	 * @return The direction, or null if the other square is the same as this one or not in a line from this one
	 */
	public Direction getDirectionToSquare(Square that) {
		int fileDelta = that.getFile().getIndex() - this.getFile().getIndex();
		int rankDelta = that.getRank().getIndex() - this.getRank().getIndex();
		// How to determine if the other square is in the same line as this: the magnitudes of
		// the fileDelta and rankDelta must either be the same or one of them must be 0. Therefore, their
		// product's magnitude must be a perfect square
		if (this == that || (double) Math.abs(fileDelta * rankDelta) != Math.pow(fileDelta, 2)) {
			return null;
		}
		return Direction.getByDeltas(UtilityFunctions.getSign(fileDelta), UtilityFunctions.getSign(rankDelta));
	}
	
	/**
	 * Gets a list of squares, from closest to furthest, that proceed from this square in a given direction
	 * @param dir The direction the squares go in
	 * @return The {@code List} of {@code Square}
	 */
	public List<Square> getSquaresInDirection(Direction dir) {
		return surroundingLines[dir.ordinal()];
	}
	
	/**
	 * Returns the element in the array at the index this square corresponds to
	 * @param array The array to access. This should be of size 64
	 * @return The element
	 */
	public <T> T getValueOfSquareInArray(T[] array) {
		return array[this.getFile().getIndex() * 8 + this.getRank().getIndex()];
	}
	
	/**
	 * Gets the square by the intersection of which {@code File} and {@code Rank} it lies on
	 * @param file The file of the square
	 * @param rank The rank of the square
	 * @return The {@code Square} at the intersection
	 */
	public static Square getByFileAndRank(File file, Rank rank) {
		return Square.values()[file.getIndex() * 8 + rank.getIndex()];
	}
	
	/**
	 * Determines if the indices of the rank and file are actually on the board
	 * @param file the index for the file
	 * @param rank the index for the rank
	 * @return Whether the intersection square for the file and rank is on the board
	 */
	public static boolean areRealSquareIndices(int file, int rank) {
		return file >= 0 && rank >= 0 && file <= 7 && file <= 7;
	}
	
	@Override
	public String toString() {
		return file.getHumanReadableForm() + rank.getHumanReadableForm();
	}
	
	
}
