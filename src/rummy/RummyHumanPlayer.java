package rummy;

import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;

import game.Game;
import game.GameGui;
import game.GameHumanPlayer;

public final class RummyHumanPlayer extends GameHumanPlayer implements RummyPlayer {

	// Composite items
	private Vector<Card> hand;
	private Vector<JButton> faces;
	private Game game;
	private int playerId;

	// Status items
	private boolean ready;
	private RummyState state;

	public RummyHumanPlayer() {
		this.hand = null;
		this.game = null;
		this.state = null;
	}

	/**
	 * Lets the player know what the game object is.
	 * 
	 * @param game
	 *            the game object
	 * @param playerId
	 *            the unique numeric player-id assigned to this player by the
	 *            game
	 */
	public void setGame(Game game, int playerId) {
		this.game = game;
		this.playerId = playerId;
		ready = true;
	}

	@Override
	public void requestMove(){
		
		
		
		
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
		
		if(this.state.getCurrentPlayer() == playerId){
			Stack<Card> stock = (Stack<Card>)this.state.getStock();
			if(stock != null){
				hand.add(stock.pop());
			}
		}
	}

	@Override
	public void finishUp() {

	}

	/**
	 * Gets the player's numeric id from the pespective of the game it's playing
	 * in. (E.g., 0 if player 0, 1 if player 1, etc.)
	 *
	 * @return the player's numeric id, from the game's viewpoint.
	 */
	public int getId() {
		return playerId;
	}

	/**
	 * Tells whether the player is ready to play the game.
	 *
	 * @return a boolean value indicating whether the player is ready to play.
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Set player hand
	 * 
	 * @return none
	 */
	public void setHand(Vector<Card> newHand) {
		hand = newHand;
	}

	/**
	 * Return a copy of the player's hand
	 * 
	 * @return
	 */
	public Vector<Card> getHand(){
		return (Vector<Card>)this.hand.clone();
	}
}
