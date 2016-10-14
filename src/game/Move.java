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
}
