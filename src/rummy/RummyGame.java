package rummy;

import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import game.Game;
import game.GameMoveAction;
import game.GamePlayer;
import game.GameState;

import rummy.RummyMoveDraw.DeckType;

/**
 * Implementation of the rummy game. This class contains game related methods.
 * 
 * @author Robert
 *
 */
public final class RummyGame extends Game {

	// The GUI object
	private RummyGui rummyGui;
	
	// Player related information
	private int numPlayers;
	private int numCardsPerHand; // determined by numPlayers
	private Vector<Vector<Card> > hands; // this is typesafe ONLY if its never returned by a method
	
	// Stock and discardPile piles
	private Stack<Card> stock;
	private Stack<Card> discardPile;
	
	// State related parameters
	private int currentPlayer;
	private Vector<Vector<Card> > melds;
	private Vector<String> chatHistory;
	private Vector<Move> moveHistory;
	private int [] score;
	private boolean hasWon;
	
	// print "debug" messages about the entries in the queue
    private static boolean MONITORQUEUE = false;
	
	private void initGui(){
		rummyGui = (RummyGui)RummyGui.createRummyGui();
		rummyGui.setGame(this);
    	rummyGui.stateChanged(new RummyState(this.stock, this.discardPile, this.hands, this.melds, this.chatHistory, this.player[0].getId(), this.moveHistory, this.score)); // getState() args not used
	}
	
	@SuppressWarnings("unchecked")
	protected void initializeGame(){
    	
    	// Create Deck
    	Stack<Card> deck = initializeDeck();
    	
    	// Deal Cards
    	this.hands = new Vector<Vector<Card> >();
    	for(int i = 0; i < numPlayers; i++){
    		Vector<Card> tempHand = new Vector<Card>();
    		for(int j = 0; j < numCardsPerHand; j++){ 
    			tempHand.add(deck.pop());
    		}
    		
    		this.hands.addElement(new Vector<Card>(tempHand));
    		((RummyPlayer)this.player[i]).setHand(tempHand);
    	}
    	
    	// Put remaining cards in stock
    	this.stock = new Stack<Card>();
    	while(deck.isEmpty() != true){
    		this.stock.push(deck.pop());
    		
    	}
    
    	// Flip first card of stock for discardPile pile
    	this.discardPile = new Stack<Card>();
    	this.discardPile.push(stock.pop());
    	
    	// State related information
    	this.melds = new Vector<Vector<Card> >();
    	this.chatHistory = null;
    	this.moveHistory = null;
    	this.score = new int[numPlayers];
    	this.hasWon = false;
    	
    	// Create Gui
    	initGui();
    	
    	notifyAllStateChanged();
    	
	}
    
    
    /**
     * Initialize deck with ids 1 - 52 (Id is enough to determine
     * rank and suit, see Card class for specifics)
     * 
     * @return deck as Stack<Card>
     */
    private Stack<Card> initializeDeck(){
  
    	Stack<Card> deck = new Stack<Card>();
    	
    	Random rg = new Random();
    	boolean [] check = new boolean[52];
    
    	for(int i= 1; i < 52; i++){

    		// Generate random value b/w 1 and 52
    		int value = rg.nextInt(52) + 1;
    		
    		// Probe for next available
    		while(check[value - 1] == true) {
    			value = (value + 1) % 53 + (value + 1)/53;

    		}
    		check[value - 1] = true;
    		
    		// Push new Card
    		deck.push(new Card(value));
    		
    	}
    	
    	return deck;
    }
    
	public RummyGame(int numPlayers){
		
		this.numPlayers = numPlayers;
		
        // Based on number of players counted, 
        // determine number of cards player uses
    	if(numPlayers == 2) this.numCardsPerHand = 10;
		else if(numPlayers <= 4) this.numCardsPerHand = 7;
		else if(numPlayers <= 6) this.numCardsPerHand = 6;
    	
        this.gameStarted = false;
        this.numPlayers = 0;
        this.player = null;
        
	}

	@Override
	public String setPlayers(GamePlayer[] players) {
		
		// check that array is not null
        if (players == null) return "no players selected";

        // count the players; keep track of whether any are null
        boolean haveNullPlayers = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) haveNullPlayers = true;
            else numPlayers++;
        }

        // if have (disallowed) null players, or too many/few, return
        if (haveNullPlayers && !nullPlayersAllowed()) {
            return "all player-slots must be filled";
        }
        if (numPlayers < minPlayersAllowed()) {
            return "not enough players for the game";
        }
        if (numPlayers > maxPlayersAllowed()) {
            return "too many players for the game";
        }

        // make a copy of the array to store as part of our state
        player = (RummyPlayer[])players.clone(); // shallow copy ... copies only as reference (a la c++ pointer addresses)
		
        // return "success"
        return null;
		
	}
	
	public boolean gameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean nullPlayersAllowed() {
		return true;
	}

	@Override
	public int minPlayersAllowed() {
		return 2;
	}

	@Override
	public int maxPlayersAllowed() {
		return 4;
	}

	/**
	 * Accessor method to query number of players in a game. 
	 * 
	 * @return numPlayers
	 */
	public int getNumberPlayers(){
		return numPlayers;
	}

	   /**
     * Tells the numeric position of a player in the game
     *
     * @param gp  the player 
     * @return the numeric position of the player in the game, -1 if the
     * player is not part of the game
     *
     */
    protected int indexOf(GamePlayer p) {
        // find the player in the array, returning the index when found
        for (int i = 0; i < player.length; i++) {
            if (player[i] == p) return i;
        }
        
        // if we get here, the player wasn't there, so return -1
        return -1;
    }
	
    
    /**
     * Performs game-specific action that needs to be done after all
     * players are ready (e.g., initializing timers).
     * 
     */
    protected void performAfterAllAreReady() {
    	//TODO
    }
    
	@Override
	protected boolean canMove(GamePlayer gp) {
		if(currentPlayer == gp.getId()){
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean canQuit(GamePlayer gp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected GameState getGameState(GamePlayer p, int stateType) {
		RummyState state = new RummyState(stock, this.discardPile, this.hands, this.melds, this.chatHistory, p.getId(), moveHistory, score);
		state.setHasWon(hasWon);
		return state;
	}
	
	  /**
     * Requests that a move be made on behalf of a player.  Typically, the
     * player that wants to make the move creates a MoveAction object that
     * encodes the move he wants to make, and then calls this method, passing
     * itself as the player object.  As long as the player does not allow
     * any other player to know who it is (which is typical), no other player
     * can make a move on this player's behalf because it does not have a
     * reference to that player, and therefore cannot pass it as a parameter.
     * This method should check that the given player is allowed to make
     * the specified move before actually performing the move.
     *
     * @param thePlayer The player wishing to make the move.
     * @param move The move object that encodes the requested move
     * @return true if the move was successfully made; false if it's an
     *  illegal move.
     */
    protected boolean makeMove(GamePlayer thePlayer, GameMoveAction move){

    	currentPlayer = thePlayer.getId();
    	RummyMoveAction rummyMove = (RummyMoveAction)move;
    	
    	if(rummyMove instanceof RummyMoveDraw){ // draw 
    		
    		DeckType thisDeckType = ((RummyMoveDraw) rummyMove).moveDeckType;
    		Card cardDrawn = null;
    		
    		if(thisDeckType == DeckType.STOCK){
    			if(this.stock.isEmpty() != true){
    				cardDrawn = stock.pop();
    			}
    			else return false;
    		}
    		else if(thisDeckType == DeckType.DISCARD){
    			if(this.discardPile.isEmpty() != true){
    				cardDrawn = this.discardPile.pop();
    			}
    			else return false;
    		}
    		
    		hands.elementAt(currentPlayer).add(cardDrawn);
    		
    		rummyGui.stateChanged(new RummyState(this.stock, this.discardPile, this.hands, this.melds, this.chatHistory, this.currentPlayer, this.moveHistory, this.score));
    		return true;
    	}
    	else if(rummyMove instanceof RummyMoveMeld){ // optional move

			Vector<Card> theMeld = null;
    		if(this.validateMeld(this.currentPlayer, (RummyMoveMeld)rummyMove, theMeld)){ // check 
        		
        		int [] indices = ((RummyMoveMeld) rummyMove).getIndices();
        		
    			for(int index : indices){
    				this.hands.elementAt(currentPlayer).remove(index);
    			}
    			
    			this.melds.addElement(theMeld); // if meld valid, add to game's melds
    		}
    		
    		rummyGui.stateChanged(new RummyState(this.stock, this.discardPile, this.hands, this.melds, this.chatHistory, this.currentPlayer, this.moveHistory, this.score));
    		return true;
    	}
    	else if(rummyMove instanceof RummyMoveLayoff){ // optional move
    		rummyGui.stateChanged(new RummyState(this.stock, this.discardPile, this.hands, this.melds, this.chatHistory, this.currentPlayer, this.moveHistory, this.score));
    		return true;
    	}
    	else if(rummyMove instanceof RummyMoveDiscard){  // discardPile and end turn
    		Card discardCard = this.hands.elementAt(currentPlayer).remove(((RummyMoveDiscard)rummyMove).getDiscardIndex());
    		this.discardPile.push(discardCard);
    		
    		// Update current player
    		currentPlayer = (currentPlayer + 1) % 4;
    		rummyGui.stateChanged(new RummyState(this.stock, this.discardPile, this.hands, this.melds, this.chatHistory, this.currentPlayer, this.moveHistory, this.score)); // getState() args not used
    		return true;
    	}
	
    	return false;
    	
    }
    
    /** 
     * Method to get a player. Intentionally left as package private (this is for GUI)
     * 
     * @return RummyPlayer
     */
    RummyPlayer getPlayer(int id){
    	return (RummyPlayer) player[id];
    }
    
    /**
     * Method used by GUI to check which human player hand to display.
     * 
     * @return boolean indicating if current player is a human
     */
    public boolean currentIsHuman(){
    	return (this.player[currentPlayer] instanceof RummyHumanPlayer);
    }
    
    /**
     * 
     * Method to validate the meld before it can be played.
     * 
     * 
     * @return true if meld valid or false if meld is invalid
     */
    public boolean validateMeld(int currentPlayer, RummyMoveMeld rummyMoveMeld, Vector<Card> theMeld){
		
		boolean valid = true;
		int [] indices = rummyMoveMeld.getIndices();
		
		if(indices.length >= 3){

			// Check sequence
			theMeld = new Vector<Card>(); 
			
			for(int index : indices){
				theMeld.addElement(this.hands.elementAt(currentPlayer).get(index));
			}
			theMeld.sort(new Card(0));
			
			Card prev = theMeld.elementAt(0);
			Card curr;
			for(int i = 1; i < theMeld.size(); i++){
				curr = theMeld.elementAt(i);
				if(Math.abs(curr.getRank() - prev.getRank()) > 1){
					valid = false;
					break;
				}
					
				prev = curr;
			}
			
			// Check group
			int meldRank = theMeld.elementAt(0).getRank();
			for(int i = 1; i < theMeld.size(); i++){
				if(theMeld.elementAt(i).getRank() != meldRank){
					valid = false;
					break;
				}
			}
		}
		else {
			valid = false;
		}
		
		return valid;
	}
    
}
