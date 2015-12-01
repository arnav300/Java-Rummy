package rummy;

import game.Game;
import game.GameDriver;
import game.ProxyPlayer;

/**
 * Contains JavaRummy's entry point. Responsible for creating a game and 
 * setting up game players. Implements GameDriver interface.
 * 
 * @author Robert
 *
 */
public final class RummyDriver implements GameDriver {
	private String[] lpChoices = new String[] { "human player",
            "EASY computer player", "MED computer player",
            "HARD computer player" };

	/**
	 * Return array of player types.
	 * 
	 * @return string array of player types
	 */
	public String[] localPlayerChoices(){
		return lpChoices;
	}
	
	/**
	 * Instantiate new player object.
	 * 
	 * @param name 
	 * 			string that specifies type of player
	 * @return RummyPlayer
	 */
	public RummyPlayer createLocalPlayer(String name){
	
		if (name.equals(lpChoices[0])) { // user selected "Human Player"
			return new RummyHumanPlayer();
		
	    } else if (name.equals(lpChoices[1])) { // user selected
	                                            // "EASY Computer Player"
	        return new RummyComputerPlayer(1);
	    } else if (name.equals(lpChoices[2])) { // user selected
	                                            // "MED Computer Player"
	        return new RummyComputerPlayer(2);
	    } else if (name.equals(lpChoices[3])) { // user selected
	                                            // "HARD Computer Player"
	    	return new RummyComputerPlayer(3);
	    } else {								// bad selection
	        return null;
	    }
	}
		
	/**
     * Makes a new local Rummy game.
     * 
     * @param numPlayers
     *            how many players are going to play
	 * @return RummyGame
     */
	public RummyGame createLocalGame(int numPlayers){
		return new RummyGame(numPlayers);
	}
	
	public ProxyPlayer createRemotePlayer(){
		return null;
	}
		
	public Game createRemoteGame(String hostName){
		return null;
	}

	public static void main(String[] args) {

		RummyDriverEngine de = new RummyDriverEngine();
		de.StartGame();

	}
	
}
