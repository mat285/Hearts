package game;

import card.*;
import java.util.*;

public class GameUtils {

    private static final Random RANDOM = new Random();

    public static final int SIZE_OF_HANDS = 13;
    public static final int MAX_POINTS_PER_ROUND = 26;
    public static final int MAX_GAME_SCORE = 100;


    /**
     * Checks to see if the hand contains no cards of the given suit
     * @param hand the hand to check
     * @param s the suit to look for
     * @return true iff hand contains no cards of suit s
     */
    public static boolean ContainsNoneOfSuit(Iterable<Card> hand, Suit s) {
        for (Card c : hand) {
            if (c.Suit == s) return false;
        }
        return true;
    }

    /**
     * Checks if the hand contains only cards of the given suit
     * @param hand the hand to check
     * @param s the suit
     * @return true if all cards in hand are of type s
     */
    public static boolean ContainsOnlySuit(Iterable<Card> hand, Suit s) {
        for (Card c : hand) {
            if (c.Suit != s) return false;
        }
        return true;
    }

    /**
     * Gets all cards in the hand of the given suit
     * @param hand the hand
     * @param s the suit
     * @return all cards in hand with suit s
     */
    public static List<Card> CardsOfSuit(Iterable<Card> hand, Suit s) {
        List<Card> cards = new ArrayList<>();
        for (Card c : hand) {
            if (c.Suit == s) cards.add(c);
        }
        return cards;
    }

    /**
     * Gets all the highest card of the given suit, or null if there are no cards of this suit
     * @param hand the hand
     * @param s the suit
     * @return the highest card of suit s or null
     */
    public static Card HighestOfSuit(Iterable<Card> hand, Suit s) {
        Card max = null;
        for (Card c : hand) {
            if (c.Suit == s && (max == null || c.Value.compareTo(max.Value)>0)) max = c;
        }
        return max;
    }

    public static Card LowestOfSuit(Iterable<Card> hand, Suit s) {
        Card min = null;
        for (Card c : hand) {
            if (min == null || (c.Suit == s && c.Value.compareTo(min.Value)<0)) min = c;
        }
        return min;
    }

    /**
     * Checks that this move if legal for this player
     * @param move the proposed move
     * @param info The SealedGameInfo
     * @return true iff this player can make this move
     */
    public static boolean ValidateMove(Move move, SealedGameInfo info) {
        return IsCardValid(move.Card(), info);
    }

    /**
     * Returns a valid random move for the given player
     * @param info The SealedGameInfo
     * @return A valid move for the player
     */
    public static Move RandomMove(SealedGameInfo info) {
        List<Move> moves = GetAllValidMoves(info);
        int idx = RANDOM.nextInt(moves.size());
        return moves.get(idx);
    }

    /**
     * Gets all valid moves for the given player
     * @param info The SealedGameInfo
     * @return all valid moves for this player
     */
    public static List<Move> GetAllValidMoves(SealedGameInfo info) {
        List<Move> valid = new ArrayList<>();
        for (Card c : info.GetHand()) {
            if (IsCardValid(c,info)) valid.add(new Move(c));
        }
        return valid;
    }

    /**
     * Checks if the card is a valid move for player
     * @param c the card to play
     * @param info The SealedGameInfo
     * @return true if player can play c
     */
    public static boolean IsCardValid(Card c, SealedGameInfo info) {
        if (!info.GetHand().contains(c)) return false;
        Suit curr = info.CurrentSuit();
        // If this is the start of a round then only the two of clubs is valid
        if (info.IsStartOfRound()) return c.equals(Cards.TWO_OF_CLUBS);
        if (curr == null) {
            // If this is the first card, then either play a card that isn't hearts or hearts must be broken
            return (info.IsHeartsBroken() || c.Suit != Suit.HEARTS || ContainsOnlySuit(info.GetHand(), c.Suit));
        }
        // Else play on suit, or if player has none then play anything
        return (c.Suit == curr || ContainsNoneOfSuit(info.GetHand(), curr));
    }

    /**
     * Checks if the card pass move is okay for the given player
     * @param move the move
     * @param info The SealedGameInfo
     * @return true iff the player can pass these cards
     */
    public static boolean IsValidCardPass(CardPassMove move, SealedGameInfo info) {
        if (move == null) return false;
        for (Card c : move.Cards()) {
            if (!info.GetHand().contains(c)) return false;
        }
        if (move.Cards().get(0).equals(move.Cards().get(1)) || move.Cards().get(0).equals(move.Cards().get(2)) || move.Cards().get(1).equals(move.Cards().get(2))) return false;
        return true;
    }

    /**
     * Creates a card pass move by randomly choosing three cards from the player
     * @param info The SealedGameInfo
     * @return a valid card pass move for this player
     */
    public static CardPassMove RandomCardPass(SealedGameInfo info) {
        List<Card> cards = new ArrayList<>(info.GetHand());
        return new CardPassMove(cards.remove(RANDOM.nextInt(cards.size())),cards.remove(RANDOM.nextInt(cards.size())),cards.remove(RANDOM.nextInt(cards.size())));
    }
}
