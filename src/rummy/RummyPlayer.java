package rummy;

import java.util.*;

import game.*;

/**
 * Interface for RummyPlayers. 
 * 
 * @author Robert Blatner
 * 
 */
interface RummyPlayer extends GamePlayer {

    public void setHand(Vector<Card> newHand);

    public Vector<Card> getHand();
}