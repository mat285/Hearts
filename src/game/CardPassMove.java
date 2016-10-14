package game;

import card.*;
import java.util.*;

public class CardPassMove {

    private List<Card> _cards;

    public CardPassMove(Card c1, Card c2, Card c3) {
        _cards = new ArrayList<>();
        _cards.add(c1);
        _cards.add(c2);
        _cards.add(c3);
    }

    public List<Card> Cards() {
        return _cards;
    }

}
