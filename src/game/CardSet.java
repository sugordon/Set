package game;

import java.util.ArrayList;

public class CardSet {
	ArrayList<Card> set = new ArrayList<Card>(3);
	
	public CardSet(Card one, Card two, Card three) {
		set.add(one);
		set.add(two);
		set.add(three);
	}
	
	public boolean contains(Card c) {
		return set.contains(c);
	}
	
	public static boolean isSet(Card one, Card two, Card three) {
		if (one == null || two == null || three == null)
			return false;
		return (CardSet.isSame(one.color, two.color, three.color) &&
				CardSet.isSame(one.number, two.number, three.number) &&
				CardSet.isSame(one.shading, two.shading, three.shading) &&
				CardSet.isSame(one.symbol, two.symbol, three.symbol));
	}
	
	private static boolean isSame(int one, int two, int three) {
		return (one == two && one == three ||
				one != two && two != three && one != three);
	}
	
	@Override
	public String toString() {
		return set.toString();
	}
}
