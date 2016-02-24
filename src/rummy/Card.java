package rummy;

import java.util.Comparator;

/**
 * Class to represent card from a traditional deck of cards. 
 * 
 * @author Robert Blatner
 * 
 */
public class Card implements Comparator<Card> {
	private int ID;
    private Rank RANK;
    private Suit SUIT;
    private String IMAGE;
   
    /**
     * Ctor for Card. All information is determined by id, which is 
     * a value from 1 - 52
     * 
     * @param id
     */
    public Card(int id) {
        ID = id;
        
        // Determine suit
        SUIT = Suit.createSuit(id);
        
        // Determine rank
        RANK = Rank.createRank(id);
        
        if(id < 10) IMAGE = "images//default//0" + id + ".gif";
        else IMAGE = "images//default//" + id + ".gif";
    }

    public String getName() {
        return RANK.toString();
    }

    public int getRank() {
        return RANK.toRank();
    }

    public int getID() {
        return ID;
    }
    
    public Suit getSuit(){
    	return SUIT;
    }
    
    public String getImage() {
        return IMAGE;
    }
    
    enum Rank{
    	ACE("Ace", 1),
    	TWO("Two", 2),
    	THREE("Three", 3),
    	FOUR("Four", 4),
    	FIVE("Five", 5),
    	SIX("Six", 6),
    	SEVEN("Seven", 7),
    	EIGHT("Eight", 8),
    	NINE("Nine", 9),
    	TEN("Ten", 10),
    	JACK("Jack", 11),
    	QUEEN("Queen", 12),
    	KING("King", 13);
    	
    	private final String name;
    	private final int rank;
    	
    	private Rank(String name, int rank){
    		this.name = name;
    		this.rank = rank;
    	}
    	
    	@Override
    	public String toString(){
    		return name;
    	}
    	
    	public int toRank(){
    		return rank;
    	}
    	
    	public static Rank createRank(int id){
    		
    		int rank = id % 13 + 1;
    		
    		switch(rank){
    		
    		case 1:
    			return Rank.ACE;
    		case 2:
    			return Rank.TWO;
    		case 3:
    			return Rank.THREE;
    		case 4:
    			return Rank.FOUR;
    		case 5:
    			return Rank.FIVE;
    		case 6:
    			return Rank.SIX;
    		case 7:
    			return Rank.SEVEN;
    		case 8:
    			return Rank.EIGHT;
    		case 9:
    			return Rank.NINE;
    		case 10:
    			return Rank.TEN;
    		case 11:
    			return Rank.JACK;
    		case 12:
    			return Rank.QUEEN;
    		case 13:
    			return Rank.KING;
    			
    		default:
    		
    			return null;
    		}
    		
    		
    	}

    }
    
    /**
     * Represents 4 suits in a standard deck of cards. 
     * 
     * @author Robert
     *
     */
    enum Suit {
    	SPADES, HEARTS, CLUBS, DIAMONDS;
    	
    	private Suit(){}
    	
    	public static Suit createSuit(int id){
    		if(id >= 1 && id <= 13){
    			return Suit.CLUBS;
    		}
    		else if(id >= 14 && id <= 26){
    			return Suit.DIAMONDS;
    		}
    		else if(id >= 27 && id <= 39){
    			return Suit.HEARTS;
    		}
    		else if(id >= 40 && id <= 52){
    			return Suit.SPADES;
    		}
    		else 
    			return null;
    	}
    }

    
    /**
     * Implements Comparator's comparison operator for Card class. This
     * enables Vector<Card>.sort(Card obj).
     * 
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     * 
     */
	@Override
	public int compare(Card c1, Card c2) {

		return c1.getID() - c2.getID();
	} 
	
	
	@Override
	public boolean equals(Object otherCard){
		
		if( ((Card)otherCard).ID == this.ID) return true;
		else return false;
		
	}

}


