/**
 * 
 */
package game;

import network.Database;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * @author User Name
 *
 */
public class Game {
	private ArrayList<Card> deck = new ArrayList<Card>(81);
	private Board board = new Board();
    private ArrayList<Player> players = new ArrayList<Player>();
    private int lock = -1;
    private HashSet<CardSet> allSets = new HashSet<CardSet>();
	private Timer lockTimer;

	private final String gameName;
    private final Player owner;
    private final int maxPlayers;

    private final String pwd;

	public Game(String name, Player owner, int numPlayers, String pwd) {
        this.gameName = name;
        this.owner = owner;
        this.maxPlayers = numPlayers;
        this.pwd = pwd;
        players.add(owner);

        this.createDeck(deck);
		deal(12);
		while (allSets.size() == 0) {
			deal(3);
		}
		for (CardSet s : allSets) {
			System.out.println(s.toString());
		}
		for (int i = 0; i < numPlayers; i++) {
			players.add(new Player("Bill"));
		}

		lockTimer = new Timer(3000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					players.get(lock).increment(-1);
					lock = -1;
					System.out.println("Lock Reset");
				}
			});
		lockTimer.setRepeats(false);
	}
	
	private boolean deal(int cards){
		for (; cards != 0; cards--) {
			if (deck.size() == 0 || (board.size() > 12 && allSets.size() == 0))
				return false;
			Card newCard = deck.remove(deck.size()-1);
			board.insert(newCard);
			for (Card card1 : board) {
				for (Card card2 : board) {
					if (card1 == card2)
						continue;
					if (card1 == null || card2 == null)
						break;
					if (CardSet.isSet(card1, card2, newCard)) {
						allSets.add(new CardSet(card1, card2, newCard));
					}
				}
			}
		}
		return true;
	}
	
	public boolean lock(int playerNum) {
		if (lock != -1) {
			return false;
		}
		System.out.println("lock");
		lock = playerNum;
		//Resets the lock after 3 seconds
		lockTimer.start();
		return true;
	}
	
	public boolean replace(int[] one, int[] two, int[] three, int playerNum) {
		if (playerNum != lock)
			return false;
		if (remove(one, two, three)) {
			deal(3);
			while (allSets.size() == 0) {
				deal(3);
			}
			players.get(playerNum).increment(1);
			lock = -1;
			lockTimer.stop();
			return true;
		}
		players.get(playerNum).increment(-1);
		return false;
	}
	
	private boolean remove(int[] one, int[] two, int[] three) {
		Card card1 = board.get(one[0], one[1]);
		Card card2 = board.get(two[0], two[1]);
		Card card3 = board.get(three[0], three[1]);
		if (!CardSet.isSet(card1, card2, card3)) {
			return false;
		}
		remove(one);
		remove(two);
		remove(three);
		return true;
	}
	
	private void remove(int[] loc) {
		Card removeCard = board.get(loc[0], loc[1]);
		board.remove(loc[0], loc[1]);
		for (Iterator<CardSet> i = allSets.iterator(); i.hasNext();) {
		    CardSet element = i.next();
		    if (element.contains(removeCard)) {
		    	System.out.println(removeCard + " removing " + element);
		    	i.remove();
		    }
		}
	}
	
	public void createDeck(ArrayList<Card> deck) {
		deck.clear();
		int[] vals = {0, 0, 0, 0};
		//Initialize the Deck
		for (int i = 0; i < 81; i++) {
			deck.add(new Card(vals[0], vals[1], vals[2], vals[3]));
			int carry = 1;
			for (int j = 0; j < 4; j++) {
				vals[j] += carry;
				carry = 0;
				if (vals[j] > 2) {
					carry = 1;
					vals[j] = 0;
				}
			}
		}
		//Collections.shuffle(deck);
		//Always shuffle the same
		Collections.shuffle(deck);
	}

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getLock() {
        return lock;
    }

    public String getGameName() {
        return gameName;
    }

    public Player getOwner() {
        return owner;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getPwd() {
        return pwd;
    }

	public static void main(String[] args) {
		Game game = new Game("game", new Player("Gordon"), 5, Database.hash("1234"));
		Scanner s = new Scanner(System.in);
		System.out.println("Removing");
		int[] one = 	{0, 1};
		int[] two = 	{0, 0};
		int[] three = 	{2, 0};
		System.out.println(game.board.get(0, 1));
		game.lock(0);
		game.replace(one, two, three, 0);
		System.out.println("Removed");
		System.out.println(game.board);
		for (CardSet set : game.allSets) {
			System.out.println(set);
		}
		while (s.hasNext()) {
			s.nextLine();
			game.lock(5);
		}
		s.close();
	}
}
