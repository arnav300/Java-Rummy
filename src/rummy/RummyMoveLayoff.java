package rummy;

import javax.swing.JButton;

import game.GamePlayer;

public class RummyMoveLayoff extends RummyMoveAction {

	private JButton meldButton = null;
	private int cardIndex = -1;
	private int meldIndex = -1;
	
	public RummyMoveLayoff(GamePlayer source, JButton meldButton, int meldIndex, int cardIndex) {
		super(source);
		this.meldButton = meldButton;
		this.cardIndex = cardIndex;
		this.meldIndex = meldIndex;
	}

	public JButton getMeldButton(){
		return this.meldButton;
	}
	
	public int getCardIndex(){
		return this.cardIndex;
	}
	
	public int getMeldIndex(){
		return this.meldIndex;
	}
	
}
