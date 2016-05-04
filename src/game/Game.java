/**
 *
 */
package game;

import network.Database;
import server.ServerMultiThread;

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
	private String lock = null;
	public HashSet<CardSet> allSets = new HashSet<CardSet>();
	private Timer lockTimer;
    public ArrayList<ServerMultiThread> threads = new ArrayList<>();

    public static final int LOCKTIME = 5000;

	private final String gameName;
	private final String owner;
	private final int maxPlayers;

	private final String pwd;

	public Game(String name, String owner, int numPlayers, String pwd) {
		this.gameName = name;
		this.owner = owner;
		this.maxPlayers = numPlayers;
		this.pwd = pwd;

		deck = Game.createDeck(deck);
		Collections.shuffle(deck, new Random(1223));
		deal(12);
		while (allSets.size() == 0) {
			deal(3);
		}
        System.out.println("SETS");
        for (CardSet s : allSets) {
			System.out.println(s.toString());
		}

		lockTimer = new Timer(Game.LOCKTIME, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
//				players.get(lock).increment(-1);
				lock = null;
				System.out.println("Lock Reset");
			}
		});
		lockTimer.setRepeats(false);
	}

	private Card[] deal(int cards){
        Card[] retVal = new Card[cards];
		for (; cards != 0; cards--) {
			if (deck.size() == 0 || (board.size() > 12 && allSets.size() == 0))
				return null;
			Card newCard = deck.remove(deck.size()-1);
            retVal[cards-1] = newCard;
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
		return retVal;
	}

	public boolean lock(String playerNum) {
		if (lock != null) {
			return false;
		}
		System.out.println("lock");
		lock = playerNum;
		//Resets the lock after 3 seconds
		lockTimer.start();
		return true;
	}

	public int replace(String sOne, String sTwo, String sThree, String name) {
		Player p = null;
		for (Player a : this.getPlayers()) {
			if (a.getName().equals(name)) {
				p = a;
				break;
			}
		}
        int playerNum = this.getPlayers().indexOf(p);
        int[] one = this.board.find(new Card(sOne));
        int[] two = this.board.find(new Card(sTwo));
        int[] three = this.board.find(new Card(sThree));
        if (one == null || two == null || three == null)
            return 3;
//		int[] one = {sOne.charAt(0), sOne.charAt(1)};
//		int[] two = {sTwo.charAt(0), sTwo.charAt(1)};
//		int[] three = {sThree.charAt(0), sThree.charAt(1)};
//		if (playerNum != lock)
//			return 2;
		if (remove(one, two, three)) {
			deal(3);
			while (allSets.size() == 0) {
				deal(3);
			}
			p.increment(1);
			lock = null;
			lockTimer.stop();

            System.out.println("SETS");
            for (CardSet s : allSets) {
                System.out.println(s.toString());
            }

			return 0;
		}
		p.increment(-1);
		return 1;
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

    public boolean removePlayer(String player) {
        Player a = null;
        for (Player p : this.getPlayers()) {
            if (p.getName().equals(player)) {
                a = p;
            }
        }
        if (a == null)
            return false;
        this.getPlayers().remove(a);
        return true;
    }

	private void remove(int[] loc) {
		Card removeCard = board.get(loc[0], loc[1]);
		board.remove(removeCard);
		for (Iterator<CardSet> i = allSets.iterator(); i.hasNext();) {
			CardSet element = i.next();
			if (element.contains(removeCard)) {
//				System.out.println(removeCard + " removing " + element);
				i.remove();
			}
		}
	}

	public static ArrayList<Card> createDeck(ArrayList<Card> deck) {
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
		return deck;
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

	public void addPlayer(String p, ServerMultiThread smt) {
        this.threads.add(smt);
		this.players.add(new Player(p));
	}

	public void kickPlayer(String p) {
		this.players.remove(new Player(p));
	}

	public String getLock() {
		return lock;
	}

	public String getGameName() {
		return gameName;
	}

	public String getOwner() {
		return owner;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public String getPwd() {
		return pwd;
	}

	public String toString() {
		String s = "";
		for (Card c : board) {
			s += c.toString();
		}
		return s+":";
	}
}
