package rummy;

import game.GamePlayer;

/**
 * This class represents a player's action to discard a card.
 * It saves the index of the card to discard.
 * 
 * @author Robert
 *
 */
public class RummyMoveDiscard extends RummyMoveAction {

	private int index;
	
	public RummyMoveDiscard(GamePlayer source, int index) {
		super(source);
		this.index = index;
	}
	
	public int getDiscardIndex(){
		return this.index;
	}

}
