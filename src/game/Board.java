package game;

import java.util.Iterator;

public class Board implements Iterable<Card> {
	
	private Card[][] board = new Card[7][3];
	private int maxRow = 4;
	private int cards = 0;
	
	public void insert(Card c) {
		//Finds the first empty slot
		int row = 0;
		int col = 0;
		outerLoop:
		for (row = 0; row < maxRow + 1; row++) {
			for (col = 0; col < 3; col++) {
				if (board[row][col] == null) {
					break outerLoop;
				}
			}
		}
		cards += 1;
		if (row >= maxRow) {
			maxRow += 1;
		}
		board[row][col] = c;
	}
	
	public int size() {
		return cards;
	}
	
	public Card get(int row, int col) {
		return board[row][col];
	}
	
	public void remove(int row, int col) {
		if (cards == 0) {
			System.err.println("Tried to remove empty cell");
			return;
		}
		cards -= 1;
		board[row][col] = null;
		if (row == maxRow - 1 &&
				board[row][0] == null &&
				board[row][1] == null &&
				board[row][2] == null) {
			maxRow -= 1;
		}
	}
	
	@Override
	public Iterator<Card> iterator() {
		return new Iterator<Card>(){
			int row = 0;
			int col = 0;

			@Override
			public boolean hasNext() {
				return row < maxRow && col < 3;
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
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				Card c = this.get(i, j);
				if (c != null) {
					s += this.get(i, j).toString() + " ";
				}
				else {
					s += "null ";
				}
			}
			s += System.lineSeparator();
		}
		return s;
	}

}
