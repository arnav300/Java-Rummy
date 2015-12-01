package rummy;

import java.util.*;

import javax.swing.JOptionPane;

import game.Game;
import game.GamePlayer;

/**
 * Interacts with a user to create a game and allow the user to select 
 * what kind of players will be playing.
 *  
 * @author Robert
 *
 */
public final class RummyDriverEngine {

	private RummyDriver driver;
	
	private String [] selections = {"Local or Remote", "Number Players"};
	
	public void StartGame(){
		
		// Prompt user to choose 'local' or 'remote' game
		int response = getSelection(selections[0], 
									"Will this game be played locally or remotely?", 
									new String[]{"Local", "Remote"});

		driver = new RummyDriver();
		GamePlayer [] p = null;
		Game game = null;
		
        if (response == 0) { // local game
        	
        	// create a "dummy game" so that the createLocalPlayers
            // method knows the minimum and maximum number of players
            // allowed, etc.
            RummyGame dummyGame = driver.createLocalGame(0);

            // interact with the user to create the players
            p = createLocalPlayers(dummyGame);

            game = driver.createLocalGame(p.length);
          
        }
        else if (response == 1) { // remote game
/*
            // create a single player, which is the one playing on this
            // network node
            p = new GamePlayer[]{createMyRemotePlayer()};

            // if no player was created, notify user and quit
            if (p[0] == null) {
                errorMessage("No player selected");
                System.exit(1);
            }

            // ask user for the name of the remote machine
            String machName =
                promptAndReadString("Remote machine name:");

            // create a "proxy" game that connects to a game on the
            // remote machine
            ProxyGame pg = driver.createRemoteGame(machName);

            // the game-creation operation failed, give error message
            // and exit
            if (pg == null || !pg.isComplete()) {
                errorMessage("Could not make connection to server");
                System.exit(1);
            }
            else {
                game = pg;
            }
            */
        }
        else { // response == 2, aka cancel
          return;
            
        }
   
        //  tell the game who its players are.  If the operation fails
        // give user an error message and exit.
        String msg = game.setPlayers(p);
        if (msg != null) {
            errorMessage(msg);
            System.exit(1);
        }

        // Tell each player who its game is
        for (int i = 0; i < p.length; i++) {
            p[i].setGame(game, i);
        }
        
        // start the game
        game.playGame();
		
	}
	
	
	/**
     * Gets a selection, based on user input, from a list of choices.
     * Because this is a GUI-based driver, we to this by creating a
     * dialog-box with button for each choice.
     * 
     * @param title a possible title for a dialog box
     * @param prompt a possible prompt-string for prompting the user
     * @param options an array of string containing the list of legal
     *   choices
     * @return the index in the options-array of the item that was selected
     */
    private int getSelection(String title, String prompt, String[] options) {
        // approximate the total button size, in order to determine which
        // style dialog box to use
        int maxLen = 0;
        for (int i = 0; i < options.length; i++) {
            maxLen = Math.max(maxLen, options[i].length());
        }
        
        if (maxLen*options.length <= 75) {
            // create the dialog box with the appropriate buttons; return
            // index of selection that user made
            return JOptionPane.showOptionDialog(null,
                    title,
                    prompt,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
        }
        else {
            // create menu-style dialog box; return index of selection that
            // user made
            Object obj =
                JOptionPane.showInputDialog(null,
                    title,
                    prompt,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
                    
            // if user pressed the "Cancel" button, return the index of
            // the options-array's last element
            if (obj == null) return options.length-1;
            
            // return the index of the user's selection
            for (int i = 0; i < options.length; i++) {
                if (obj.equals(options[i])) return i;
            }
            
            // if somehow we don't have a match, return the index of
            // the options-array's last element
            return options.length-1;
        }
    }
    
    
    // private arrays for selecting local players; the difference between
    // the two is that we don't allow the user to select "no more" until
    // the minimum number of players have been created.
    private static String[] networkCancelStrArray =
        {"network client", "Cancel"};
    private static String[] networkNoMoreCancelStrArray =
        {"network client", "No more", "Cancel"};
    
    /**
     * Creates an array of local players.  The type of players selected
     * will be based on user-responses to prompts.
     * 
     * @param dummyGame a Game object that is an instance of the game to
     *   be played.  (This allows us to ask it the minimum and maximum
     *   number of players that are allowed.)
     * @return the array of players for this game, as selected by the user
     * 
     */
    private RummyPlayer[] createLocalPlayers(RummyGame dummyGame) {
        // create an array that contains all the player choices, plus
        // the "network player" and "cancel" selections.
        String menuStrings[] = playerChoiceMenu(networkCancelStrArray);
        
        // create a vector for collecting the players
        Vector<RummyPlayer> playerVec = new Vector<RummyPlayer>();

        // the minimum and maximum players that are allowed to participate
        // in a single game
        int min = dummyGame.minPlayersAllowed();
        int max = dummyGame.maxPlayersAllowed();
        
        // the position (counting from the end) of the "network player"
        // selection in the array
        int networkPos = 2;
        
        // iterate until all players have been created
        for (;;) {

            // if we have reached the minimum number of players for the
            // game, we'll add "no more" to our list of choices.  Due to
            // the positions of the selections we also need to modify
            // our idea of where the "network player" selection is
            if (playerVec.size() == min) {
                menuStrings = playerChoiceMenu(networkNoMoreCancelStrArray);
                networkPos = 3;
            }
            
            // if we've reached the maximum number of players, quit the loop
            if (playerVec.size() >= max) break;

            // ask user what kind of game to play
            int response = getSelection(
                            "Player type ...",
                            "Player #"+(playerVec.size()+1),
                            menuStrings);

            // if user selects "Cancel", quit the program
            if (response == menuStrings.length-1) {
                System.exit(1);
            }
            // if user selects "network client", create it
            else if (response == menuStrings.length-networkPos) {
                playerVec.addElement((RummyPlayer)driver.createRemotePlayer());
            }
            // if user selects "No More", break out of the loop
            else if (response == menuStrings.length-2) {
                break;
            }
            // otherwise, create a local player of the appropriate type,
            // and add it to the vector
            else {
                playerVec.addElement(driver.createLocalPlayer(menuStrings[response]));
            }
        }

        // if user did not select enough players, exit (I don't think this
        // will ever happen unless the minimum is greater than the maximum)
        if (playerVec.size() < min) {
            errorMessage("Not enough players");
            System.exit(1);
        }
        
        // create an array to return the players in; copy the players
        // into the array; return it
        RummyPlayer[] rtnVal = new RummyPlayer[playerVec.size()];
        for (int i = playerVec.size()-1; i >= 0; i--) {
            rtnVal[i] = playerVec.elementAt(i);
        }
        return rtnVal;
    }
    
   /** 
    * Creates a array of choices for selecting a player.
    *
    * @param extras the "extra" options (i.e. those in addition
    *   to the player--for example "Cancel", "No more")
    * @return an array of String that contains all the player
    *   options plus the extra ones
    */
   private String[] playerChoiceMenu(String[] extras) {
   
       // compute the local-player choices
       String[] localChoices = driver.localPlayerChoices();
       
       // create a return value that contains room for all the player
       // choices, plus all the extra choices
       String[] rtnVal =
           new String[localChoices.length+(extras.length)];
           
       // copy all the local-player choices--then all the extra choices--
       // into the return-value array
       for (int i = 0; i < localChoices.length; i++) {
           String str = localChoices[i];
           rtnVal[i] = str;
       }
       for (int i = 0; i < extras.length; i++) {
           rtnVal[localChoices.length+i] = extras[i];
       }
       
       // return the filled array
       return rtnVal;
   }

    
    /**
     * Notify the user that an error has occured.
     *
     * @param message the text to display to the user
     */
    private void errorMessage(String message) {
        // pop up a dialog box with the message
        JOptionPane.showMessageDialog(null, message,
            "Please confirm",JOptionPane.ERROR_MESSAGE);
    }

}
