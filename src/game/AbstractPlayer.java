package game;


import java.util.*;
import card.*;

public abstract class AbstractPlayer implements IPlayer {
    private List<Card> _hand;
    private int _id;
    private String _name;

    private static final String[] _names = {"Michael", "Elaine", "Chase", "Alex"};
    private static int _index = 0;

    public AbstractPlayer() {
        this(_names[_index % _names.length]);
        _index++;
        _index = _index % _names.length;
    }

    public AbstractPlayer(String name) {
        _name = name;
    }

    public @Override void Initialize(int id) {
        _id = id;
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
