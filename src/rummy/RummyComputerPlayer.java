package rummy;

import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;

import game.Game;
import game.GameComputerPlayer;
import game.GameGui;
import rummy.RummyMoveDraw.DeckType;


/**
 * Player class that is controlled by computer. Implements the RummyPlayer class.
 * 
 * @author Robert
 *
 */
public final class RummyComputerPlayer extends GameComputerPlayer implements RummyPlayer, Runnable {

	// Characteristic items
	private Vector<Card> hand;
	private Vector<JButton> faces;
	private int difficulty;
	private int playerId;
	
	// Status items
	private boolean ready = true;
	private RummyState state;
	
	// Utility items
	private Game game;
	Random rg = new Random();
	
	public RummyComputerPlayer(int difficulty) {
		this.difficulty = difficulty;
		this.hand = null;
		this.game = null;
	}
	
	/**
	 * Set the player id and game.
	 * 
	 * @param game
	 * @param playerId
	 */
	@Override
	public void setGame(Game game, int playerId) {
		this.playerId = playerId;
		this.game = game;
		
	}

	@Override
	public void doRequestMove() {	
		
		// determine whether to draw, meld/layout or discard HERE !!!!!!!!!!!!!
		// how is that information passed to a computer player?
		
		if(state.getCurrentPlayer() == playerId){
			
			// draw card
			
			doDraw();
			// doMelds();
			// doLayouts();
			doDiscard();
			
			// determine meld/layout possibilities
			// discard
			
			
		}
	}
	
	/**
	 * Helper method to draw card.
	 * 
	 * @return none
	 */
	private void doDraw(){
		if(rg.nextInt(2) == 0){
			((RummyGame)game).applyAction(new RummyMoveDraw(this, DeckType.DISCARD));
		}
		else {
			((RummyGame)game).applyAction(new RummyMoveDraw(this, DeckType.STOCK));
		}
	}
	
	/**
	 * Helper method to determine possible melds and play them
	 * 
	 * @return none
	 */
	private void doMelds(){
		
		
	}
	
	/**
	 * Helper method to determine possible layouts and play them
	 * 
	 * @return none
	 */
	private void doLayouts(){
		
		
	}
	
	/**
	 * Helper method to discard.
	 * 
	 * @return none
	 */
	private void doDiscard(){
		((RummyGame)game).applyAction(new RummyMoveDiscard(this, rg.nextInt(hand.size())));
	}
	
	@Override
	public void notYourMove() {
		
		
	}

	@Override
	public void timeToQuit() {
		
		
	}

	@Override
	public void invalidRequest() {
		
		
	}

	@Override
	public void illegalMove() {
		
		
	}

	@Override
	public void gameIsOver() {
		
		
	}

	@Override
	public void stateChanged() {
		this.state = (RummyState) game.getState(this, 0);
//		moveLog = cState.getLog();
//		debug = true;

	}

	@Override
	public void finishUp() {
		
		
	}

	
	
	@Override
	public int getId() {	
		return playerId;
	}

	@Override
	public boolean isReady() {
		return ready;
	}


	@Override
	public void setHand(Vector<Card> newHand) {
		this.hand = new Vector<Card>(newHand);
	}
	
	@Override
	public Vector<Card> getHand(){
		return (Vector<Card>)this.hand.clone();
	}

	
}
