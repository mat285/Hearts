package game;

import card.Card;

public class Move {
    private Card _card;

    public Move(Card card) {
        this._card = card;
    }

    public Card Card() {
        return _card;
    }

    public boolean equals(Object o) {
        return (o instanceof  Move) && ((Move) o).Card().equals(Card());
    }

    public int hashCode() {
        return Card().hashCode();
    }

    public String toString() { return "Move: " + _card.toString(); }
}
