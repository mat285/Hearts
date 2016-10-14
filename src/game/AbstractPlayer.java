package game;


import java.util.*;
import card.*;

public abstract class AbstractPlayer implements IPlayer {
    private List<Card> _hand;
    private int _id;

    public AbstractPlayer() {
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

}
