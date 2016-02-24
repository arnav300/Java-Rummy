package rummy;

import game.GamePlayer;

/**
 * Class to represent a player's draw. Player chooses stock or discard pile.
 * 
 * @author Robert
 *
 */
public class RummyMoveDraw extends RummyMoveAction {
	enum DeckType {STOCK, DISCARD};
	DeckType moveDeckType;
	
	public RummyMoveDraw(GamePlayer source, DeckType d ) {
		super(source);
		moveDeckType = d;
	}

}
