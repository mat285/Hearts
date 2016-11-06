package card;

import java.util.*;

public class Trick {
    private Card _first;
    private Card _second;
    private Card _third;
    private Card _fourth;

    public Trick() {}

    public Trick(Card first) {
        _first = first;
    }

    public boolean Add(Card c) {
        if (c == null) return false;
        if (_first == null) _first = c;
        else if (_second == null) _second = c;
        else if (_third == null) _third = c;
        else if (_fourth == null) _fourth = c;
        else return false;
        return true;
    }

    public Card First() { return _first; }
    public Card Second() { return _second; }
    public Card Third() { return _third; }
    public Card Fourth() { return _fourth; }

    public List<Card> AllCards() {
        List<Card> cards = new ArrayList<>();
        if (_first != null) cards.add(_first);
        if (_second != null) cards.add(_second);
        if (_third != null) cards.add(_third);
        if (_fourth != null) cards.add(_fourth);
        return cards;
    }

    public int Size() {
        return AllCards().size();
    }

    /**
     * Returns true if and only if all four cards have been added to this trick
     * @return true iff all four cards have been added to this trick
     */
    public boolean IsComplete() {
        return _first != null && _second != null && _third != null && _fourth != null;
    }

    /**
     * Returns true if no cards have been added to this trick yet
     * @return true iff no cards are in this trick
     */
    public boolean IsEmpty() { return _first == null && _second == null && _third == null && _fourth == null; }

    /**
     * Selects the highest card in this trick that is of the same suit as the first card
     * @return The highest card in this trick
     */
    public Card Highest() {
        if (_first == null) return null;
        List<Card> all = AllCards();
        Card max = _first;
        Suit s = _first.Suit;
        for (Card c : all) {
            if (c.Suit == s && c.Value.ordinal() > max.Value.ordinal()) max = c;
        }
        return max;
    }

    /**
     * Returns the number of points this trick is worth
     * @return the number of points this trick is worth
     */
    public int Points() {
        List<Card> all = AllCards();
        int sum = 0;
        for (Card c : all) {
            sum += c.PointValue();
        }
        return sum;
    }

    public Card LastCardAdded() {
        if (_first == null) return null;
        if (_second == null) return _first;
        if (_third == null) return _second;
        if (_fourth == null) return _third;
        return _fourth;
    }

    public Suit Suit() {
        return _first != null ? _first.Suit : null;
    }

    public String toString() {
        return "[" + _first + ", " + _second + ", " + _third + ", " + _fourth + "]";
    }

    public Trick Clone() {
        Trick t = new Trick();
        for (Card c : AllCards()) {
            t.Add(c);
        }
        return t;
    }
}
