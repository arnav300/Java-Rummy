package game;

/**
 * A GameQuitAction is an action that is sent by a player to the game
 * to tell it that it wants to quit the game.
 * 
 * @author Robert
 * 
 */
public class GameQuitAction extends GameAction {

    /**
     * Constructor for GameQuitAction
     *
     * @param source the player who created the action
     */
    public GameQuitAction(GamePlayer p)
    {
        // invoke superclass constructor to initialize the source
        super(p);
    }

    /**
     * Tells whether the action is a "quit game" action
     *
     * @return true iff the action is a "quit game" action
     */
    public boolean isQuitRequest() {
        return true;
    }
}
