package game;

import card.*;
import java.util.*;

public class CardPassMove {

    private List<Card> _cards;

    /**
     * Creates a new card pass move with the given cards
     * @param c1 the first card
     * @param c2 the second card
     * @param c3 the third card
     */
    public CardPassMove(Card c1, Card c2, Card c3) {
        _cards = new ArrayList<>();
        _cards.add(c1);
        _cards.add(c2);
        _cards.add(c3);
    }

    /**
     * Gets all cards in this move
     * @return the cards in this move
     */
    public List<Card> Cards() {
        return _cards;
    }

    public String toString() {
        return Cards().toString();
    }
}
