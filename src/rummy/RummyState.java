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

	private int currentPlayer;
	private int numPlayers;
	
	private Vector<Card> stock;
    private Vector<String> chatHistory;
    private Vector<Move> moveLog;
	
	private int[] score;
    private boolean hasWon;
    
    /**
     * Constructor for creating a new state.
     * 
     * @param Deck
     *            the deck in the middle.
     * @param Players
     *            Vector containing all the players
     * @param ChatHistory
     *            Vector containing all the chats
     * @param curPlayer
     *            integer to keep track of whose turn it is.
     */
    public RummyState(Stack<Card> stock, Vector<String> newChatHistory,
            int newCurrPlayer, Vector<Move> moves, int[] score) {
        this.stock = stock;
        chatHistory = newChatHistory;
        currentPlayer = newCurrPlayer;
        this.score = score;
        moveLog = moves;
        this.numPlayers = score.length;
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
        return currentPlayer;
    }
    
    /**
     * Gets the stuff for the chat.
     */
    public Vector<String> getChat() {
        return chatHistory;
    }

    /**
     * Gets the stuff for the movelog
     */
    public Vector<Move> getLog() {
        return moveLog;
    }

    
    /**
     * score of specific player
     * 
     * @param ID
     *            , the ID of the player's score we are requesting
     * @return score, the score of the requested player
     */
    public int getScore(int ID) {
        return score[ID];
    }

    /**
     * this one will send off the entire array of the scores
     * 
     * @return score, the score array
     */
    public int[] getScores() {
        return score;
    }


}
