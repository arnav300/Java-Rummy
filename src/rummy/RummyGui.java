package rummy;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import game.GameGui;
import game.GameState;
import rummy.RummyMoveDraw.DeckType;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

/**
 * 
 * This class represents all human players. It uses a static factor method to maintain
 * a single instance of the class, and keeps track of the number of human users
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
	RummyState state = null;
	
	// Boxes corresponding to position in Border Layout
	private final int SOUTH = 0; // anyway to turn these into an enum?
	private final int EAST = 1;
	private final int NORTH = 2;
	private final int WEST = 3;
	Box [] positionBoxes = new Box[4]; // indices are SOUTH - 0, EAST - 1, NORTH - 2, WEST - 3, values are player IDs
	Box centerBox = null; // CENTER
	Box meldButtonsBox = null;
	
	// Center stuff - melds/layouts, stock/discard, game message text
	Box cardBox = null;
	JPanel meldGridBag;
	GridBagConstraints meldConstraints;
	Vector<Box> melds;
	JButton stockTop;
	JButton discardPileTop;
	
	/**
	 * Class to handle action events for stockTop and discardPileTop.
	 * @author Robert
	 *
	 */
	@SuppressWarnings("serial")
	private class AbstractDrawAction extends AbstractAction {
		
		@Override
		public void actionPerformed(ActionEvent e){

			Object source = e.getSource();
			RummyPlayer rp = getCurrentPlayer();
			
			if(rp instanceof RummyHumanPlayer){
				if(draw == true){ // Draw
					if(source == stockTop || source == discardPileTop) {
						((RummyGame)game).applyAction(new RummyMoveDraw(rp, (source == stockTop) ? DeckType.STOCK : DeckType.DISCARD));
						move = true;
						draw = false;
						meldLayoutBtn.setEnabled(true);
					}
				}
			}	
			
		}
	}
	AbstractDrawAction abstractDrawAction;
	
	JTextField gameMessageText;
	JButton meldLayoutBtn;
	JButton doMeldBtn;
	Vector<Card> theMeld;
	/**
	 * Class to handle meld related action events.
	 * @author Robert
	 *
	 */
	@SuppressWarnings("serial")
	private class AbstractMoveActions extends AbstractAction {
		
	
		/**
		 * Helper method to get the index of the card corresponding
		 * to the selected JButton 
		 * 
		 * @param the card id (i.e. a number from 1-52)
		 * @return -1 if source doesn't exist in the hand, else an Integer
		 * 			containing the index of the source in the hand.
		 */
		private int getSelectedCardIndex(int ID){
			
			int index = -1;
			String value = String.valueOf(ID);
			int len = playerBoxHands[SOUTH].getComponentCount();
			for(int i = 0; i < len; i++){
				if(value.equals((playerBoxHands[SOUTH].getComponent(i)).getName()) ) {
					index = i;
					break;
				}
			}
			
			return index;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {

			Object source = e.getSource();
			RummyPlayer rp = getCurrentPlayer();
			
			if(rp instanceof RummyHumanPlayer){
				if(move == true) { // Meld/Layout/Discard
					if(meldLayout == true){
						if(source == meldLayoutBtn){ // Exit meld/layout mode
							theMeld.removeAllElements();
							meldLayoutBtn.setText("Meld/Layout");
							doMeldBtn.setEnabled(false);
							gameMessageText.setText("Player " + getCurrentPlayer().getId() + " didn't select cards for a meld!");
							meldLayout = false;
						}
						else if(source == doMeldBtn){ // do meld
							
							int [] indices = new int[theMeld.size()];
							for(int i = 0; i < theMeld.size(); i++){
								indices[i] = getSelectedCardIndex(theMeld.elementAt(i).getID());
							}
							
							((RummyGame)game).applyAction(new RummyMoveMeld(rp, indices));
							
							theMeld.removeAllElements();
							meldLayoutBtn.setText("Meld/Layout");
							doMeldBtn.setEnabled(false);
							gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to make a move.");
							meldLayout = false;
						}
						else if( ((JButton)source).getParent().getClass() == Box.class && 
								(Box) ((JButton)source).getParent() == playerBoxHands[SOUTH]){ // collect cards for meld
									
							Card value = new Card( Integer.valueOf(((JButton)source).getName()) );
							if( theMeld.contains(value) != true){
								theMeld.addElement(value);
								gameMessageText.setText(gameMessageText.getText() + value.getName() + " ");
							}
							else {
								// TODO
								theMeld.remove(value); // still need to remove card name from gameMessageText !!!!!!
							}
						}
						else if ( ((JPanel)((JButton)source).getParent()) == meldGridBag) { // do layout
							
							// Check if valid layout ?
							//((RummyGame)game).applyAction(new RummyMoveMeld(rp, indices));
							
							theMeld.removeAllElements();
							meldLayoutBtn.setText("Meld/Layout");
							doMeldBtn.setEnabled(false);
							gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to make a move.");
							meldLayout = false;
							
						}

					}
					else if (meldLayout == false){
						
						if( (Box) ((JButton)source).getParent() == playerBoxHands[SOUTH]) { // Discard

							int index = getSelectedCardIndex( Integer.valueOf(((JButton)source).getName()) );

							((RummyGame)game).applyAction(new RummyMoveDiscard(rp, index));
							meldLayoutBtn.setEnabled(false);
							move = false;
							draw = true;
						}
						else if (source == meldLayoutBtn ){ // Enter Meld/Layout mode
							meldLayoutBtn.setText("Exit " + meldLayoutBtn.getText());
							doMeldBtn.setEnabled(true);
							gameMessageText.setText(gameMessageText.getText() + "\nChoose cards to meld/layout: ");
							meldLayout = true;
						}

					}
				}
				
			}
		}
	}

	AbstractMoveActions abstractMoveActions;
	
	// Move/Chat Log
	JTextField chatField;
	JTextArea moveLog;
	Box logsBox;
	JButton sendMessageBtn;
	/**
	 * Class to handle chat field/move log related action events. 
	 * @author Robert
	 *
	 */
	@SuppressWarnings("serial")
	private class AbstractSendMessageAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveLog.setText(moveLog.getText() + "\n" + chatField.getText());
			moveLog.setCaretPosition(moveLog.getDocument().getLength());
			chatField.setText("");
			
		}
	}
	
	// Player Info - indices are player positions
	JButton [] playerNames; 
	JButton [] playerHands; 
	Box [] playerBoxHands; 
	
	// Player Info - indices are player IDs
	Vector<Vector<Card> > hands;
	int [] playerPosition; 
	RummyPlayer [] players;
	
	
	// Move
	private boolean draw = true;
	private boolean move = false;
	private boolean meldLayout = false;
	
	private int numPlayers;

	private static RummyGui rummyGui = null;
	
	private RummyGui() {
		super();
	}
	
	/**
	 * The singleton factory method.
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
	protected String defaultTitle() {
		return null;  
	}
	
	/**
	 * Responsible for creating AWT/Swing GUI. Depending on number of players
	 * the view will be arranged differently:
	 * 
	 * <= 4 players:
	 * |-------------|
	 * |	  3 	 |
	 * | 4		   2 |
	 * |	  1	  	 |
	 * |-------------|
	 * 
	 * @param none
	 */
	protected void setGameMore(){
		
		setSize(WINDOW_SIZE,WINDOW_SIZE);
		setMinimumSize(new Dimension(WINDOW_SIZE + 60,WINDOW_SIZE));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Card in center
		abstractDrawAction = new AbstractDrawAction();
		ImageIcon temp = new ImageIcon("images/default/00.gif");
		this.stockTop = new JButton(temp);
		Dimension dim = new Dimension(temp.getIconWidth(), temp.getIconHeight());
		this.stockTop.setMinimumSize(dim);
		this.stockTop.setMaximumSize(dim);
		this.stockTop.setEnabled(false);
		this.stockTop.addActionListener(abstractDrawAction);
		
		// ... set image of discard pile in stateChanged()
		
	
		// Meld utilities
//		this.MeldBox = 
		this.theMeld = new Vector<Card>();
		this.melds = new Vector<Box>();
		
		// Center info
		abstractMoveActions = new AbstractMoveActions();
		this.centerBox = Box.createVerticalBox();
		this.meldButtonsBox = Box.createHorizontalBox();
		this.doMeldBtn = new JButton("Play Meld");
		this.doMeldBtn.setEnabled(false);
		this.doMeldBtn.addActionListener(abstractMoveActions);
		this.meldGridBag = new JPanel();
		this.meldGridBag.setLayout(new GridBagLayout());
		this.meldGridBag.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));
		this.meldConstraints = new GridBagConstraints();
		this.meldConstraints.ipadx = 2;
		this.meldConstraints.ipady = 2;
		this.cardBox = Box.createHorizontalBox();
		this.gameMessageText = new JTextField();
		this.gameMessageText.setBorder(null);
		this.meldLayoutBtn = new JButton("Meld/Layout");
		this.meldLayoutBtn.setEnabled(false);
		this.meldLayoutBtn.addActionListener(abstractMoveActions);
		this.meldLayoutBtn.addKeyListener(this);
		
		this.centerBox.add(Box.createVerticalStrut(10));
		this.centerBox.add(this.meldGridBag);
		this.centerBox.add(Box.createVerticalStrut(10));
		this.centerBox.add(this.cardBox);
		this.centerBox.add(Box.createVerticalStrut(5));
		this.centerBox.add(this.gameMessageText);
		this.centerBox.add(Box.createVerticalStrut(5));
		this.meldButtonsBox.setAlignmentX(Box.CENTER_ALIGNMENT);
		this.meldButtonsBox.add(this.meldLayoutBtn);
		this.meldButtonsBox.add(Box.createHorizontalStrut(5));
		this.meldButtonsBox.add(this.doMeldBtn);
		this.centerBox.add(this.meldButtonsBox);
		this.centerBox.add(Box.createVerticalStrut(10));
		
		// Get game information and allocate buttons
		numPlayers = ((RummyGame)game).getNumberPlayers(); 
		
		this.playerNames = new JButton[numPlayers];
		this.playerHands = new JButton[numPlayers];
		this.playerBoxHands = new Box[numPlayers];
		this.playerPosition = new int[numPlayers];
		
		// Initialize buttons and boxes for all players
		for(int i = 0; i < numPlayers; i++) {
			this.playerNames[i] = new JButton("Player" + String.valueOf(i + 1));
			this.playerNames[i].addActionListener(this);
			this.playerPosition[i] = i; // default positions are player ids
			
			if( i == WEST || i == EAST) { 
				
				this.positionBoxes[i] = Box.createHorizontalBox();
				this.positionBoxes[i].add(Box.createHorizontalGlue());

				this.playerNames[i].setAlignmentY(Component.CENTER_ALIGNMENT);
				this.playerBoxHands[i] = Box.createVerticalBox();
				this.playerBoxHands[i].setAlignmentY(Component.CENTER_ALIGNMENT);
				
				if(i == EAST){
					this.positionBoxes[i].add(Box.createHorizontalStrut(5));
					this.positionBoxes[i].add(this.playerNames[i]);
					this.positionBoxes[i].add(Box.createHorizontalGlue());
					this.positionBoxes[i].add(this.playerBoxHands[i]);
				}
				else if(i == WEST) {
					this.positionBoxes[i].add(this.playerBoxHands[i]);
					this.positionBoxes[i].add(Box.createHorizontalGlue());
					this.positionBoxes[i].add(this.playerNames[i]);
					this.positionBoxes[i].add(Box.createHorizontalStrut(5));
				}
			}
			else if(i == NORTH || i == SOUTH) {
				this.positionBoxes[i] = Box.createVerticalBox();
				this.positionBoxes[i].add(Box.createVerticalGlue());
				
				this.playerNames[i].setAlignmentX(Component.CENTER_ALIGNMENT);
				this.playerBoxHands[i] = Box.createHorizontalBox();
				this.playerBoxHands[i].setAlignmentX(Component.CENTER_ALIGNMENT);
				
				if(i == SOUTH){
					this.positionBoxes[i].add(this.playerNames[i]);
					this.positionBoxes[i].add(Box.createVerticalGlue());
					this.positionBoxes[i].add(this.playerBoxHands[i]);
				}
				else if(i == NORTH){
					this.positionBoxes[i].add(this.playerBoxHands[i]);
					this.positionBoxes[i].add(Box.createVerticalGlue());
					this.positionBoxes[i].add(this.playerNames[i]); 
					
				}
			}
		}
		
		// Chat Field and Move/Chat Log
		this.chatField = new JTextField();
		this.sendMessageBtn = new JButton("Send");
		this.moveLog = new JTextArea(5,15);
		this.logsBox = Box.createHorizontalBox();
		
		this.chatField.setPreferredSize(new Dimension(WINDOW_SIZE/3,100));
		this.chatField.setBorder(BorderFactory.createLineBorder(Color.black));
		this.chatField.addKeyListener(this);
		
		this.sendMessageBtn.setMaximumSize(new Dimension(this.sendMessageBtn.getWidth(), 100));
		this.sendMessageBtn.addActionListener(new AbstractSendMessageAction());
		
		this.moveLog.setBorder(BorderFactory.createLineBorder(Color.black));
		JScrollPane sp = new JScrollPane(this.moveLog);
		sp.setMinimumSize(new Dimension(this.moveLog.getWidth(), this.moveLog.getHeight()));
		
		this.logsBox.add(this.chatField);
		this.logsBox.add(this.sendMessageBtn);
		this.logsBox.add(sp);
		this.positionBoxes[SOUTH].add(Box.createHorizontalGlue());
		this.positionBoxes[SOUTH].add(this.logsBox);
		
		resetTop();
	
		// Other player related information
		hands = new Vector<Vector<Card> >();
		
	}
	
	/**
	 * Wrapper to redraw top, the entire frame's underlying JPanel
	 */
	private void resetTop(){
		top.removeAll(); // self explanatory
		top.add(this.centerBox, BorderLayout.CENTER);
		top.add(this.positionBoxes[SOUTH], BorderLayout.SOUTH);
		top.add(this.positionBoxes[EAST], BorderLayout.EAST);
		top.add(this.positionBoxes[NORTH], BorderLayout.NORTH);
		top.add(this.positionBoxes[WEST], BorderLayout.WEST);
		top.repaint(); // informs Swing component needs to be repainted
		top.revalidate(); // tells layout manager to scale
	}
	
	/**
	 * Returns the component that holds this game's
	 * specific GUI
	 */
	protected Component createApplicationComponent(){
		top = new JPanel(new BorderLayout());
		return top;
	}
	
    /**
     *  Updates Gui according to state. This includes
     *  - game message
     *  - discard/stock piles in center
     *  - current player at south position
     *  - all other players
     */
    public void stateChanged(GameState newState){
    	
    	this.state = ((RummyState)newState);
    	
    	// Update center (stock, discard pile, melds, etc.)
    	Vector<Card> stock = this.state.getStock();
    	Vector<Card> discardPile = this.state.getDiscardPile();
    	
    	String gameMessage = null;
    	
    	// Perform move dependent actions
    	if(draw == true){    	
    		gameMessage = "draw.";
    	}
    	else if(move == true){
    		gameMessage = "make a move.";
    	}
    	
    	// Update melds/layouts area
    	Vector<Card> meld = null;
    	for(int i = 0; (meld = this.state.getMeld(i)) != null; i++ ){
    		Box tempBox = Box.createHorizontalBox();
    		for(Card c : meld){
    			ImageIcon cardFace = new ImageIcon( c.getImage());
    			//Image image = cardFace.getImage().getScaledInstance((int)this.stockTop.getPreferredSize().getWidth(), (int)this.stockTop.getPreferredSize().getHeight(), Image.SCALE_SMOOTH);
    			
    			JButton temp = new JButton(cardFace);
				Dimension dim = new Dimension(cardFace.getIconWidth(), cardFace.getIconHeight());
				temp.setName(Integer.toString(c.getID()) ); 
				temp.setPreferredSize(dim);
				temp.addActionListener(abstractMoveActions);
    			tempBox.add(temp);
    		}
    		this.meldGridBag.add(tempBox, this.meldConstraints); 
    	}
    	
    	// Update stock pile
    	if(stock.isEmpty() == true){ // Stock empty
			this.stockTop.setEnabled(false);
		}
		else this.stockTop.setEnabled(true);
    	
    	// Update discard pile
    	if(discardPile.isEmpty() == true) { // Discard empty
			this.discardPileTop.setEnabled(false);
		}
		else {
			ImageIcon cardFace = new ImageIcon( discardPile.lastElement().getImage());
			Image image = cardFace.getImage().getScaledInstance((int)this.stockTop.getPreferredSize().getWidth(), (int)this.stockTop.getPreferredSize().getHeight(), Image.SCALE_SMOOTH);	
			cardFace = new ImageIcon(image);
			this.discardPileTop = new JButton(cardFace);
			Dimension dim = new Dimension(cardFace.getIconWidth(), cardFace.getIconHeight());
			this.discardPileTop.setPreferredSize(dim);
			this.discardPileTop.setMaximumSize(dim);
			this.discardPileTop.setMinimumSize(dim);
			this.discardPileTop.addActionListener(abstractDrawAction);
			
		}
    	
    	// Refresh decks 
    	this.cardBox.removeAll();
		this.cardBox.add(this.stockTop);
		this.cardBox.add(Box.createRigidArea(new Dimension(10, 0)));
		this.cardBox.add(this.discardPileTop);

		// Update game message
		this.gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to " + gameMessage);
		Dimension dim = new Dimension(centerBox.getWidth(),30);
		this.gameMessageText.setPreferredSize(dim);
		this.gameMessageText.setMaximumSize(dim);
		this.gameMessageText.setOpaque(false);
		this.gameMessageText.setHorizontalAlignment(JTextField.CENTER);
		
    	// Swap player at position SOUTH with current player
    	for(int i = 0; i < numPlayers; i++){
    		if(i == this.state.getCurrentPlayer() && ((RummyGame)game).currentIsHuman() == true) {
		
    			int swap;
    			for(swap = 0; swap < numPlayers; swap++) {
    				if(this.playerPosition[swap] == SOUTH) break; // find player at SOUTH
    			}
				int tempPos = this.playerPosition[i]; // swap
				this.playerPosition[i] = SOUTH;
				this.playerPosition[swap] = tempPos;
				
				// Update player name at SOUTH
				this.playerNames[SOUTH].setText("Player " + this.state.getCurrentPlayer());
				
				break;
				
    		}
    	}	
    	
    	// Update players at all positions
    	for(int i = 0; i < numPlayers; i++) {
    		
    		Vector<Card> hand = this.state.getHand(i);
			this.playerNames[this.playerPosition[i]].setText("Player " + i);
			this.playerBoxHands[this.playerPosition[i]].removeAll();
    		
    		if(this.playerPosition[i] == SOUTH){ // face up cards
    	    	for(Card c: hand){
    				ImageIcon cardFace = new ImageIcon(c.getImage());
    		
    				int ratio = cardFace.getIconHeight()/cardFace.getIconWidth();
    				int width = WINDOW_SIZE/(hand.size() + 2);
    				int height = width * ratio;
    				
    				Image image = cardFace.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);	
    				cardFace = new ImageIcon(image);
    				
    				JButton temp = new JButton(cardFace);
    				dim = new Dimension(cardFace.getIconWidth(), cardFace.getIconHeight());
    				temp.setName(Integer.toString(c.getID()) ); // Used to identify card for discard in actionPerformed()
    				temp.setPreferredSize(dim);
    				temp.setMaximumSize(dim);
    				temp.setMinimumSize(dim);
    				temp.addActionListener(abstractMoveActions);
    				this.playerBoxHands[SOUTH].add(temp);
    			}
    			
    		}
    		else { // face down cards
    			
    			for(int j = 0; j < hand.size(); j++){
    				ImageIcon backCard = null;
    				if(this.playerPosition[i] == EAST || this.playerPosition[i] == WEST){ // EAST/WEST players
    					backCard = new ImageIcon("images/default/53.gif");
    				}
    				else if(this.playerPosition[i] == NORTH ){ // NORTH player
    					backCard = new ImageIcon("images/default/00.gif");	
    				}
					JButton temp = new JButton(backCard);
					dim = new Dimension(backCard.getIconWidth(), backCard.getIconHeight());
					temp.setPreferredSize(dim);
					temp.setMaximumSize(dim);
					temp.setMinimumSize(dim);
					this.playerBoxHands[this.playerPosition[i]].add(temp);
    			}
    			
    		}
    	}
    
    	resetTop();
    }
    
    /**
     * Get current player.
     * @return current player as RummyPlayer
     */
    private RummyPlayer getCurrentPlayer(){
    	return ((RummyGame)game).getPlayer(state.getCurrentPlayer());
    	
    }


	@Override
	public void keyPressed(KeyEvent e) {
	
		if(e.getKeyChar() == '\n'){
			if(this.chatField.getText().isEmpty() == false){
				this.moveLog.setText(this.moveLog.getText() + "\n" + this.chatField.getText());
				this.moveLog.setCaretPosition(this.moveLog.getDocument().getLength());
				this.chatField.setText("");
			}
		}
		else if(e.getKeyChar() == 27){ // esc in meld/layout mode
			if(meldLayout == true){
				// TODO
				theMeld.removeAllElements();
				meldLayoutBtn.setText("Meld/Layout");
				doMeldBtn.setEnabled(false);
				gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to make a move.");
				meldLayout = false;
			}
		}
	} 

	/**
	 * All ActionEvents are handled by inner classes that inherit from
	 * AbstractAction. Therefore, RummyGui's actionPerformed() does nothing.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}

