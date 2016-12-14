package game;


import java.util.*;

import ai.AbstractAIPlayer;
import card.*;

public abstract class AbstractPlayer implements IPlayer {
    private List<Card> _hand;
    private int _id;
    private String _name;

    private static final String[] _names = {"Michael", "Elaine", "Chase", "Alex"};

    public AbstractPlayer(){_hand = new ArrayList<>();}

    public AbstractPlayer(String name) {
        _name = name;
        _hand = new ArrayList<>();
    }

    public @Override void Initialize(int id) {
        _hand = new ArrayList<>();
        _id = id;
        _name = _names[_id];
    }

    public int ID() {
        return _id;
    }

    public @Override void StartRound(Card[] hand) {
        _hand = new ArrayList<>();
        for (Card c : hand) {
            _hand.add(c);
        }
    }

    public @Override void ReceiveCards(CardPassMove give, CardPassMove get) {
        for (Card c : give.Cards()) {
            RemoveCardFromHand(c);
        }
        for (Card c : get.Cards()) {
            _hand.add(c);
        }
        Deck.Sort(_hand);
    }

    protected void RemoveCardFromHand(Card c) {
        _hand.remove(c);
    }

    public String toString() {
        return _name;
    }
}
