package game;

import card.*;
import java.util.*;

public final class GameInfo {
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
        Move move = CurrentPlayer().Play(Seal(CurrentPlayer()));
        if (!GameUtils.ValidateMove(move, Seal(CurrentPlayer()))) move = GameUtils.RandomMove(Seal(CurrentPlayer()));
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
            if (entry.getValue() == GameUtils.MAX_POINTS_PER_ROUND) moonShooter = entry.getKey();
        }
        if (moonShooter != null) {
            for (IPlayer player : _players) {
                if (player != moonShooter) _scores.put(player, _scores.get(player) + GameUtils.MAX_POINTS_PER_ROUND);
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
    public boolean IsGameOver() {
        for (Integer score : _scores.values()) {
            if (score >= GameUtils.MAX_GAME_SCORE) return true;
        }
        return false;
    }

    /**
     * Checks if the round is over by seeing if all players are out of cards
     * @return true iff the current round is over
     */
    public boolean IsRoundOver() {
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
    public boolean IsTrickDone() {
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
    public IPlayer CurrentPlayer() {
        return _players[_currentPlayer];
    }

    /**
     * Gets the player that started the current trick
     * @return The player starting the current trick
     */
    public int PlayerStartingTrick() {
        IPlayer player =  _currentTrick.IsEmpty() ? CurrentPlayer() : _plays.get(_currentTrick.First());
        for(int i = 0; i < _players.length; i++){
            if(_players[i] == player) return i;
        }
        return -1;
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
            if (_playerHands.get(_players[i]).contains(Cards.TWO_OF_CLUBS)) {
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
    public CardPassMove[] ValidateCardPassMoves(CardPassMove[] moves) {
        for (int i = 0; i < moves.length; i++) {
            if (!GameUtils.IsValidCardPass(moves[i], Seal(_players[i]))){
                moves[i] = GameUtils.RandomCardPass(Seal(_players[i]));
            }

        }
        return moves;
    }

    /**
     * Checks if this is the start of a round
     * @return true if no moves have been played yet in this round
     */
    private boolean isStartOfRound() {
        return _currentTrick.IsEmpty() && _playerHands.get(CurrentPlayer()).size() == GameUtils.SIZE_OF_HANDS;
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

    public SealedGameInfo Seal(IPlayer player) {
        return new SealedGameInfo(CurrentTrick(),GetRoundScores(), GetGameScores(), IsHeartsBroken(), RoundNumber(), isStartOfRound(), new HashSet<>(_playerHands.get(player)), RemainingCards());
    }

    public Set<Card> RemainingCards() {
        Set<Card> remaining = new HashSet<>();
        for (IPlayer p : _players) {
            remaining.addAll(_playerHands.get(p));
        }
        return remaining;
    }

    public void SanityCheck() {
        Set<Card> hand0 = _playerHands.get(_players[0]);
        Set<Card> hand1 = _playerHands.get(_players[1]);
        Set<Card> hand2 = _playerHands.get(_players[2]);
        Set<Card> hand3 = _playerHands.get(_players[3]);
        if (hand0.size() != hand1.size() || hand1.size() != hand2.size() || hand2.size() != hand3.size()) throw new RuntimeException();
    }

    public void PrintCardPass(CardPassMove[] moves) {
        for (int i = 0; i < moves.length; i++) {
            System.out.println("Player "+ i+ ": " + moves[i]);
        }
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
            System.out.println("Player " + i + ": " +cards + " Size " + cards.size());
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
