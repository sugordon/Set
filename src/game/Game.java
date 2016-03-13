/**
 * 
 */
package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 * @author User Name
 *
 */
public class Game {
	private ArrayList<Card> deck = new ArrayList<Card>(81);
	private Board board = new Board();
	private HashSet<Set> allSets = new HashSet<Set>();

	public Game() {
		this.createDeck();
		deal(12);
		while (allSets.size() == 0) {
			deal(3);
		}
		System.out.println(allSets.size());
		for (Set s : allSets) {
			System.out.println(s.toString());
		}
	}
	
	private boolean deal(int cards){
		for (; cards != 0; cards--) {
			if (deck.size() == 0)
				return false;
			Card newCard = deck.remove(deck.size()-1);
			board.insert(newCard);
			for (Card card1 : board) {
				for (Card card2 : board) {
					if (card1 == card2)
						continue;
					if (card1 == null || card2 == null)
						break;
					if (Set.isSet(card1, card2, newCard)) {
						allSets.add(new Set(card1, card2, newCard));
					}
				}
			}
		}
		return true;
	}
	
	private void createDeck() {
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
		Collections.shuffle(deck, new Random(5122));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game();
		Scanner s = new Scanner(System.in);
		while (s.hasNext()) {
			System.out.println("INPUT " + s.nextLine());
		}
	}

}
