package rummy;

import game.GameMoveAction;
import game.GamePlayer;

public abstract class RummyMoveAction extends GameMoveAction {

	public RummyMoveAction(GamePlayer source) {
		super(source);
	}

	public boolean isMove(){
		return true;
	}
	
}
