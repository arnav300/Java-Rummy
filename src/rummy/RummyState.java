package rummy;

import java.util.Stack;
import java.util.Vector;

import game.GameState;


/**
 * 
 * Defines the state of the game. This class is constantly 
 * updated and passed between players.
 * 
 * @author Robert
 *
 */
public final class RummyState extends GameState {

	private int numPlayers;
	private int currentPlayer;
	
	private Vector<Card> stock;
	private Vector<Card> discard;
	private Vector<Vector<Card> > melds;
	private Vector<Vector<Card> > hands;
    private Vector<String> chatHistory;
    private Vector<Move> moveLog;
	
	private int[] score;
    private boolean hasWon;
    
    
    /**
     * Constructor for creating a new state.
     * 
     * @param stock
     *            the deck in the center.
     * @param discard
     * 			  the deck of discarded cards in the center.
     * @param hands
     *            Vector containing all the hands of all players.
     * @param melds
     * 			  Vector containing Vector's, which represent melds in play.
     * @param newChatHistory
     *            Vector containing all the chats
     * @param currentPlayer
     *            integer to keep track of whose turn it is.
     * @param moves
     * 			  Vector of moves.
     * @param score
     * 			  array containing scores of each player.
     * 
     */
    public RummyState(Stack<Card> stock, Stack<Card> discard, Vector<Vector<Card> > hands, Vector<Vector<Card> > melds, Vector<String> newChatHistory, 
           int currentPlayer, Vector<Move> moves, int[] score) {
        
    	this.stock = stock;
    	this.discard = discard;
        this.hands = hands;
        this.melds = melds;
        this.chatHistory = newChatHistory;
        this.currentPlayer = currentPlayer;
        this.score = score;
        this.moveLog = moves;
        this.numPlayers = score.length;
    }
	
    /**
     * Return current stock for Rummy game
     * 
     * @return current stock
     */
    public Vector<Card> getStock(){
    	return stock;
    }
    
    /**
     * Return current discard pile for Rummy game
     * 
     * @return current discard pile
     */
    public Vector<Card> getDiscardPile(){
    	return discard;
    }
    
    /**
     * Return melds for Rummy game. Unlike the number of hands,
     * the number of melds is variable. Therefore, this method will return 
     * null in the case the index is outside the range of melds.
     * 
     * @return the meld at the index, else null
     * 
     */
    public Vector<Card> getMeld(int index){
    	if(index < melds.size() && index >= 0){
    		return melds.elementAt(index);
    	}
    	else{
    		return null;
    	}
    }
    
    /**
     * Goes through the active players and finds the
     * player with the most points
     * 
     * @return the id of the winning player
     */
    public int getWinner() {
        int highScore = -1;
        int id = -1;
        for (int i = 0; i < numPlayers-1; i++) {
            if (score[i] > highScore) {
                highScore = score[i];
                id = i;
            }
        }
        return id;

    }
    
    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public boolean isHasWon() {
        return hasWon;
    }
    

    /**
     * Returns whose turn it is.
     */
    public int getCurrentPlayer() {
        return this.currentPlayer;
    }
    
    /**
     * Gets the stuff for the chat.
     */
    public Vector<String> getChat() {
        return chatHistory;
    }

    /**
     * Gets the stuff for the movelog.
     */
    public Vector<Move> getMove() {
        return moveLog;
    }

    /**
     * Get player hand.
     * 
     * @param player id
     * @return player hand
     */
    public Vector<Card> getHand(int ID){
    	return hands.elementAt(ID);
    }
    
    /**
     * Return the score of specific player
     * 
     * @param ID
     *            , the ID of the player's score we are requesting
     * @return score, the score of the requested player
     */
    public int getScore(int ID) {
        return score[ID];
    }

    /**
     * Return the entire array of the scores
     * 
     * @return score, the score array
     */
    public int[] getScores() {
        return score.clone();
    }
}
