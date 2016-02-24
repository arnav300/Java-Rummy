package rummy;

import game.GamePlayer;

public class RummyMoveMeld extends RummyMoveAction {

	private int [] indices = null;
	
	public RummyMoveMeld(GamePlayer source, int [] indices ) {
		super(source);
		// TODO Auto-generated constructor stub
		this.indices = indices;
		
	}

	public int [] getIndices(){
		return this.indices;
	}

}
