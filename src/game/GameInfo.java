package game;

import card.*;
import java.util.*;

public final class GameInfo {
    private final Card TWO_OF_CLUBS = new Card(Suit.CLUBS, Value.TWO);
    private final int SIZE_OF_HANDS = 13;
    private final int MAX_GAME_SCORE = 100;
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
        for (IPlayer player : _players) {
            _scores.put(player,0);
        }
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
        if (move.Card().Suit == Suit.HEARTS) BreakHearts();
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
        _plays = new HashMap<>();
    }

    protected void EndRound() {
        for (Map.Entry<IPlayer,Integer> entry : _roundScore.entrySet()) {
            _scores.put(entry.getKey(),_scores.get(entry.getKey()) + entry.getValue());
        }
    }

    protected boolean IsGameOver() {
        for (Integer score : _scores.values()) {
            if (score >= MAX_GAME_SCORE) return true;
        }
        return false;
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

    protected List<IPlayer> GetLosers() {
        List<IPlayer> loosers = new ArrayList<>();
        if (!IsGameOver()) return loosers;
        int maxScore = Integer.MIN_VALUE;
        for (Integer score : _scores.values()) {
            if (score > maxScore) maxScore = score;
        }
        for (Map.Entry<IPlayer,Integer> entry : _scores.entrySet()) {
            if (entry.getValue() == maxScore) loosers.add(entry.getKey());
        }
        return loosers;
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
        moves = ValidateCardPassMoves(moves);
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

    public Trick CurrentTrick() { return _currentTrick.Clone(); }

    public Direction PassDirection() {
        return _passDirection;
    }

    protected IPlayer[] Players() {
        return _players;
    }

    public List<Card> HandOfPlayer(IPlayer player) {
        List<Card> hand = new ArrayList<>();
        hand.addAll(_playerHands.get(player));
        return hand;
    }

    protected CardPassMove[] ValidateCardPassMoves(CardPassMove[] moves) {
        for (int i = 0; i < moves.length; i++) {
            if (!IsValidCardPass(moves[i], _players[i])){
                moves[i] = RandomCardPass(_players[i]);
            }

        }
        return moves;
    }

    protected boolean IsValidCardPass(CardPassMove move, IPlayer player) {
        if (move == null) return false;
        for (Card c : move.Cards()) {
            if (!doesPlayerHaveCard(player, c)) return false;
        }
        return true;
    }

    public CardPassMove RandomCardPass(IPlayer player) {
        List<Card> cards = new ArrayList<>(_playerHands.get(player));
        return new CardPassMove(cards.remove(RANDOM.nextInt(cards.size())),cards.remove(RANDOM.nextInt(cards.size())),cards.remove(RANDOM.nextInt(cards.size())));
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
        List<Move> valid = new ArrayList<>();
        for (Card c : _playerHands.get(player)) {
            if (IsCardValid(c,player)) valid.add(new Move(c));
        }
        return valid;
    }

    public boolean IsCardValid(Card c, IPlayer player) {
        if (!doesPlayerHaveCard(player,c)) return false;
        Suit curr = CurrentSuit();
        // If this is the start of a round then only the two of clubs is valid
        if (isStartOfRound()) return c.equals(TWO_OF_CLUBS);
        if (curr == null) {
            // If this is the first card, then either play a card that isn't hearts or hearts must be broken
            return (IsHeartsBroken() || c.Suit != Suit.HEARTS || containsOnlySuit(player, c.Suit));
        }
        // Else play on suit, or if player has none then play anything
        return (c.Suit == curr || containsNoneOfSuit(player, curr));

    }

    private boolean isStartOfRound() {
        return _currentTrick.IsEmpty() && _playerHands.get(CurrentPlayer()).size() == SIZE_OF_HANDS;
    }

    private boolean containsOnlySuit(IPlayer player, Suit s) {
        for (Card c : _playerHands.get(player)) {
            if (c.Suit != s) return false;
        }
        return true;
    }

    private boolean containsNoneOfSuit(IPlayer player, Suit s) {
        for (Card c : _playerHands.get(player)) {
            if (c.Suit == s) return false;
        }
        return true;
    }

    private boolean doesPlayerHaveCard(IPlayer player, Card c) {
        return _playerHands.get(player).contains(c);
    }

    public void PrintDebugInfo() {
        System.out.println("Round: " + _roundNumber);
        System.out.println("Scores:");
        for (int i = 0; i < _players.length; i++) {
            System.out.println("Player " + i + ": Game: " +_scores.get(_players[i]) + " Round: " + _roundScore.get(_players[i]));
        }
        System.out.println("Hands:");
        for (int i = 0; i < _players.length; i++) {
            List<Card> cards = new ArrayList<>(_playerHands.get(_players[i]));
            Deck.Sort(cards);
            System.out.println("Player " + i + ": " +cards);
        }
        System.out.println("Played moves:");
        for (Card c : _currentTrick.AllCards()) {
            for (int i = 0; i < _players.length; i++) {
                if (_players[i].equals(_plays.get(c))) System.out.println("Player " + i + ": " + c);
            }
        }
        System.out.println("Current Trick: " + _currentTrick.toString());
        System.out.println("Current Player: " + _currentPlayer);
        System.out.println();
    }
}
