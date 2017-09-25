package gamePlaying;


import java.util.Arrays;

import independentDataStructures.Tree;
import moves.Move;
import representation.Board;
import static support.UtilityFunctions.*;

/**
 * Comprises a complete game
 * @author matthewslesinski
 *
 */
public class GameTree extends Game {
	
	/** The list of positions that have occurred in this game. The last one is the current position */
	private Tree<Move, Board> positions;
	
	@Override
	protected void initBoardStore(Board initialBoard) {
		positions = new Tree<Move, Board>(initialBoard);
	}
	
	@Override
	public Board getCurrentPosition() {
		return positions.getCurrentNodeValue();
	}
	
	@Override
	public Board getLastPosition() {
		return positions.getMainLeafValue();
	}
	
	@Override
	public void undoPlies(int plies) {
		Arrays.asList(getRange(0, plies)).forEach(i -> positions.moveUp());
	}
	
	@Override
	public int getPlyNumber() {
		return positions.getCurrentDepth();
	}
	
	@Override
	public void addPosition(Board board) {
		positions.addMainChild(board.lastMove(), board);
		positions.moveDown();
	}
	
	@Override
	public void addVariation(Board board) {
		positions.addChild(board.lastMove(), board);
		positions.moveDown(board.lastMove());
	}
}
