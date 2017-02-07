package rummy;

import java.util.Vector;

import game.GamePlayer;

public class RummyMoveMeld extends RummyMoveAction {

	private Vector<Card> meld= null;
	
	public RummyMoveMeld(GamePlayer source, Vector<Card> meld) {
		super(source);
		this.meld = meld;
	}

	public Vector<Card> getMeld(){
		return this.meld;
	}

}
