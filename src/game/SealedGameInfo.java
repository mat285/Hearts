package game;

import card.*;

public class SealedGameInfo {

    private Trick _currentTrick;
    private int[] _roundScores;
    private int[] _scores;
    private boolean _heartsBroken;
    private int _roundNumber;
    private boolean _isStartOfRound;

    public SealedGameInfo(Trick currentTrick, int[] roundScores, int[] scores, boolean heartsBroken, int roundNumber, boolean isStartofRound) {

    }

    public Card LastCardPlayed() {
        return _currentTrick.LastCardAdded();
    }

    public Trick CurrentTrick() {
        return _currentTrick.Clone();
    }

    public int[] RoundScore() {
        return _roundScores;
    }

    public int[] GameScores() {
        return _scores;
    }

    public boolean IsHeartsBroken() {
        return _heartsBroken;
    }

    public int RoundNumber() {
        return _roundNumber;
    }

    public Suit CurrentSuit() {
        return _currentTrick.Suit();
    }

    public boolean IsStartOfRound() {
        return _isStartOfRound;
    }
}
