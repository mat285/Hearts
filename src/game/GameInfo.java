package game;

import card.*;
import java.util.*;

public final class GameInfo {
    private final Card TWO_OF_CLUBS = new Card(Suit.CLUBS, Value.TWO);
    private final Random RANDOM = new Random();

    private boolean _heartsBroken;
    private Trick _currentTrick;
    private int _roundNumber;
    private Map<IPlayer, Integer> _scores;
    private Map<IPlayer, Integer> _roundScore;
    private Map<IPlayer, Set<Card>> _playerHands;
    private Map<Card, IPlayer> _plays;
    private IPlayer[] _players;
    private Direction _passDirection;
    private int _currentPlayer;


    public GameInfo(IPlayer[] players) {
        _players = players;
        _playerHands = new HashMap<>();
        _scores = new HashMap<>();
        _roundNumber = 0;
    }

    protected void NextRound(Card[][] hands) {
        _roundNumber++;
        _currentTrick = new Trick();
        _plays = new HashMap<>();
        _roundScore = new HashMap<>();
        _heartsBroken = false;
        _passDirection = Direction.values()[_roundNumber % Direction.values().length];
        for (int i = 0; i < _players.length; i++) {
            HashSet<Card> hand = new HashSet<>();
            for (Card c : hands[i]) {
                hand.add(c);
            }
            _playerHands.put(_players[i],hand);
            _players[i].StartRound(hands[i]);
            _roundScore.put(_players[i], 0);
        }
    }

    protected void NextPlayerPlay() {
        Move move = CurrentPlayer().Play(this);
        if (!ValidateMove(move, CurrentPlayer())) move = RandomMove(CurrentPlayer());
        ExecuteMove(move, CurrentPlayer());
        _currentPlayer = (_currentPlayer+1) % _players.length;
    }

    protected void ExecuteMove(Move move, IPlayer player) {
        RemoveCardFromPlayersHand(move.Card(), player);
        _currentTrick.Add(move.Card());
        _plays.put(move.Card(), player);
    }

    protected void NextTrick() {
        Card high = _currentTrick.Highest();
        IPlayer winner = _plays.get(high);
        int newScore = _roundScore.get(winner) + _currentTrick.Points();
        _roundScore.put(winner, newScore);
        _currentTrick = new Trick();
        for (int i = 0; i < _players.length; i++) {
            if (_players[i] == winner) _currentPlayer = i;
        }
    }

    protected boolean IsRoundOver() {
        boolean roundOver = true;
        for (Set<Card> hand : _playerHands.values()) {
            roundOver = roundOver && hand.isEmpty();
        }
        return roundOver;
    }

    protected boolean IsTrickDone() {
        return _currentTrick.IsComplete();
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

    protected IPlayer FindAndSetFirstPlayer() {
        for (int i = 0; i < _players.length; i++) {
            if (_playerHands.get(_players[i]).contains(TWO_OF_CLUBS)) {
                _currentPlayer = i;
                return _players[i];
            }
        }
        return null;
    }

    protected void BreakHearts() {
        _heartsBroken = true;
    }

    /**
     * Assumes the following layout for passing cards:
     *
     *                  2
     *              1       3
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
        FindAndSetFirstPlayer();
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

    protected IPlayer[] Players() {
        return _players;
    }

    protected boolean ValidateMove(Move move, IPlayer player) {
        return GetAllValidMoves(player).contains(move);
    }

    public Move RandomMove(IPlayer player) {
        List<Move> moves = GetAllValidMoves(player);
        int idx = RANDOM.nextInt(moves.size());
        return moves.get(idx);
    }

    public List<Move> GetAllValidMoves(IPlayer player) {
        //TODO implement this
        return null;
    }
}
