package immutableArrayBoard;

import representation.BoardBuilder;
import representation.MoveMaker;

public class ImmutableArrayBoardMoveMaker extends MoveMaker<ImmutableArrayBoard> {

	@Override
	protected BoardBuilder<ImmutableArrayBoard> getNewBuilderFromBoard(ImmutableArrayBoard board) {
		return ImmutableArrayBoard.Builder.fromBoard(board);
	}
}
