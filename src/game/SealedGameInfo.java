package game;

import card.*;
import java.util.*;

public class SealedGameInfo {

    private Trick _currentTrick;
    private int[] _roundScores;
    private int[] _scores;
    private boolean _heartsBroken;
    private int _roundNumber;
    private boolean _isStartOfRound;
    private Set<Card> _hand;
    private Set<Card> _remaining;
    private int[] _cardDistribution;

    public SealedGameInfo(Trick currentTrick, int[] roundScores, int[] scores, boolean heartsBroken, int roundNumber, boolean isStartofRound, Set<Card> hand, Set<Card> remaining, int[] cardDistribution) {
        _currentTrick = currentTrick;
        _roundScores = roundScores;
        _scores = scores;
        _heartsBroken = heartsBroken;
        _roundNumber = roundNumber;
        _isStartOfRound = isStartofRound;
        _hand = hand;
        _remaining = remaining;
        _cardDistribution = cardDistribution;
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

    public Set<Card> GetHand() { return _hand; }

    public Set<Card> RemainingCards() { return _remaining; }

    public int[] CardDistribution() { return _cardDistribution; }

    public SealedGameInfo Clone() {
        Set<Card> hand = new HashSet<>(_hand);
        Set<Card> remaining = new HashSet<>(_remaining);
        return new SealedGameInfo(CurrentTrick(), RoundScore(), GameScores(),IsHeartsBroken(), RoundNumber(),IsStartOfRound(),hand, remaining, CardDistribution());
    }
}
