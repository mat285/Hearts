package card;

import java.util.*;

public class Deck {
    private List<Card> _cards;

    /**
     * Initializes a new 52 card deck of all standard cards
     */
    public Deck() {
        _cards = new ArrayList<>();
        for (int s = 0; s < Suit.values().length; s++) {
            for (int v = 0; v < Value.values().length; v++) {
                _cards.add(new Card(Suit.values()[s], Value.values()[v]));
            }
        }
    }

    /**
     * Shuffles the cards in this deck
     */
    public void Shuffle() {
        Collections.shuffle(_cards);
    }

    /**
     * Deals the cards in this deck into standard hands for hearts
     * @return the hands of each of the four players
     */
    public Card[][] Deal() {
        Card[][] hands = new Card[4][_cards.size()/4];
        int k = 0;
        for (int i = 0; i < _cards.size()/4; i++) {
            for (int j = 0; j < hands.length; j++) {
                hands[j][i] = _cards.get(k++);
            }
        }
        return hands;
    }

    public static void Sort(Card[] cards) {
        Arrays.sort(cards);
    }

    public static void Sort(List<Card> cards) {
        Collections.sort(cards);
    }
}
