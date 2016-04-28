package game;

import javax.swing.*;

public class Card extends JToggleButton{
//	final int number;
//	final int symbol;
//	final int shading;
//	final int color;
    final int hash;

	public Card(int num, int sym, int sha, int col) {
        this.hash = 1000*num+ 100*sym+ 10*sha+ col;
//		this.number = num;
//		this.symbol = sym;
//		this.shading = sha;
//		this.color = col;
	}

    public Card(String input) {
        hash = Integer.parseInt(input);
    }
	
	@Override
	public int hashCode() {
		return hash;
	}

    @Override
    public boolean equals(Object o) {
        return o.hashCode() == this.hashCode();
    }
	
	@Override
	public String toString() {
		return String.format("%04d", this.hashCode());
	}
}
