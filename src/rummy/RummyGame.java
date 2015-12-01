package rummy;

import java.util.Hashtable;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import game.Game;
import game.GameAction;
import game.GameMoveAction;
import game.GamePlayer;
import game.GameState;

/**
 * Implementation of the rummy game. This class contains game related methods.
 * 
 * @author Robert
 *
 */
public final class RummyGame extends Game {

	// the gui
	private RummyGui rummyGui;
	
	// Players and player related information
	private int numPlayers;
	private int sizeOfHand; // determined by numPlayers
	private RummyPlayer[] player;
	private Queue<GameAction> actionQueue;
	
	// Stock and Discard piles
	private Stack<Card> stock;
	private Stack<Card> discard;
	
	// State related parameters
	private boolean gameStarted;
	private int currentPlayer;
	private Vector<String> chatHistory;
	private Vector<Move> moveLog;
	private int [] score;
	private boolean hasWon;
	
	// print "debug" messages about the entries in the queue
    private static boolean MONITORQUEUE = false;
	
	private void initGui(){
		rummyGui = (RummyGui)RummyGui.createRummyGui();
		rummyGui.setGui(this);
	}
	
	protected void initializeGame(){
		
    	// Create Gui
    	initGui();
    	
    	// Create Deck
    	Stack<Card> deck = initializeDeck();
    	
    	// Deal Cards
    	for(int i = 0; i < numPlayers; i++){
    		Vector<Card> tempHand = new Vector<Card>();
    		for(int j = 0; j < sizeOfHand; j++){ 
    			tempHand.add(deck.pop());
    		}
    		
    		player[i].setHand(tempHand);
    	}
    	
    	// Put remaining cards in stock
    	stock = new Stack<Card>();
    	while(deck.isEmpty() != true){
    		stock.push(deck.pop());
    	}
    
    	// Flip first card of stock for discard pile
    	discard = new Stack<Card>();
    	discard.push(stock.pop());
    	
    	
    
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
    	Hashtable<Integer, Boolean> ht = new Hashtable<Integer, Boolean>();
    	
    	for(int i= 1; i < 52; i++){

    		// Generate random value b/w 1 and 52
    		int value = rg.nextInt(52) + 1;
    		
    		// Probe if next int
    		while(ht.contains(value) == true) {
    			value = (value + 1) % 53 + (value + 1)/53;

    		}
    		ht.put(new Integer(value), new Boolean(true));
    		
    		// Push new Card
    		deck.push(new Card(value));
    		
    	}
    	
    	return deck;
    }
    
	public RummyGame(int numPlayers){
		
		this.numPlayers = numPlayers;
		
        // Based on number of players counted, 
        // determine number of cards player uses
    	if(numPlayers == 2) this.sizeOfHand = 10;
		else if(numPlayers <= 4) this.sizeOfHand = 7;
		else if(numPlayers <= 6) this.sizeOfHand = 6;
    	
        this.gameStarted = false;
        this.numPlayers = 0;
        this.player = null;
        
	}

	@Override
	public void applyAction(GameAction action) {
		actionQueue.add(action);
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
		return 6;
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
	 * Accessor method touoe query number per hand.
	 * 
	 * @return sizeOfHand
	 */
	public int getSizeOfHand(){
		return sizeOfHand;
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
		// TODO Auto-generated method stub
		return null;
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
     * @return true if the move was successfully made; false it it's an
     *  illegal move.
     */
    protected boolean makeMove(GamePlayer thePlayer, GameMoveAction move){
    	
    	
    	
    	
    	return false;
    }
}

/**
 * An abstract class specifies an action to perform.
 */
abstract class PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public abstract void invokeAction(GamePlayer player);
} 
       
/**
 * A class that invokes the 'stateChanged' action.
 */
class StateChangedInvoker extends PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public void invokeAction(GamePlayer player) {
        player.stateChanged();
    }
}
 
/**
 * A class that invokes the 'timeToQuit' action.
 */
class TimeToQuitInvoker extends PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public void invokeAction(GamePlayer player) {
        player.timeToQuit();
    }
}
  
/**
 * A class that invokes the 'gameIsOver' action.
 */
class GameIsOverInvoker extends PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public void invokeAction(GamePlayer player) {
        player.gameIsOver();
    }
}
     
/**
 * A class that helps invoke the 'stateChanged' method on
 * a player, using a separate thread.
 */
class ParallelNotifier implements Runnable {

    // the player to invoke the action on
    private GamePlayer myPlayer;
    
    // the action to perform
    private PlayerActionInvoker actionObject;
    
    /**
     * Constructor for ParallelNotifier
     * 
     * @param player  the player to invoke the action on
     * @param pai  the object representing the action to invoke
     */
    public ParallelNotifier(GamePlayer player, PlayerActionInvoker pai) {
    
        // invoke superclass constructor
        super();
        
        // initialize instance variables
        myPlayer = player;
        actionObject = pai;
    }

    /**
     * The code that runs in the separate thread.
     */
    public void run() {
        // invoke the appropriate method on the player
        actionObject.invokeAction(myPlayer);
    }
}   
