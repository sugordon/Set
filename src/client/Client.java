package Client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import game.Board;
import game.Card;
import game.Game;
import network.Database;

public class Client {
	private int playerNum;
	private ArrayList<Card> deck = new ArrayList<Card>(81);
	private Board board = new Board();

	public boolean start() { return false; }

	private boolean connect() {
		return false;
	}
	
	public int registerUser(String name, String pwd) {
		return Database.newUser(name, Database.hash(pwd));
	}
	
	public boolean login(String name, String pwd) {
		return Database.auth(name, Database.hash(pwd));
	}
	
	public void sendLock() {

	}

	public void sendSet(int[] one, int[] two, int[] three) {
		
	}
}
