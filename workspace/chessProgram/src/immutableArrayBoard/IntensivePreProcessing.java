package immutableArrayBoard;

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
import moves.DecompressedBoard;
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

	/**
	 * Acting like a list and backed by an array, this keeps a list of squares that you can add to, up to the maxSize,
	 * but can't remove from. Therefore, this is able to keep track of where the squares are placed, so it can quickly
	 * get neighbors as well as the index of a particular square.
	 * @author matthewslesinski
	 *
	 */
	private static class FixedSizeList {
		private final Square[] squares;
		private final int[] indices = new int[Square.values().length];
		int size = 0;
		
		private FixedSizeList(int maxSize) {
			this.squares = new Square[maxSize];
		}
		
		/**
		 * Adds a square to this list
		 * @param square The square to add
		 */
		public void add(Square square) {
			if (size >= squares.length) {
				throw new BadArgumentException(square, FixedSizeList.class, "Can't add too many squares to this list");
			}
			squares[size] = square;
			indices[square.getIndex()] = size++;
		}
		
		/**
		 * Does this list contain the square, or in other words, does this square have a piece?
		 * @param square The square to check
		 * @return if it's in this list
		 */
		public boolean contains(Square square) {
			return squares[indices[square.getIndex()]] == square;
		}
		
		/**
		 * Gets the square in this list that is a certain number of other squares removed from the current one
		 * @param square The current square
		 * @param diff The number of squares removed the other one is
		 * @return The square a certain number of other squares removed
		 */
		public Square getNeighbor(Square square, int diff) {
			if (!contains(square)) {
				throw new BadArgumentException(square, FixedSizeList.class, "This square is not in this list");
			}
			int index = indices[square.getIndex()] + diff;
			if (index < 0 || index >= size) {
				return null;
			}
			return squares[index];
		}
		
	}
	
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
	private final FixedSizeList[] fileSquares = new FixedSizeList[File.values().length];
	private final FixedSizeList[] rankSquares = new FixedSizeList[Rank.values().length];
	private final FixedSizeList[] upRightSquares = new FixedSizeList[UpRightDiagonal.values().length];
	private final FixedSizeList[] downRightSquares = new FixedSizeList[DownRightDiagonal.values().length];
	
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
		initializeLists(fileSquares, index -> new FixedSizeList(8));
		initializeLists(rankSquares, index -> new FixedSizeList(8));
		initializeLists(upRightSquares, index -> new FixedSizeList(UpRightDiagonal.getByIndex(index).getLength()));
		initializeLists(downRightSquares, index -> new FixedSizeList(DownRightDiagonal.getByIndex(index).getLength()));
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
		FixedSizeList[] listArray = null;
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
		
		FixedSizeList listOfSquares = listArray[indexOfContainingLine];
		if (!listOfSquares.contains(square)) {
			throw new BadArgumentException(square, Square.class, "This square does not have a piece on it");
		}
		
		return listOfSquares.getNeighbor(square, numberOfSteps);
		
		
	}
}
