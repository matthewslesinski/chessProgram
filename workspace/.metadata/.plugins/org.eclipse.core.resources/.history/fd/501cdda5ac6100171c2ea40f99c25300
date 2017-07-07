package moves;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import boardFeatures.Direction;
import boardFeatures.DownRightDiagonal;
import boardFeatures.File;
import boardFeatures.Rank;
import boardFeatures.Square;
import boardFeatures.UpRightDiagonal;
import gamePlaying.Color;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;
import support.BadArgumentException;

/**
 * Used to preprocess a board before calculating the moves, so that calculating them is easier. The main benefits of this class
 * are that it can provide the basic info about the board, such as which piece is on a square, the player to move, castling abilities,
 * and en passant abilities, but it can also list all the squares a piece of particular type and color is on, as well as efficiently
 * retrieving the next square(s) from any particular square in a particular direction that has a piece on it.
 * @author matthewslesinski
 *
 */
public class IntensivePreProcessing<B extends Board> implements DecompressedBoard<B> {
	private static final int NUMBER_OF_CASTLING_RIGHTS = CastlingRights.values().length;
	
	/** The color to move */
	private final Color toMove;
	
	/** Array of the booleans for each castling right, for if it's allowed without accounting for specifics of the position */
	private final boolean[] castlingRights = new boolean[CastlingRights.values().length];
	
	/** The file to capture onto with en passant, or null if not allowed */
	private final File enPassantFile;
	
	/** Stores the pieces on the board in the most intuitive arrangement. Each index of the array will be the piece at the square with that index as its index */
	private final Piece[] pieces = new Piece[Square.values().length];
	@SuppressWarnings("unchecked")
	
	/**
	 * Stores the pieces on the board in another intuitive arrangement. Each index of the array
	 * will be the list with the squares that the piece with that ordinal (minus 1 because Piece.NONE is not included) is on
	 */
	private final List<Square>[] piecesToSquares = new List[Piece.values().length - 1];
	
	/** The four following arrays of lists contain the squares with pieces on them for each of the instances of a line type */
	private final ImportantSquareList[] fileSquares = new ImportantSquareList[File.values().length];
	private final ImportantSquareList[] rankSquares = new ImportantSquareList[Rank.values().length];
	private final ImportantSquareList[] upRightSquares = new ImportantSquareList[UpRightDiagonal.values().length];
	private final ImportantSquareList[] downRightSquares = new ImportantSquareList[DownRightDiagonal.values().length];
	
	/**
	 * Initiates the preprocessing
	 * @param board The board to decrompress
	 */
	public IntensivePreProcessing(B board) {
		toMove = board.whoseMove();
		for (int i = 0; i < NUMBER_OF_CASTLING_RIGHTS; i++) {
			castlingRights[i] = board.canCastle(CastlingRights.values()[i]);
		}
		enPassantFile = board.enPassantCaptureFile();
		
		initializeLists(piecesToSquares, index -> new ArrayList<Square>(10));
		initializeLists(fileSquares, index -> new FixedSizeList(8, Direction.UP));
		initializeLists(rankSquares, index -> new FixedSizeList(8, Direction.RIGHT));
		initializeLists(upRightSquares, index -> new FixedSizeList(UpRightDiagonal.getByIndex(index).getLength(), Direction.UP_RIGHT));
		initializeLists(downRightSquares, index -> new FixedSizeList(DownRightDiagonal.getByIndex(index).getLength(), Direction.DOWN_RIGHT));
		preProcessBoard(board);
		
	}
	
	/**
	 * Initializes all list in the appropriate array, using the index of the array that each list will be in
	 * @param arr The array to put the lists in
	 * @param constructor How to initialize each list
	 */
	private <T> void initializeLists(T[] arr, Function<Integer, T> constructor) {
		int length = arr.length;
		for (int i = 0; i < length; i++) {
			arr[i] = constructor.apply(i);
		}
	}
	
	/**
	 * Finishes the preprocessing by, for each square with a piece on it, adding that square and its piece to
	 * each of the lists
	 * @param board
	 */
	private void preProcessBoard(B board) {
		for (Square square : Square.values()) {
			Piece piece = board.getPieceAtSquare(square);
			if (piece != Piece.NONE ) {
				pieces[square.getIndex()] = piece;
				piecesToSquares[piece.ordinal() - 1].add(square);
				fileSquares[square.getFile().getIndex()].add(square);
				rankSquares[square.getRank().getIndex()].add(square);
				upRightSquares[square.getUpRightDiagonal().getIndex()].add(square);
				downRightSquares[square.getDownRightDiagonal().getIndex()].add(square);
			}
		}
	}
	
	@Override
	public Color whoseMove() {
		return toMove;
	}
	
	@Override
	public boolean canCastle(CastlingRights right) {
		return castlingRights[right.getIndex()];
	}
	
	@Override
	public File getEnPassantFile() {
		return enPassantFile;
	}
	
	@Override
	public List<Square> getListOfSquaresForPiece(Piece piece) {
		if (piece == Piece.NONE) {
			throw new BadArgumentException(piece, Piece.class, "An absent piece cannot be associated with a square");
		}
		return piecesToSquares[piece.ordinal() - 1];
	}
	
	@Override
	public Piece getPieceAtSquare(Square square) {
		return square.getValueOfSquareInArray(pieces);
	}
	
	@Override
	public Square getNPiecesAway(Square square, Direction dir, int n) {
		if (n < 0) {
			throw new BadArgumentException(n, Integer.class, "Steps away can't be negative");
		}
		int sign = dir.getMovement().getIncrement();
		int numberOfSteps = n * sign;
		int indexOfContainingLine;
		ImportantSquareList[] listArray = null;
		switch (dir) {
		case DOWN:
		case UP:
			indexOfContainingLine = square.getFile().getIndex();
			listArray = fileSquares;
			break;
		case DOWN_LEFT:
		case UP_RIGHT:
			indexOfContainingLine = square.getUpRightDiagonal().getIndex();
			listArray = upRightSquares;
			break;
		case DOWN_RIGHT:
		case UP_LEFT:
			indexOfContainingLine = square.getDownRightDiagonal().getIndex();
			listArray = downRightSquares;
			break;
		case LEFT:
		case RIGHT:
			indexOfContainingLine = square.getRank().getIndex();
			listArray = rankSquares;
			break;
		default:
			throw new BadArgumentException(dir, Direction.class, "Can't get n pieces away if we don't have a direction to go by");
		}
		
		ImportantSquareList listOfSquares = listArray[indexOfContainingLine];
		
		return listOfSquares.getNeighbor(square, numberOfSteps);
	}
}
