package game;

import card.*;
import java.util.*;

public class SealedGameInfo implements Cloneable{

    private int _currentPlayer;
    private Trick _currentTrick;
    private int[] _roundScores;
    private int[] _scores;
    private boolean _heartsBroken;
    private int _roundNumber;
    private boolean _isStartOfRound;
    private Set<Card> _hand;
    private Set<Card> _remaining;
    private int[] _cardDistribution;

    public SealedGameInfo(int currentPlayer, Trick currentTrick, int[] roundScores, int[] scores, boolean heartsBroken, int roundNumber, boolean isStartofRound, Set<Card> hand, Set<Card> remaining, int[] cardDistribution) {
        _currentPlayer = currentPlayer;
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

    public int CurrentPlayer() { return _currentPlayer; }

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

    public @Override boolean equals(Object o){
        SealedGameInfo info = (SealedGameInfo) o;
        return o instanceof SealedGameInfo
                && info._currentTrick.equals(_currentTrick)
                && info._heartsBroken == _heartsBroken
                && info._isStartOfRound == _isStartOfRound
                && info._hand.equals(_hand)
                && info._remaining.equals(_remaining)
                && info._roundNumber == _roundNumber
                && Arrays.equals(info._roundScores, _roundScores)
                && Arrays.equals(info._scores, _scores)
                && Arrays.equals(info._cardDistribution, _cardDistribution);
    }

    public @Override int hashCode(){
        int result = 1;

        result = result*37 + _currentTrick.hashCode();
        result = result*37 + (_heartsBroken ? 0 : 1);
        result = result*37 + (_isStartOfRound ? 0 : 1);
        result = result*37 + _hand.hashCode();
        result = result*37 + _remaining.hashCode();
        result = result*37 + _roundNumber;
        for(int i : _roundScores){
            result = result*37 + i;
        }
        for(int i : _scores){
            result = result*37 + i;
        }
        for(int i : _cardDistribution){
            result = result*37 + i;
        }

        return result;
    }

    public SealedGameInfo Clone(){
        Set<Card> clonedHand = new HashSet<>();
        Set<Card> clonedRemaining = new HashSet<>();

        clonedHand.addAll(_hand);
        clonedRemaining.addAll(_remaining);

        return new SealedGameInfo(_currentPlayer, _currentTrick.Clone(), _roundScores.clone(),
                _scores.clone(), _heartsBroken, _roundNumber, _isStartOfRound,
                clonedHand, clonedRemaining, _cardDistribution.clone());

    }
}
