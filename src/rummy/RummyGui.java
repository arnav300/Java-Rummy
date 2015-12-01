package rummy;

import java.awt.event.ActionEvent;
import java.util.Vector;

import game.Game;
import game.GameGui;
import game.GamePlayer;
import game.GameState;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;

/**
 * 
 * This class represents all human players. It uses a static factor method to maintain
 * a single instantiation of the class, and keeps track of the number of human users
 * in a game. 
 * 
 * @author Robert
 *
 */
public final class RummyGui extends GameGui {

	private static final long serialVersionUID = 1L;

	private static final int WINDOW_SIZE = 600;
	
	JPanel top; // BorderLayout
	
	// Gui must have access to game state in order
	// to update visuals accordings
	RummyState state;
	
	// Box corresponding to BOTTOM, RIGHT, TOP and LEFT of RummyGui BorderLayout
	Box bottomBox = null;
	Box rightBox = null;
	Box topBox = null;
	Box leftBox = null;
	
	// Player information
	JButton [] playerNames;
	Vector<Card> [] hands;
	JButton [] playerHands;
	Box [] playerBoxHands;	
	
	private int numPlayers;

	private static RummyGui rummyGui = null;
	
	private RummyGui() {
		super();
	}
	
	/**
	 * The singleton factory method. This object represents all human users.
	 * 
	 * @return the singleton object
	 */
	public static GameGui createRummyGui(){
		if(rummyGui == null){
			rummyGui = new RummyGui();
		}
		
		return rummyGui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

	@Override
	protected String defaultTitle() {
		return null;
	}
	
	/**
	 * Responsible for creating AWT/Swing GUI. Depending on number of players
	 * the view will be arranged differently:
	 * 
	 * <=4 players:
	 * |-------------|
	 * |	  3 	 |
	 * | 4		   2 |
	 * |	  1	  	 |
	 * |-------------|
	 * 
	 * == 5 players:
	 * |-------------|
	 * |	4   3 	 |
	 * | 5		   2 |
	 * |	  1	  	 |
	 * |-------------|
	 * 
	 * == 6 players
	 * |-------------|
	 * | 5	  4    3 |
	 * |		     |
	 * | 6	  1	   2 |
	 * |-------------|
	 * 
	 * @param none
	 */
	protected void setGuiMore(){
		
		setSize(WINDOW_SIZE,WINDOW_SIZE);
		setMinimumSize(new Dimension(WINDOW_SIZE + 60,WINDOW_SIZE));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Card in center
		ImageIcon ii = new ImageIcon("images/default/00.gif");
		JButton centerC = new JButton(ii);
		
		Dimension dim = new Dimension(ii.getIconWidth(),ii.getIconHeight());
		centerC.setMinimumSize(dim);
		centerC.setMaximumSize(dim);
		centerC.setHorizontalAlignment(SwingConstants.CENTER);
		centerC.setVerticalAlignment(SwingConstants.CENTER);
		
		Box cardBox = Box.createVerticalBox();
		Box alignmentBox = Box.createHorizontalBox();
		
		
		cardBox.add(Box.createVerticalGlue());
		cardBox.add(centerC, BorderLayout.CENTER);
		cardBox.add(Box.createVerticalGlue());
		
		alignmentBox.add(Box.createHorizontalGlue());
		alignmentBox.add(cardBox);
		alignmentBox.add(Box.createHorizontalGlue());
		
		// Create buttons and register buttons as action listeners
		numPlayers = ((RummyGame)game).getNumberPlayers(); // is this type safe ????
	
		playerNames = new JButton[numPlayers];
		playerBoxHands = new Box[numPlayers];
		playerHands = new JButton[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			
			playerNames[i] = new JButton("Player" + String.valueOf(i + 1));
			playerNames[i].addActionListener(this);
		}
		
		// Always add bottom (using 1 vertical box)
		bottomBox = Box.createVerticalBox();
		playerNames[0].setAlignmentX(Component.CENTER_ALIGNMENT);
		bottomBox.add(Box.createVerticalGlue());
		bottomBox.add(playerNames[0]);
		bottomBox.add(Box.createVerticalGlue());
		
		// Add everybody else (and arrange boxes accordingly)
		if(numPlayers <= 4){
			
			rightBox = Box.createHorizontalBox(); 
			playerNames[1].setAlignmentY(Component.CENTER_ALIGNMENT);
			rightBox.add(Box.createVerticalGlue());
			rightBox.add(playerNames[1]);
			rightBox.add(Box.createVerticalGlue());
			
			topBox = Box.createVerticalBox();
			topBox.add(Box.createVerticalGlue());
			if(numPlayers >= 3){
				playerNames[2].setAlignmentX(Component.CENTER_ALIGNMENT);
				
				topBox.add(playerNames[2]);
				topBox.add(Box.createVerticalGlue());
			}
			
			leftBox = Box.createHorizontalBox();
			leftBox.add(Box.createVerticalGlue());
			if(numPlayers == 4){
				playerNames[3].setAlignmentY(Component.CENTER_ALIGNMENT);
				leftBox.add(playerNames[3]);
				leftBox.add(Box.createVerticalGlue());
				leftBox.setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		}
		else if(numPlayers == 5){
			
			// Right player
			rightBox = Box.createHorizontalBox();
			playerNames[1].setAlignmentY(Component.CENTER_ALIGNMENT);
			rightBox.add(Box.createHorizontalGlue());
			rightBox.add(playerNames[1]);
			rightBox.add(Box.createHorizontalGlue());
			
			// Top players
			topBox = Box.createHorizontalBox();
			Box topRightPlayer = Box.createVerticalBox();
			Box topLeftPlayer = Box.createVerticalBox();
			
			playerNames[2].setAlignmentX(Component.CENTER_ALIGNMENT);
			topRightPlayer.add(Box.createVerticalGlue());
			topRightPlayer.add(playerNames[2]);
			topRightPlayer.add(Box.createVerticalGlue());
			
			playerNames[3].setAlignmentX(Component.CENTER_ALIGNMENT);
			topLeftPlayer.add(Box.createVerticalGlue());
			topLeftPlayer.add(playerNames[3]);
			topLeftPlayer.add(Box.createVerticalGlue());
			
			topBox.add(Box.createHorizontalGlue());
			topBox.add(topRightPlayer);
			topBox.add(Box.createHorizontalGlue());
			topBox.add(topLeftPlayer);
			topBox.add(Box.createHorizontalGlue());
			
			// Left players
			leftBox = Box.createHorizontalBox();
			playerNames[4].setAlignmentY(Component.CENTER_ALIGNMENT);
			leftBox.add(Box.createHorizontalGlue());
			leftBox.add(playerNames[4]);
			leftBox.add(Box.createHorizontalGlue());
			
		}
		else if(numPlayers == 6){
			
			// Right Players
			rightBox = Box.createVerticalBox();
			Box topRightBox = Box.createHorizontalBox();
			Box bottomRightBox = Box.createHorizontalBox();
			
			playerNames[1].setAlignmentY(Component.BOTTOM_ALIGNMENT);
			bottomRightBox.add(Box.createHorizontalGlue());
			bottomRightBox.add(playerNames[1]);
			bottomRightBox.add(Box.createHorizontalGlue());
			
			playerNames[2].setAlignmentY(Component.TOP_ALIGNMENT);
			topRightBox.add(Box.createHorizontalGlue());
			topRightBox.add(playerNames[2]);
			topRightBox.add(Box.createHorizontalGlue());
			
			rightBox.add(Box.createVerticalGlue());
			rightBox.add(bottomRightBox);
			rightBox.add(Box.createVerticalGlue());
			rightBox.add(topRightBox);
			rightBox.add(Box.createVerticalGlue());
			
			// Top player
			topBox = Box.createVerticalBox();
			playerNames[3].setAlignmentX(Component.CENTER_ALIGNMENT);
			topBox.add(Box.createVerticalGlue());
			topBox.add(playerNames[3]);
			topBox.add(Box.createVerticalGlue());
			
			// Left players
			leftBox = Box.createVerticalBox();
			Box topLeftBox = Box.createHorizontalBox();
			Box bottomLeftBox = Box.createHorizontalBox();
			
			topLeftBox.add(Box.createHorizontalGlue());
			topLeftBox.add(playerNames[4]);
			topLeftBox.add(Box.createHorizontalGlue());
			
			bottomLeftBox.add(Box.createHorizontalGlue());
			bottomLeftBox.add(playerNames[5]);
			bottomLeftBox.add(Box.createHorizontalGlue());
			
			leftBox.add(Box.createVerticalGlue());
			leftBox.add(bottomLeftBox);
			leftBox.add(Box.createVerticalGlue());
			leftBox.add(topLeftBox);
			leftBox.add(Box.createVerticalGlue());
			
		}
		
		// Add player boxes to content pane
		top.add(alignmentBox, BorderLayout.CENTER);
		top.add(bottomBox, BorderLayout.SOUTH);
		top.add(rightBox, BorderLayout.EAST);
		top.add(topBox, BorderLayout.NORTH);
		top.add(leftBox, BorderLayout.WEST);
		
	}

	/**
	 * Returns the component that holds this game's
	 * specific GUI
	 */
	protected Component createApplComponent(){
		top = new JPanel(new BorderLayout());
		return top;
	}
	
	/**
	 * Is called when the state is changed.  Places all the
	 * the buttons in the appropriate box and updates the
	 * text of the move log and chat box
	 */
	public void stateChanged() { 
	
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
    /**
     * Updates Gui according to state
     */
    public void stateChanged(GameState state){
    	//places the panels on the correct border
    	//is only called once
    	
//    	if(setPOV){
//    		initPOV(cState);
//    	}
//    	
    	
    	//update the two logs
    	//moveLog.setText(setMoveText(state.getLog()));
    	//convo.setText(setChatText(state.getChat()));
    	
    	//makes sure the player has to pick a card before asking
    	for(int i = 0; i < numPlayers; i++){
    		if( i != ((RummyState)state).getCurrentPlayer()) 
    			playerNames[i].setEnabled(false);
    	}

    	
    	for(int i = 0;i < numPlayers; i++){
    		playerBoxHands[i].removeAll();
    		hands[i].clear();
    		for(Card c: this.hands[i]){
    			ImageIcon cardFace = new ImageIcon(c.getImage());
    			if(this.hands[i].size()>10){
    				int cWidth = WINDOW_SIZE/this.hands[i].size();
    				//show the front of the card
    				Image img = cardFace.getImage();
    				img = img.getScaledInstance(cWidth,(int) (cWidth*1.3), Image.SCALE_FAST);
    				cardFace = new ImageIcon(img);
    			}
    			JButton temp = new JButton(cardFace);
    			Dimension dim = new Dimension(cardFace.getIconWidth(), cardFace.getIconHeight());
    			temp.setPreferredSize(dim);
    			temp.setMaximumSize(dim);
    			temp.setMinimumSize(dim);
    			temp.addActionListener(this);
    			this.playerBoxHands[i].add(temp);
    		}
    	}
    	
    	if(numPlayers <= 4){
    		bottomBox.add(this.playerBoxHands[0]);
    		rightBox.add(this.playerBoxHands[1]);
    		if(numPlayers == 3){
    			topBox.add(this.playerBoxHands[2]);
    		}
    		else if(numPlayers == 4){
    			leftBox.add(this.playerBoxHands[3]);
    		}
    		
    	}
    	else if(numPlayers == 5){
    		bottomBox.add(this.playerBoxHands[0]);
    		rightBox.add(this.playerBoxHands[1]);
    		topBox.add(this.playerBoxHands[2]);
    		topBox.add(this.playerBoxHands[3]);
    		leftBox.add(this.playerBoxHands[4]);
    	}
    	else if(numPlayers == 6){
    		bottomBox.add(this.playerBoxHands[0]);
    		rightBox.add(this.playerBoxHands[1]);
    		rightBox.add(this.playerBoxHands[2]);
    		topBox.add(this.playerBoxHands[3]);
    		leftBox.add(this.playerBoxHands[4]);
    		leftBox.add(this.playerBoxHands[5]);
    	}
    	
    }
    

}

