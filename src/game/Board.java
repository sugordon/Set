package game;

import java.util.Iterator;

public class Board implements Iterable<Card> {
	
	private Card[][] board = new Card[7][3];
	private int maxrow = 4;
	private int cards = 0;
	
	public void insert(Card c) {
		//Finds the first empty slot
		int row = 0;
		int col = 0;
		outerloop:
		for (row = 0; row < maxrow + 1; row++) {
			for (col = 0; col < 3; col++) {
				if (board[row][col] == null) {
					break outerloop;
				}
			}
		}
		cards += 1;
		if (row >= maxrow) {
			maxrow += 1;
		}
		board[row][col] = c;
	}
	
	public void remove(int row, int col) {
		if (cards == 0) {
			System.err.println("Tried to remove empty cell");
			return;
		}
		cards -= 1;
		board[row][col] = null;
		if (row == maxrow - 1 &&
				board[row][0] == null &&
				board[row][1] == null &&
				board[row][2] == null) {
			maxrow -= 1;
		}
	}
	
	@Override
	public Iterator<Card> iterator() {
		return new Iterator<Card>(){
			int row = 0;
			int col = 0;

			@Override
			public boolean hasNext() {
				return row < maxrow && col < 3;
			}

			@Override
			public Card next() {
				Card retCard = board[row][col];
				if (++col > 2) {
					col = 0;
					row++;
				}
				if (retCard == null && hasNext()) {
					return this.next();
				}
				return retCard;
			}};
	}

}
