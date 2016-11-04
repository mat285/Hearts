package game;

public class ScoredPlayer implements Comparable<ScoredPlayer> {
    private IPlayer _player;
    private int _score;

    public ScoredPlayer(IPlayer player, int score) {
        _player = player;
        _score = score;
    }

    public IPlayer Player() {
        return _player;
    }

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
