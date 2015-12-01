package game;

public abstract class GameHumanPlayer implements GamePlayer {

    protected Game game;
	protected int playerId;
	
	 /**
     * Constructor for the GameComputerPlayer class.
     */
    public GameHumanPlayer() {

        // set game to null and playerId to -1, indicating that the player
        // is not yet associated with any game
        game = null;
        this.playerId = -1;

    }
	
	/**
     * Lets the player know what the game object is.
     * 
     * @param game the game object
     * @param playerId the unique numeric player-id assigned to this
     *  player by the game
     */
	@Override
    public void setGame(Game game, int playerId){
        // disallow setting game if one is already set
        // (actually, we're allowing that so that the remote-GUI stuff
        // will work)
        if (true || this.game == null) {
            this.game = game; // set game
            this.playerId = playerId; // set player Id
            setGameMore(); // do additional initialization
            
        }
    }

   /**
     * performs additional initialization, as required by the GUI
     *
     */
    protected void setGameMore() {
        // does nothing, but may be overriden by subclasses to do additional
        // initialization
    }

	
	@Override
	public void requestMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notYourMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeToQuit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void illegalMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameIsOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishUp() {
		// TODO Auto-generated method stub
		
	}


	/**
     * Gets the player's numeric id from the pespective of the game
     * it's playing in.  (E.g., 0 if player 0, 1 if player 1.)
     *
     * @return the player's numeric id, from the game's viewpoint.
     */
	@Override
    public int getId() {
        return playerId;
    }

	
    /**
     * Tells whether the player is ready to play the game.
     *
     * @return a boolean value indicating whether the player is ready
     *   to play.
     */
	@Override
    public boolean isReady() {
        // typically, a GUI player is always ready to play
        return true;
    }

}
