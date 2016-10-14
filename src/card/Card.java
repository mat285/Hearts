package card;

public final class Card implements Comparable<Card> {

    public static final int QUEEN_OF_SPADES_POINTS = 13;
    public static final int HEARTS_POINTS = 1;
    public static final int OTHER_CARD_POINTS = 0;

    public final Suit Suit;
    public final Value Value;

    public Card(Suit suit, Value value) {
        this.Suit = suit;
        this.Value = value;
    }

    public @Override boolean equals(Object o) {
        return (o instanceof Card) && ((Card) o).Suit == this.Suit && ((Card) o).Value == this.Value;
    }

    public @Override int hashCode() {
        return Suit.hashCode()*Value.hashCode();
    }

    public @Override String toString() {
        return Value.name() + " of " + Suit.name();
    }

    public @Override int compareTo(Card c) {
        if (c.Suit == this.Suit) return this.Value.ordinal() - c.Value.ordinal();
        return this.Suit.ordinal() - c.Suit.ordinal();
    }

    public int PointValue() {
        if (this.Suit == Suit.SPADES && this.Value == Value.QUEEN) return QUEEN_OF_SPADES_POINTS;
        else if (this.Suit == Suit.HEARTS) return HEARTS_POINTS;
        return OTHER_CARD_POINTS;
    }
}
