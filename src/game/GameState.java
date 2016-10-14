package game;

import card.*;
import java.util.*;

public class GameState {
    private final Card TWO_OF_CLUBS = new Card(Suit.CLUBS, Value.TWO);

    private boolean _heartsBroken;
    private Trick _currentTrick;
    private int _roundNumber;
    private Map<IPlayer, Integer> _scores;
    private Map<IPlayer, Set<Card>> _playerHands;
    private IPlayer[] _players;
    private Direction _passDirection;
    private int _currentPlayer;

    public GameState(IPlayer[] players) {
        _players = players;
        _playerHands = new HashMap<>();
        _scores = new HashMap<>();
        for (IPlayer p : _players) {
            _scores.put(p,0);
            _playerHands.put(p, new HashSet<Card>());
        }
        _roundNumber = 0;
    }

    protected void NextRound(Card[][] hands) {
        _roundNumber++;
        _currentTrick = new Trick();
        _heartsBroken = false;
        _passDirection = Direction.values()[_roundNumber % Direction.values().length];
        for (int i = 0; i < _players.length; i++) {
            HashSet<Card> hand = new HashSet<>();
            for (Card c : hands[i]) {
                if (c.equals(TWO_OF_CLUBS)) _currentPlayer = i;
                hand.add(c);
            }
            _playerHands.put(_players[i],hand);
            _players[i].StartRound(hands[i]);
        }
    }

    protected IPlayer CurrentPlayer() {
        return _players[_currentPlayer];
    }

    protected boolean DoesPlayerHaveCard(Card c, IPlayer p) {
        return _playerHands.get(p).contains(c);
    }

    protected boolean RemoveCardFromPlayersHand(Card c, IPlayer p) {
        return _playerHands.get(p).remove(c);
    }

    /**
     * Assumes the following layout for passing cards:
     *
     *                  2
     *              3       1
     *                  0
     *
     * @param moves the cardpassmoves of the players
     */
    protected void PassCards(CardPassMove[] moves) {
        switch (_passDirection) {
            case LEFT:
                passFromIToJ(moves,0,1);
                passFromIToJ(moves,1,2);
                passFromIToJ(moves,2,3);
                passFromIToJ(moves,3,0);
                break;

            case RIGHT:
                passFromIToJ(moves,0,3);
                passFromIToJ(moves,3,2);
                passFromIToJ(moves,2,1);
                passFromIToJ(moves,1,0);
                break;

            case ACROSS:
                passFromIToJ(moves,0,2);
                passFromIToJ(moves,2,0);
                passFromIToJ(moves,1,3);
                passFromIToJ(moves,3,1);
                break;

            default:
                break;
        }
    }

    private void passFromIToJ(CardPassMove[] moves, int i, int j) {
        CardPassMove ci = moves[i];
        CardPassMove cj = moves[j];
        IPlayer pi = _players[i];
        IPlayer pj = _players[j];
        _playerHands.get(pi).removeAll(ci.Cards());
        _playerHands.get(pj).addAll(ci.Cards());
        pj.ReceiveCards(cj,ci);
    }

    public boolean IsHeartsBroken() {
        return _heartsBroken;
    }

    public int RoundNumber() {
        return _roundNumber;
    }

    public int ScoreOfPlayer(IPlayer p) {
        return _scores.get(p);
    }

    public Suit CurrentSuit() {
        return _currentTrick.Suit();
    }

    public Direction PassDirection() {
        return _passDirection;
    }
}
