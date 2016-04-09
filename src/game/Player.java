package game;

public class Player {
	private int score = 0;
	final String name;
	public Player(String name) {
		this.name = name;
	}

    public int getScore() {
        return score;
    }

    public void increment(int s) {
        score += s;
    }
}
