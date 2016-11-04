package game;

public class ScoredPlayer implements Comparable<ScoredPlayer> {
    private IPlayer _player;
    private int _score;

    /**
     * Creates a new Scored Player with the given IPlayer and score
     * @param player
     * @param score
     */
    public ScoredPlayer(IPlayer player, int score) {
        _player = player;
        _score = score;
    }

    /**
     * Gets the player of this object
     * @return The player
     */
    public IPlayer Player() {
        return _player;
    }

    /**
     * Gets the score of this player
     * @return This player's score
     */
    public int Score() {
        return _score;
    }

    @Override
    public int compareTo(ScoredPlayer s) {
        return _score - s._score;
    }

    @Override
    public String toString() {
        return _player.toString() + " " + _score;
    }
}
