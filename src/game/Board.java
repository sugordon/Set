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

	public int getMaxRow() {
		return maxRow;
	}

	public int[] find(Card c) {
		int[] ans = new int[2];
		for (ans[0] = 0; ans[0] < 7; ans[0]++) {
			for (ans[1] = 0; ans[1] < 3; ans[1]++) {
                if (board[ans[0]][ans[1]] == null) {
                    continue;
                }
				if (board[ans[0]][ans[1]].equals(c)) {
					return ans;
				}
			}
		}
		System.out.println(c.toString() + " is null");
		return null;
	}

	public void remove(Card c) {
		if (cards == 0) {
			System.err.println("Tried to remove empty cell");
			return;
		}
		int[] loc = this.find(c);
		cards -= 1;
		board[loc[0]][loc[1]] = null;
		if (loc[0] == maxRow - 1 &&
				board[loc[0]][0] == null &&
				board[loc[0]][1] == null &&
				board[loc[0]][2] == null) {
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
	public void removeAll() {
        for (int index = 0; index < board.length; index++) {
            for (int inner = 0; inner < board[index].length; inner++) {
                board[index][inner] = null;
            }
        }
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
