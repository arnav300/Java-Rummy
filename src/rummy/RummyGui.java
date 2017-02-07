package rummy;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Vector;

import game.GameGui;
import game.GameState;
import rummy.Card.Rank;
import rummy.Card.Suit;
import rummy.RummyMoveDraw.DeckType;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
	
	// The GUI must have access to game state in order
	// to update visuals accordingly
	RummyState state = null;
	
	// Boxes corresponding to position in Border Layout
	private final int SOUTH = 0; 
	private final int EAST = 1;
	private final int NORTH = 2;
	private final int WEST = 3;
	Box [] positionBoxes = new Box[4];
	Box centerBox = null;
	Box meldButtonsBox = null;
	
	// Center stuff - melds/layouts, stock/discard, game message text
	Box cardBox = null;
	JPanel meldFlowPanel;
	private final int VERTICAL_GAP = 10;
	private final int HORIZONTAL_GAP = 10;
	JButton stockTop;
	JButton discardPileTop;
	

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
	private boolean meldActivated = false;
	private boolean layoutActivated = false;
	
	private int numPlayers;

	private static RummyGui rummyGui = null;
	
	AbstractDrawAction abstractDrawAction;
	AbstractMoveActions abstractMoveActions;
	
	// Chat related components
	Box logsBox;
	JTextField chatField;
	JTextArea moveLog;
	JButton sendMessageBtn; 
	JTextField gameMessageText;
	
	// Meld specific components
	JButton meldActivatedBtn;
	JButton doMeldBtn;
	Vector<Card> theMeld;
	
	// Layout specific components
	JButton meldSelectedButton; 
	
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
						meldActivatedBtn.setEnabled(true);
					}
				}
			}	
			
		}
	}

	/**
	 * Class to handle meld/layout/discard related action events.
	 * @author Robert
	 *
	 */
	@SuppressWarnings("serial")
	private class AbstractMoveActions extends AbstractAction {
		
	
		/**
		 * Helper method to get the index of the card corresponding
		 * to the selected JButton 
		 * 
		 * @param the card id (i.e. a number from 1-52; IDs are unique)
		 * @return -1 if source doesn't exist in the hand, else an 'int'
		 * 			that is the index of the source in the hand
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
		
		/**
		 * Helper method to get the index of the meld in ((RummyGame)game).melds.
		 * It takes advantage of the fact that the representation of the meld as
		 * a JButton object is also represented as a Vector<Card> object in 
		 * ((RummyGame)game).melds.
		 * 
		 * The method iterates through all objects of the meldFlowPanel. If 
		 * the object is a JButton, 'index' is incremented. If the object is 
		 * the button that is selected, the iterating quits and 'index' will
		 * be set to index of the meld in ((RummyGame)game).melds. 
		 * 
		 * @param the meld as a JButton
		 * @return the index of the meld as an 'int'
		 */
		private int getSelectedMeldIndex(Object source){
			int index = -1;
			
			for(int i = 0; i < meldFlowPanel.getComponentCount(); i++){
				Object component = meldFlowPanel.getComponent(i);
				if( component instanceof JButton ) {
					index++;
					if ( component == source) {	
						break;
					}
				}
			}
			
			return index;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {

			Object source = e.getSource();
			RummyPlayer currentPlayer = getCurrentPlayer();
			
			if(currentPlayer instanceof RummyHumanPlayer){
				
				if(move == true) {
				
					// Meld
					if(meldActivated == true){
						
						// Activate meld mode
						if(source == meldActivatedBtn){
							theMeld.removeAllElements();
							meldActivatedBtn.setText("Meld/Layout");
							doMeldBtn.setEnabled(false);
							gameMessageText.setText("Player " + getCurrentPlayer().getId() + " didn't select cards for a meld!");
							meldActivated = false;
						}
						
						// Do meld
						else if(source == doMeldBtn){ 
							
							((RummyGame)game).applyAction(new RummyMoveMeld(currentPlayer, (Vector<Card>)theMeld.clone()) );
							
							theMeld.removeAllElements();
							meldActivatedBtn.setText("Meld/Layout");
							doMeldBtn.setEnabled(false);
							gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to make a move.");
							meldActivated = false;
						}
						
						// Append reference of selected card to meld
						else if( ((JButton)source).getParent().getClass() == Box.class && 
								(Box) ((JButton)source).getParent() == playerBoxHands[SOUTH]){
							
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
					}
					
					
					else if(layoutActivated == true) {
						
						
						if ( ((JButton)source).getParent() instanceof JPanel && 
						      (JPanel) ((JButton)source).getParent() == meldFlowPanel)  { // Meld selected
								
								gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to make a move.");
								layoutActivated = false;
						}
						
						// Activate layout mode (by selecting a meld currently on the table)
						// And save reference of selected meld
						else if ( ((JButton)source).getParent() instanceof Box &&
							 ((Box) ((JButton)source).getParent() ) == playerBoxHands[SOUTH]) { // Meld selected
							
							int cardIndex = getSelectedCardIndex( Integer.valueOf(((JButton)source).getName()) );
							int meldIndex = getSelectedMeldIndex( meldSelectedButton );
							
							((RummyGame)game).applyAction(new RummyMoveLayoff(currentPlayer, meldSelectedButton, meldIndex, cardIndex));
							
							gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to make a move.");
							layoutActivated = false;
						}

					}
					else if (meldActivated == false && layoutActivated == false){
						
						// Perform discard move
						if( ((JButton)source).getParent() instanceof Box &&
								(Box) ((JButton)source).getParent() == playerBoxHands[SOUTH]) {
							
							int index = getSelectedCardIndex( Integer.valueOf(((JButton)source).getName()) );

							((RummyGame)game).applyAction(new RummyMoveDiscard(currentPlayer, index));
							meldActivatedBtn.setEnabled(false);
							move = false;
							draw = true;
						}
						
						// Activate layout mode (by selecting a meld currently on the table)
						// and save reference of selected meld
						else if ( ((JButton)source).getParent() instanceof JPanel && 
								  (JPanel) ((JButton)source).getParent() == meldFlowPanel) { // Enter layout mode
							
							meldSelectedButton = (JButton)source;
							gameMessageText.setText(gameMessageText.getText() + "\nChoose a card to layout: ");
							layoutActivated = true;
						}
						
						// Activate meld mode
						else if (source == meldActivatedBtn ){
							meldActivatedBtn.setText("Exit " + meldActivatedBtn.getText());
							doMeldBtn.setEnabled(true);
							gameMessageText.setText(gameMessageText.getText() + "\nChoose cards to meld: ");
							meldActivated = true;
						}

					}
				}
				
			}
		}
	}

	/**
	 * Class to handle chat field/move log related action events. 
	 * @author Robert
	 *
	 */
	@SuppressWarnings("serial")
	private class AbstractSendMessageAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveLog.setText(moveLog.getText() + "\n" + "Player " + Integer.toString(state.getCurrentPlayer()) + ": " + chatField.getText());
			moveLog.setCaretPosition(moveLog.getDocument().getLength());
			chatField.setText("");
		}
	}
	
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
		this.theMeld = new Vector<Card>();
		
		// Center info
		abstractMoveActions = new AbstractMoveActions();
		this.centerBox = Box.createVerticalBox();
		this.meldButtonsBox = Box.createHorizontalBox();
		this.doMeldBtn = new JButton("Play Meld");
		this.doMeldBtn.setEnabled(false);
		this.doMeldBtn.addActionListener(abstractMoveActions);
		this.meldFlowPanel = new JPanel();
		this.meldFlowPanel.setLayout(new FlowLayout());
		this.meldFlowPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));
		((FlowLayout)this.meldFlowPanel.getLayout()).setHgap(HORIZONTAL_GAP);
		((FlowLayout)this.meldFlowPanel.getLayout()).setVgap(VERTICAL_GAP);
		this.cardBox = Box.createHorizontalBox();
		this.gameMessageText = new JTextField();
		this.gameMessageText.setBorder(null);
		this.meldActivatedBtn = new JButton("Meld/Layout");
		this.meldActivatedBtn.setEnabled(false);
		this.meldActivatedBtn.addActionListener(abstractMoveActions);
		this.meldActivatedBtn.addKeyListener(this);
		
		this.centerBox.add(Box.createVerticalStrut(10));
		this.centerBox.add(this.meldFlowPanel);
		this.centerBox.add(Box.createVerticalStrut(10));
		this.centerBox.add(this.cardBox);
		this.centerBox.add(Box.createVerticalStrut(5));
		this.centerBox.add(this.gameMessageText);
		this.centerBox.add(Box.createVerticalStrut(5));
		this.meldButtonsBox.setAlignmentX(Box.CENTER_ALIGNMENT);
		this.meldButtonsBox.add(this.meldActivatedBtn);
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
		this.moveLog.setLineWrap(true);
		this.moveLog.setWrapStyleWord(true);
		
		JScrollPane moveLogScrollPane = new JScrollPane(this.moveLog);
		moveLogScrollPane.setMinimumSize(new Dimension(this.moveLog.getWidth(), this.moveLog.getHeight()));
		
		
		this.logsBox.add(this.chatField);
		this.logsBox.add(this.sendMessageBtn);
		this.logsBox.add(moveLogScrollPane);
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
    	
    	// Update melds/layouts area - melds will be displayed as pairs of characters that
    	// represent suit/rank (e.g. C4 is 4 of clubs, H13 is king of hearts etc.)
    	Vector<Card> meld = null;
    	
    	this.meldFlowPanel.removeAll();
    	int rowWidth = 0;
    	int rowHeight = 0;
    	for(int i = 0; (meld = this.state.getMeld(i)) != null; i++ ){
    	
    		JButton tempButton = new JButton(convertVectorToText(meld));
    		tempButton.addActionListener(abstractMoveActions);
    		Dimension defaultDim = tempButton.getPreferredSize();
    		
    		rowWidth += defaultDim.getWidth() + HORIZONTAL_GAP;
    		rowHeight = (int) Math.max(rowHeight, defaultDim.getHeight() + VERTICAL_GAP);
    		if(rowWidth > meldFlowPanel.getWidth()){
    			rowHeight += defaultDim.getHeight() + VERTICAL_GAP; // 10 is size of vertical strut
    			rowWidth = 0;
    			this.meldFlowPanel.add(Box.createVerticalStrut(VERTICAL_GAP));
    		}
    		
    		this.meldFlowPanel.add(tempButton); 
    		this.meldFlowPanel.add(Box.createHorizontalStrut(HORIZONTAL_GAP));
    	}
    	
    	this.meldFlowPanel.setPreferredSize(new Dimension(meldFlowPanel.getWidth(), rowHeight));
    	
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
    			
    			hand.sort(new Card(0));
    			
    	    	for(Card c: hand){
    				ImageIcon cardFace = new ImageIcon(c.getImage());
    		
    				JButton temp = new JButton(cardFace);
    				Dimension dim = new Dimension(cardFace.getIconWidth(), cardFace.getIconHeight());
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
					Dimension dim = new Dimension(backCard.getIconWidth(), backCard.getIconHeight());
					temp.setPreferredSize(dim);
					temp.setMaximumSize(dim);
					temp.setMinimumSize(dim);
					this.playerBoxHands[this.playerPosition[i]].add(temp);
    			}
    			
    		}
    	}
    	
    	// Check for winner
    	for(int i = 0; i < numPlayers; i++ ){
	    	if( this.state.getHand( i ).size() == 0){
	    		this.state.setHasWon(true);
	    		
		    	String title = "Player " + String.valueOf(state.getCurrentPlayer()) + " is the winner!";
		    	String prompt = "We have a winner!";
		    	JOptionPane.showMessageDialog(null,
									          title,
									          prompt,
									          JOptionPane.PLAIN_MESSAGE);
				
		    	this.dispose();
		    	System.exit(0);
	    	}
    	}
    	
		// Update game message
		this.gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to " + gameMessage);
		Dimension dim = new Dimension(centerBox.getWidth(),30);
		this.gameMessageText.setPreferredSize(dim);
		this.gameMessageText.setMaximumSize(dim);
		this.gameMessageText.setOpaque(false);
		this.gameMessageText.setHorizontalAlignment(JTextField.CENTER);
	
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
		else if(e.getKeyChar() == 27){ // esc in meld mode
			if(meldActivated == true){
				// TODO
				theMeld.removeAllElements();
				meldActivatedBtn.setText("Meld/Layout");
				doMeldBtn.setEnabled(false);
				gameMessageText.setText("Player " + getCurrentPlayer().getId() + "'s turn to make a move.");
				meldActivated = false;
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
	
	/**
     * Converts text, which represents cards, to a Vector<Card>.
     * @parameter text, as string, that represents cards (e.g. "C1 S12 D3 H5")
     * @return Vector<Card>
     */
	public Vector<Card> convertTextToVector(String text){
		
		String [] cardSequence = text.split(" ");
		Vector<Card> meld = new Vector<Card>();
		int cardCount = cardSequence.length;
		
		// Convert sequences, such as 'C13 D3 S11', to array of Card objects
		for(int i = 0; i < cardCount; i++){

			// Get suit
			char suit = cardSequence[i].charAt(0);

			// Get rank - while loop to grab all digits of 2 digit ranks
			int numChars = cardSequence[i].length();
			
			int rank = 0;
			if(numChars == 2) {
				rank = cardSequence[i].charAt(1) - 48;
			}
			else if(numChars == 3) {
				rank += (cardSequence[i].charAt(1) - 48) * 10;
				rank += (cardSequence[i].charAt(2) - 48);
			}
			
			Card newCard = new Card(Suit.createSuit(suit),Rank.createRank(rank));
			meld.add(newCard);
		}
		
		return meld;
	}

	/**
     * Converts a Vector<Card> to text, which contains text representing a cards.
     * @parameter Vector<Card>
     * @return text, as string, that represents cards (e.g. "C1 S12 D3 H5")
     */
	public String convertVectorToText(Vector<Card> cardsAsVector){

		StringBuffer textBuffer = new StringBuffer();
		for(Card card : cardsAsVector){
			char suit = card.getSuit().toChar();
			textBuffer.append(suit + Integer.toString(card.getRank()) + " ");
		}
		
		return textBuffer.toString();
	}
	
}

