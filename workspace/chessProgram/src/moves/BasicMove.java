package moves;


import boardFeatures.Square;
import gamePlaying.Color;
import pieces.PieceType;
import support.BadArgumentException;

/**
 * An implementation of moves that stores a variable for each quality that could be inquired about.
 * @author matthewslesinski
 *
 */
public class BasicMove implements Move {

	/**
	 * The possible boolean values for isCapture, isCastle, etc... The bit in a compressed version of this
	 * move is set to a value iff the index of that value in this array is the boolean value for that move aspect
	 */
	private static final Boolean[] BOOLEAN_OPTIONS = {false, true};
	
	private final PieceType movingPiece;
	private final boolean isCapture;
	private final PieceType capturedPiece;
	private final Square start;
	private final Square end;
	private final boolean isCastle;
	private final boolean isPromotion;
	private final PieceType promotionPiece;
	private final boolean isEnPassant;
	private final Color movingColor;
	
	/**
	 * Builds a basic move from a compressed version
	 * @param compressedMove The compressed move
	 */
	public BasicMove(int compressedMove) {
		// getObject takes the section of the bit string in compressedMove that corresponds to the MoveBitStringSection, and returns the
		// index of the array specified in the first argument that is equal to that bit string
		movingPiece = getObject(PieceType.values(), MoveBitStringSection.MOVING_PIECE, compressedMove);
		isCapture = getObject(BOOLEAN_OPTIONS, MoveBitStringSection.IS_CAPTURE, compressedMove);
		capturedPiece = isCapture ? getObject(PieceType.values(), MoveBitStringSection.CAPTURE_PIECE, compressedMove) : null;
		start = getObject(Square.values(), MoveBitStringSection.START_SQUARE, compressedMove);
		end = getObject(Square.values(), MoveBitStringSection.END_SQUARE, compressedMove);
		isCastle = getObject(BOOLEAN_OPTIONS, MoveBitStringSection.IS_CASTLE, compressedMove);
		isPromotion = getObject(BOOLEAN_OPTIONS, MoveBitStringSection.IS_PROMOTION, compressedMove);
		promotionPiece = isPromotion ? getObject(PieceType.getPromotionPieces(), MoveBitStringSection.PROMOTION_TYPE, compressedMove) : null;
		isEnPassant = getObject(BOOLEAN_OPTIONS, MoveBitStringSection.IS_EN_PASSANT, compressedMove);
		movingColor = Color.getColor(getObject(BOOLEAN_OPTIONS, MoveBitStringSection.COLOR, compressedMove));
	}
	
	/**
	 * Constructs a {@code BasicMove} from the provided arguments
	 * @param type The type of move
	 * @param movingPiece The type of piece making the move
	 * @param start The start square
	 * @param end The end Square
	 * @param capturedPiece The type of piece being captured, or null if none
	 * @param promotionPiece The type of piece the move promotes a pawn to, or none
	 * @param color The color making the move
	 */
	private BasicMove(MoveType type, PieceType movingPiece, Square start, Square end, PieceType capturedPiece, PieceType promotionPiece, Color color) {
		this.start = start;
		this.movingPiece = movingPiece;
		this.movingColor = color;
		PieceType captureArg = null;
		PieceType promotionArg = null;
		boolean isCaptureArg = false;
		boolean isCastleArg = false;
		boolean isPromotionArg = false;
		boolean isEnPassantArg = false;
		switch (type) {
		case EN_PASSANT:
			isEnPassantArg = true;
		case CAPTURE:
			isCaptureArg = true;
			captureArg = capturedPiece;
			break;
		case CASTLE:
			isCastleArg = true;
			break;
		case PROMOTION_WITH_CAPTURE:
			isCaptureArg = true;
			captureArg = capturedPiece;
			// fall through to promotion
		case PROMOTION:
			isPromotionArg = true;
			promotionArg = promotionPiece;
			break;
		default:
			break;
		}
		// Setting the en passant destination to be the captured pawn's square has been useful, but its usefulness has run its course.
		// Switch to the more natural expression of the end square
		this.end = isEnPassantArg ? calculateEnPassantDestination(end, movingColor) : end;
		this.isCapture = isCaptureArg;
		this.isCastle = isCastleArg;
		this.isPromotion = isPromotionArg;
		this.isEnPassant = isEnPassantArg;
		this.promotionPiece = promotionArg;
		this.capturedPiece = captureArg;
	}
	
	/**
	 * Calculates which {@code Square} the moving pawn for en passant would move to
	 * @param captureSquare The {@code Square} of the pawn getting captured in en passant
	 * @param toMove The {@code Color} of the moving player
	 * @return The {@code Square} the moving pawn would move to
	 */
	private Square calculateEnPassantDestination(Square captureSquare, Color toMove) {
		return Square.getByFileAndRank(captureSquare.getFile(), toMove.getEnPassantDestinationRank());
	}
	
	/**
	 * Retrieves an index from an array, where the index is what the {@code section} of {@code compressedMove} holds
	 * @param options The array to retrieve from
	 * @param section The bit section of the int that has the index
	 * @param compressedMove The compressed representation of this move.
	 * @return
	 */
	private <T> T getObject(T[] options, MoveBitStringSection section, int compressedMove) {
		return options[section.getValue(compressedMove)];
	}
	
	@Override
	public Color getMovingColor() {
		return movingColor;
	}
	
	@Override
	public PieceType getMovingPiece() {
		return movingPiece;
	}
	
	@Override
	public boolean isCapture() {
		return isCapture;
	}
	
	@Override
	public PieceType getCapturedPiece() {
		return capturedPiece;
	}

	@Override
	public boolean isPromotion() {
		return isPromotion;
	}
	
	@Override
	public PieceType getPromotionPiece() {
		return promotionPiece;
	}

	@Override
	public boolean isCastle() {
		return isCastle;
	}

	@Override
	public boolean isEnPassant() {
		return isEnPassant;
	}

	@Override
	public Square getStartSquare() {
		return start;
	}

	@Override
	public Square getEndSquare() {
		return end;
	}

	@Override
	public int compress() {
		int compressedMove = 0;
		compressedMove |= MoveBitStringSection.MOVING_PIECE.setValue(compressedMove, movingPiece.ordinal());
		compressedMove |= MoveBitStringSection.IS_CAPTURE.setValue(compressedMove, isCapture ? 1 : 0);
		compressedMove |= MoveBitStringSection.CAPTURE_PIECE.setValue(compressedMove, capturedPiece == null ? 0 : capturedPiece.ordinal());
		compressedMove |= MoveBitStringSection.START_SQUARE.setValue(compressedMove, start.ordinal());
		compressedMove |= MoveBitStringSection.END_SQUARE.setValue(compressedMove, end.ordinal());
		compressedMove |= MoveBitStringSection.IS_CASTLE.setValue(compressedMove, isCastle ? 1 : 0);
		compressedMove |= MoveBitStringSection.IS_PROMOTION.setValue(compressedMove, isPromotion ? 1 :0);
		compressedMove |= MoveBitStringSection.PROMOTION_TYPE.setValue(compressedMove, promotionPiece == null ? 0 : (promotionPiece.ordinal() - 1));
		compressedMove |= MoveBitStringSection.IS_EN_PASSANT.setValue(compressedMove, isEnPassant ? 1 : 0);
		compressedMove |= MoveBitStringSection.COLOR.setValue(compressedMove, movingColor.isWhite() ? 1 : 0);
		return compressedMove;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BasicMove)) {
			return false;
		}
		if (o == this) {
			return true;
		}
		Move that = (Move) o;
		return this.start == that.getStartSquare() && this.end == that.getEndSquare();
	}
	
	@Override
	public int hashCode() {
		return this.compress();
	}
	
	@Override
	public String toString() {
		return getMoveAsString(true, true);
	}
	
	public static class Builder {
		
		private Square start = null;
		private Square end = null;
		private Color movingColor = null;
		private PieceType movingPiece = null;
		private MoveType type = null;
		private PieceType capturedPiece = null;
		private PieceType promotionPiece = null;
		
		/**
		 * Constructs a {@code BasicMove.Builder} using the essential pieces of information for a move.
		 * @param type The type of move that this is
		 * @param movingPiece The type of piece making the move
		 * @param start The starting square
		 * @param end The ending square
		 * @param movingColor The color of the playermaking the move
		 */
		public Builder(MoveType type, PieceType movingPiece, Square start, Square end, Color movingColor) {
			this.type = type;
			this.movingPiece = movingPiece;
			this.start = start;
			this.end = end;
			this.movingColor = movingColor;
			switch (type) {
			case CASTLE:
				argCheck(movingPiece, PieceType.class, PieceType.KING);
				break;
			case EN_PASSANT:
				capturedPiece = PieceType.PAWN;
				//roll on down to promotion
			case PROMOTION_WITH_CAPTURE:
			case PROMOTION:
				argCheck(movingPiece, PieceType.class, PieceType.PAWN);
				break;
			default:
				break;
			}
			
		}
		
		/**
		 * Specifies the type of piece that this move captures. While en passants involve capturing, though, this should not be called for an en passant
		 * @param capturedPiece The type of captured piece
		 * @return This builder
		 */
		public Builder withCapture(PieceType capturedPiece) {
			argCheck(this.capturedPiece, PieceType.class, null);
			if (this.type == MoveType.CAPTURE || this.type == MoveType.PROMOTION_WITH_CAPTURE) {
				this.capturedPiece = capturedPiece;
			} else {
				throw new BadArgumentException(this.type, MoveType.class, "This move type cannot involve capture a " + capturedPiece.toString());
			}
			return this;
		}
		
		/**
		 * Specifies the type of piece that 
		 * @param promotionPiece
		 * @return
		 */
		public Builder withPromotion(PieceType promotionPiece) {
			argCheck(this.promotionPiece, PieceType.class, null);
			
			if ((this.type == MoveType.PROMOTION || this.type == MoveType.PROMOTION_WITH_CAPTURE) && promotionPiece.isPromotionPiece()) {
				this.promotionPiece = promotionPiece;
			} else {
				throw new BadArgumentException(this.type, MoveType.class, "This move cannot involve promoting to a " + promotionPiece.toString());
			}
			return this;
		}
		
		/**
		 * Hatches this cocoon into a {@code BasicMove}. 
		 * @return The {@code BasicMove}
		 */
		public BasicMove build() {
			PieceType promotionArg = null;
			PieceType capturedArg = null;
			switch (type) {
			case PROMOTION_WITH_CAPTURE:
			case PROMOTION:
				if (this.promotionPiece == null) {
					throw new BadArgumentException(this, Builder.class, "A promotion must involve promoting a to piece");
				}
				promotionArg = this.promotionPiece;
				if (type != MoveType.PROMOTION_WITH_CAPTURE) {
					break;
				}
			case CAPTURE:
			case EN_PASSANT:
				if (this.capturedPiece == null) {
					throw new BadArgumentException(this, Builder.class, "A capture must involve capturing a piece");
				}
				capturedArg = this.capturedPiece;
			default:
				break;
			}
			
			return new BasicMove(type, movingPiece, start, end, capturedArg, promotionArg, movingColor);
		}
		
		
		/**
		 * Makes sure that a variable doesn't get initiated twice
		 * @param toCheck The object that gets initialized
		 * @param objectClass The object's class
		 */
		private static void argCheck(Object toCheck, Class<?> objectClass, Object expected) {
			if (toCheck != expected) {
				throw new BadArgumentException(toCheck, objectClass, expected == null ? "null expected" : "The value was already initialized to " + expected.toString());
			}
		}
	}

}
