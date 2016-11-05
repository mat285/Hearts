package player;

import game.*;
import card.*;

import java.util.*;

public class RuleBasedPlayer extends AbstractPlayer implements IPlayer {

    private static final Card QUEEN_OF_SPADES = new Card(Suit.SPADES, Value.QUEEN);
    private static final Card KING_OF_SPADES = new Card(Suit.SPADES, Value.KING);
    private static final Card ACE_OF_SPADES = new Card(Suit.SPADES, Value.ACE);

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        CardPassMove move = getRidOfQueen(info);
        if (move != null) return move;
        move= shortSuit(info);
        if (move != null) return move;
        return GameUtils.RandomCardPass(info);
    }

    private CardPassMove shortSuit(SealedGameInfo info) {
        Suit[] preferable = {Suit.HEARTS, Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS};
        for (Suit s : preferable) {
            List<Card> num = GameUtils.CardsOfSuit(info.GetHand(),s);
            if (num.size() <= 3) return moveFromList(num, info);
        }
        return null;
    }

    private CardPassMove getRidOfQueen(SealedGameInfo info) {
        Card c1 = null, c2 = null, c3 = null;
        if (info.GetHand().contains(QUEEN_OF_SPADES)) c1 = QUEEN_OF_SPADES;
        if (info.GetHand().contains(KING_OF_SPADES)) c2 = KING_OF_SPADES;
        if (info.GetHand().contains(ACE_OF_SPADES)) c3 = ACE_OF_SPADES;
        if (c1 == null) c1 = highestUndesirable(info);
        if (c2 == null) c2 = highestUndesirable(info);
        if (c3 == null) c3 = highestUndesirable(info);
        return new CardPassMove(c1,c2,c3);
    }

    private Card highestUndesirable(SealedGameInfo info) {
        Suit[] preferable = {Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES};
        for (Suit s : preferable) {
            Card c = GameUtils.HighestOfSuit(info.GetHand(), s);
            if (c != null) return c;
        }
        return null;
    }

    private CardPassMove moveFromList(List<Card> cards, SealedGameInfo info) {
        CardPassMove move = GameUtils.RandomCardPass(info);
        Card c1, c2, c3;
        if (cards.size() > 0) c1 = cards.get(0);
        else c1 = move.Cards().get(0);
        if (cards.size() > 1) c2 = cards.get(1);
        else c2 = move.Cards().get(1);
        if (cards.size() >2 ) c3 = cards.get(2);
        else c3 = move.Cards().get(2);
        return new CardPassMove(c1,c2,c3);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        List<Move> possible = GameUtils.GetAllValidMoves(info);
        Move min = possible.get(0);
        for (int i = 1; i < possible.size(); i++) {
            if (min.Card().Value.compareTo(possible.get(i).Card().Value) > 0) {
                min = possible.get(i);
            }
        }
        return min;
    }

    @Override
    public String toString() {
        return super.toString() + " (Rule)";
    }
}
