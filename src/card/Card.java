package card;

public class Card implements Comparable<Card> {
    public Suit Suit;
    public Value Value;

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
}
