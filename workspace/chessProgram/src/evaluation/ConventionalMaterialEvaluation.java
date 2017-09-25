package evaluation;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import gamePlaying.Color;
import pieces.Piece;
import pieces.PieceType;
import representation.Board;
import static support.UtilityFunctions.*;

/**
 * Evaluates a board only by adding up the material on each side according to the conventional (commonly taught) evaluations for pieces.
 * For instance, pawn=1, knight/bishop=3, rook=5, and queen=9
 * @author matthewslesinski
 *
 */
public class ConventionalMaterialEvaluation implements Evaluator {

	/** Holds a mapping from type of piece to value */
	private static final Map<Piece, Double> MATERIAL_VALUES = new EnumMap<Piece, Double>(Piece.class);
	/** Populates the map */
	static {
		initializeMaterialValues();
	}
	
	/**
	 * Initializes the internal map of piece's material values.
	 */
	private static void initializeMaterialValues() {
		Arrays.stream(Color.values()).forEach(color -> {
			int multiplier = color.isWhite() ? 1 : -1;
			Arrays.stream(PieceType.values()).forEach(type -> {
				Piece piece = Piece.getByColorAndType(color, type);
				MATERIAL_VALUES.put(piece, multiplier * type.getConventionalEvaluation());
			});
		});
	}
	
	@Override
	public double evaluateBoard(Board board) {
		return Arrays.stream(board.toPieceArray()).map(bindAtEnd(MATERIAL_VALUES::getOrDefault,  0.)).reduce(0., Double::sum);
	}

}
