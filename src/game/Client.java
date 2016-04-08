package game;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import network.Database;

public class Client {
	private int playerNum;
	private ArrayList<Card> deck = new ArrayList<Card>(81);
	private Board board = new Board();

	public Client(int seed, int playerNum) {
		this.playerNum = playerNum;
		Game.createDeck(seed, deck);
	}
	
	public boolean connect() {
		return false;
	}
	
	public boolean registerUser(String name, String pwd) {
		return Database.newUser(name, pwd);
	}
	
	public boolean login(String name, String pwd) {
		return Database.auth(name, pwd);
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
