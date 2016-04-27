package game;

import javax.swing.*;

public class Card extends JToggleButton{
	final int number;
	final int symbol;
	final int shading;
	final int color;

	public Card(int num, int sym, int sha, int col) {
		this.number = num;
		this.symbol = sym;
		this.shading = sha;
		this.color = col;
	}
	
	@Override
	public int hashCode() {
		return 1000*this.number + 100*this.symbol + 10*this.shading + this.color;
	}
	
	@Override
	public String toString() {
		return String.format("%04d", this.hashCode());
	}
}
