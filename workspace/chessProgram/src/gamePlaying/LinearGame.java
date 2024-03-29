package gamePlaying;

import java.util.LinkedList;
import java.util.List;

import representation.Board;

/**
 * Comprises a complete game
 * @author matthewslesinski
 *
 */
public class LinearGame extends Game {
	
	/** The list of positions that have occurred in this game. The last one is the current position */
	private List<Board> positions;
	
	@Override
	protected void initBoardStore(Board initialBoard) {
		positions = new LinkedList<>();
		addPosition(initialBoard);
	}
	
	@Override
	public Board getCurrentPosition() {
		return positions.get(positions.size() - 1);
	}
	
	@Override
	public Board getLastPosition() {
		return positions.get(positions.size() - 1);
	}
	@Override
	public void undoPlies(int plies) {
		while (plies-- > 0) {
			positions.remove(positions.size() - 1);
		}
	}
	
	@Override
	public int getPlyNumber() {
		return positions.size();
	}
	
	@Override
	public void addPosition(Board board) {
		positions.add(board);
	}
	
	@Override
	public void addVariation(Board board) {
		addPosition(board);
	}

}
