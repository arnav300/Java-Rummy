package game;

/**
 * A GameNullAction is an action that is sent generated automatically
 * whenever a game asks for an action, but no action exists.
 * 
 * @author Robert
 * 
 */
public class GameNullAction extends GameAction {

    /**
     * Constructor for objects of class GameNullAction
     */
    public GameNullAction(GamePlayer p) {
        super(p);
    }

    /**
     * Tells whether the action is a "null" action
     *
     * @return true iff the action is null action, indicating that no
     *  actions have been requested by any player
     */
    public boolean isNullAction() {
        // return "true" because this object is a null action
        return true;
    }
}