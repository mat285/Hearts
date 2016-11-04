package game;

import card.*;
import java.util.*;

public class GameUtils {

    public static boolean ContainsNoneOfSuit(Iterable<Card> hand, Suit s) {
        for (Card c : hand) {
            if (c.Suit == s) return false;
        }
        return true;
    }

    public static boolean ContainsOnlySuit(Iterable<Card> hand, Suit s) {
        for (Card c : hand) {
            if (c.Suit != s) return false;
        }
        return true;
    }
}
