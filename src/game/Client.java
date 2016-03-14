package game;

import java.util.ArrayList;

public class Client {
	private int playerNum;
	private ArrayList<Card> deck = new ArrayList<Card>(81);
	private Board board = new Board();

	public Client(int seed, int playerNum) {
		this.playerNum = playerNum;
		Game.createDeck(seed, deck);
	}
	public void deal(int num) {
		for (; num != 0; num--) {
			Card newCard = deck.remove(deck.size()-1);
			board.insert(newCard);
		}
	}
	public void sendLock() {

	}
	public void sendSet(int[] one, int[] two, int[] three) {
		
	}
}
