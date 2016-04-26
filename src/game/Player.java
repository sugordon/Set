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

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object p) {
        return p instanceof Player && ((Player)p).getName() == this.getName();
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    public void increment(int s) {
        score += s;
    }
}
