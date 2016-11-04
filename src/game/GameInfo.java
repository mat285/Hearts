package game;

import card.*;
import java.util.*;

public final class GameInfo {
    private final Card TWO_OF_CLUBS = new Card(Suit.CLUBS, Value.TWO);
    private final int MAX_POINTS_PER_ROUND = 26;
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


    /**
     * Instantiates a new GameInfo object with the given array of players
     * @param players The players of the game
     */
    public GameInfo(IPlayer[] players) {
        _players = players;
        _playerHands = new HashMap<>();
        _scores = new HashMap<>();
        for (IPlayer player : _players) {
            _scores.put(player,0);
        }
        _roundNumber = 0;
    }

    /**
     * Starts a new round. Increments the round number, gives the cards the players, and initializes necessary
     * bookkeeping data structures.
     * @param hands The hands for each player in this round
     */
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

    /**
     * Calls the play move on the current player, validates and then executes the move
     */
    protected void NextPlayerPlay() {
        Move move = CurrentPlayer().Play(Seal());
        if (!ValidateMove(move, CurrentPlayer())) move = RandomMove(CurrentPlayer());
        ExecuteMove(move, CurrentPlayer());
        _currentPlayer = (_currentPlayer+1) % _players.length;
    }

    /**
     * Plays the given move into the current trick for the given player
     * @param move The move to play
     * @param player The player making the move
     */
    protected void ExecuteMove(Move move, IPlayer player) {
        RemoveCardFromPlayersHand(move.Card(), player);
        _currentTrick.Add(move.Card());
        if (move.Card().Suit == Suit.HEARTS) BreakHearts();
        _plays.put(move.Card(), player);
    }

    /**
     * Initializes the next trick and scores the winner of the current trick
     */
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

    /**
     * Ends the current round and tallies up the score
     */
    protected void EndRound() {
        if (!IsRoundOver()) return;
        IPlayer moonShooter = null;
        for (Map.Entry<IPlayer,Integer> entry : _roundScore.entrySet()) {
            if (entry.getValue() == MAX_POINTS_PER_ROUND) moonShooter = entry.getKey();
        }
        if (moonShooter != null) {
            for (IPlayer player : _players) {
                if (player != moonShooter) _scores.put(player, _scores.get(player) + MAX_POINTS_PER_ROUND);
            }
        } else {
            for (Map.Entry<IPlayer,Integer> entry : _roundScore.entrySet()) {
                _scores.put(entry.getKey(),_scores.get(entry.getKey()) + entry.getValue());
            }
        }
    }

    /**
     * Checks if the game is over by seeing if any player has met or gone over MAX_GAME_SCORE
     * @return true iff this game is over
     */
    protected boolean IsGameOver() {
        for (Integer score : _scores.values()) {
            if (score >= MAX_GAME_SCORE) return true;
        }
        return false;
    }

    /**
     * Checks if the round is over by seeing if all players are out of cards
     * @return true iff the current round is over
     */
    protected boolean IsRoundOver() {
        boolean roundOver = true;
        for (Set<Card> hand : _playerHands.values()) {
            roundOver = roundOver && hand.isEmpty();
        }
        return roundOver;
    }

    /**
     * Checks if the current trick is complete
     * @return true iff the current trick is complete
     */
    protected boolean IsTrickDone() {
        return _currentTrick.IsComplete();
    }

    /**
     * Returns the current ranking of players if the game is over, otherwise returns nothing
     * @return the final ranking of the players
     */
    public List<ScoredPlayer> GetRanking() {
        List<ScoredPlayer> sort = new ArrayList<>();
        if (!IsGameOver()) return sort;
        for (IPlayer player : _players) {
            sort.add(new ScoredPlayer(player, _scores.get(player)));
        }
        Collections.sort(sort);
        return sort;
    }

    /**
     * Gets the player whose turn it is
     * @return The currently playing player
     */
    protected IPlayer CurrentPlayer() {
        return _players[_currentPlayer];
    }

    /**
     * Checks if the player has the given card in their hand
     * @param c the card
     * @param p the player to check
     * @return true iff the p has card c
     */
    protected boolean DoesPlayerHaveCard(Card c, IPlayer p) {
        return _playerHands.get(p).contains(c);
    }

    /**
     * Removes the given card from the given player's hand
     * @param c the card
     * @param p the player
     */
    protected boolean RemoveCardFromPlayersHand(Card c, IPlayer p) {
        return _playerHands.get(p).remove(c);
    }

    /**
     * Finds the player with the Two of Clubs and sets them as the current player
     * @return the player with the Two of Clubs
     */
    protected IPlayer FindAndSetFirstPlayer() {
        for (int i = 0; i < _players.length; i++) {
            if (_playerHands.get(_players[i]).contains(TWO_OF_CLUBS)) {
                _currentPlayer = i;
                return _players[i];
            }
        }
        return null;
    }

    /**
     * Gets the last played card
     * @return the card last played
     */
    public Card LastCardPlayed() {
        return _currentTrick.LastCardAdded();
    }

    /**
     * Sets HeartsBroken to True
     */
    protected void BreakHearts() {
        _heartsBroken = true;
    }

    /**
     * Passes cards between players
     * Assumes the following layout for passing cards:
     *
     *                  2
     *              1       3
     *                  0
     *
     * @param moves the CardPassMoves of the players
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

    /**
     * Passes cards from player i to player j
     * @param moves the card pass moves
     * @param i the player to pass from
     * @param j the player to pass to
     */
    private void passFromIToJ(CardPassMove[] moves, int i, int j) {
        CardPassMove ci = moves[i];
        CardPassMove cj = moves[j];
        IPlayer pi = _players[i];
        IPlayer pj = _players[j];
        _playerHands.get(pi).removeAll(ci.Cards());
        _playerHands.get(pj).addAll(ci.Cards());
        pj.ReceiveCards(cj,ci);
    }

    /**
     * Checks whether hearts has been broken
     * @return true iff hearts has been broken
     */
    public boolean IsHeartsBroken() {
        return _heartsBroken;
    }

    /**
     * Gets the number of the current round
     * @return the round number
     */
    public int RoundNumber() {
        return _roundNumber;
    }

    /**
     * Returns the score of the given player in the game
     * @param p The player whose score to get
     * @return the score of p
     */
    public int ScoreOfPlayer(IPlayer p) {
        return _scores.get(p);
    }

    /**
     * Gets the current suit of the current trick
     * @return the suit of the current trick
     */
    public Suit CurrentSuit() {
        return _currentTrick.Suit();
    }

    /**
     * Gets a copy of the current trick
     * @return A copy of the current trick
     */
    public Trick CurrentTrick() { return _currentTrick.Clone(); }

    /**
     * Gets the direction to pass cards
     * @return the direction to pass cards
     */
    public Direction PassDirection() {
        return _passDirection;
    }

    /**
     * Gets all of the players in this game
     * @return the players of this game
     */
    protected IPlayer[] Players() {
        return _players;
    }

    /**
     * Gets the hand of the given player
     * @param player the player
     * @return the hand of the player
     */
    public List<Card> HandOfPlayer(IPlayer player) {
        List<Card> hand = new ArrayList<>();
        hand.addAll(_playerHands.get(player));
        return hand;
    }

    /**
     * Validates that the card passing moves are okay, if not fixes them
     * @param moves the proposed card passing moves
     * @return the fixed card passing moves
     */
    protected CardPassMove[] ValidateCardPassMoves(CardPassMove[] moves) {
        for (int i = 0; i < moves.length; i++) {
            if (!IsValidCardPass(moves[i], _players[i])){
                moves[i] = RandomCardPass(_players[i]);
            }

        }
        return moves;
    }

    /**
     * Checks if the card pass move is okay for the given player
     * @param move the move
     * @param player the player
     * @return true iff the player can pass these cards
     */
    protected boolean IsValidCardPass(CardPassMove move, IPlayer player) {
        if (move == null) return false;
        for (Card c : move.Cards()) {
            if (!doesPlayerHaveCard(player, c)) return false;
        }
        return true;
    }

    /**
     * Creates a card pass move by randomly choosing three cards from the player
     * @param player the player
     * @return a valid card pass move for this player
     */
    public CardPassMove RandomCardPass(IPlayer player) {
        List<Card> cards = new ArrayList<>(_playerHands.get(player));
        return new CardPassMove(cards.remove(RANDOM.nextInt(cards.size())),cards.remove(RANDOM.nextInt(cards.size())),cards.remove(RANDOM.nextInt(cards.size())));
    }

    /**
     * Checks that this move if legal for this player
     * @param move the proposed move
     * @param player the player making the move
     * @return true iff this player can make this move
     */
    protected boolean ValidateMove(Move move, IPlayer player) {
        return GetAllValidMoves(player).contains(move);
    }

    /**
     * Returns a valid random move for the given player
     * @param player the player to get a valid move for
     * @return A valid move for the player
     */
    public Move RandomMove(IPlayer player) {
        List<Move> moves = GetAllValidMoves(player);
        int idx = RANDOM.nextInt(moves.size());
        return moves.get(idx);
    }

    /**
     * Gets all valid moves for the given player
     * @param player the player
     * @return all valid moves for this player
     */
    public List<Move> GetAllValidMoves(IPlayer player) {
        List<Move> valid = new ArrayList<>();
        for (Card c : _playerHands.get(player)) {
            if (IsCardValid(c,player)) valid.add(new Move(c));
        }
        return valid;
    }

    /**
     * Checks if the card is a valid move for player
     * @param c the card to play
     * @param player the player
     * @return true if player can play c
     */
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

    /**
     * Checks if this is the start of a round
     * @return true if no moves have been played yet in this round
     */
    private boolean isStartOfRound() {
        return _currentTrick.IsEmpty() && _playerHands.get(CurrentPlayer()).size() == SIZE_OF_HANDS;
    }

    /**
     * Checks if the player's hand contains only the given suit
     * @param player The player
     * @param s the suit to look for
     * @return true iff this player's hand only contains cards of suit s
     */
    private boolean containsOnlySuit(IPlayer player, Suit s) {
        for (Card c : _playerHands.get(player)) {
            if (c.Suit != s) return false;
        }
        return true;
    }

    /**
     * Checks if this player has any cards of the given suit
     * @param player the player
     * @param s the suit
     * @return true iff this player has no cards of suit s
     */
    private boolean containsNoneOfSuit(IPlayer player, Suit s) {
        for (Card c : _playerHands.get(player)) {
            if (c.Suit == s) return false;
        }
        return true;
    }

    /**
     * Checks if the player has the given card in their hand
     * @param player the player
     * @param c the card
     * @return true iff this player has c in their hand
     */
    private boolean doesPlayerHaveCard(IPlayer player, Card c) {
        return _playerHands.get(player).contains(c);
    }

    public int[] GetRoundScores() {
        int[] scores = new int[_players.length];
        for (int i = 0; i < _players.length; i++) {
            scores[i] = _roundScore.get(_players[i]);
        }
        return scores;
    }

    public int[] GetGameScores() {
        int[] scores = new int[_players.length];
        for (int i = 0; i < _players.length; i++) {
            scores[i] = _scores.get(_players[i]);
        }
        return scores;
    }

    public SealedGameInfo Seal() {
        return new SealedGameInfo(CurrentTrick(),GetRoundScores(), GetGameScores(), IsHeartsBroken(), RoundNumber(), isStartOfRound());
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
