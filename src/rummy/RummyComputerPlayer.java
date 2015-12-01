package rummy;

import java.util.Vector;

import javax.swing.JButton;

import game.Game;
import game.GameGui;


/**
 * Player class that is controlled by computer. Implements the RummyPlayer class.
 * 
 * @author Robert
 *
 */
public final class RummyComputerPlayer implements RummyPlayer, Runnable {

	// Composite items
	private Vector<Card> hand;
	private Vector<JButton> faces;
	private Game game;
	private int difficulty;
	private int playerId;
	
	// Status items
	private boolean ready = true;
	private RummyState state;
	
	
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
	public void requestMove() {
		
		
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
		state = (RummyState) game.getState(this, 0);
		if(state.getCurrentPlayer() == playerId){
		
			// do move
			
			
			
		}
		
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
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
