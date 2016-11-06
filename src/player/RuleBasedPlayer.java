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
        if (info.GetHand().size() == GameUtils.SIZE_OF_HANDS) return playFirstMove(info);
        if (info.CurrentTrick().IsEmpty()) return playFirstInTrickMove(info);
        if (GameUtils.ContainsNoneOfSuit(info.GetHand(), info.CurrentSuit())) return playOffSuit(info);
        if (info.CurrentTrick().Points() == 0 && info.CurrentTrick().Size() == 3) return takeTrick(info);
        return avoidTrick(info);
    }

    private Move playFirstMove(SealedGameInfo info) {
        if (info.GetHand().contains(GameUtils.TWO_OF_CLUBS)) return new Move(GameUtils.TWO_OF_CLUBS);
        Card highClub = GameUtils.HighestOfSuit(info.GetHand(), Suit.CLUBS);
        if (highClub != null) return new Move(highClub);
        return playOffSuit(info);
    }

    private Move playOffSuit(SealedGameInfo info) {
        if (info.GetHand().contains(QUEEN_OF_SPADES)) return new Move(QUEEN_OF_SPADES);
        Suit[] preferable = {Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS, Suit.CLUBS};
        for (Suit s : preferable) {
            Card h = GameUtils.HighestOfSuit(info.GetHand(), s);
            if (h != null) {
                return new Move(h);
            }
        }
        return null;
    }

    private Move takeTrick(SealedGameInfo info) {
        Card high = GameUtils.HighestOfSuit(info.GetHand(), info.CurrentSuit());
        if (high != null) return new Move(high);
        return playOffSuit(info);
    }

    private Move playFirstInTrickMove(SealedGameInfo info) {
        List<Move> possible = GameUtils.GetAllValidMoves(info);
        Move min = null;
        for (Move m : possible) {
            if (min == null || m.Card().PointValue() < min.Card().PointValue() || (min.Card().PointValue() == m.Card().PointValue() && m.Card().Value.compareTo(min.Card().Value) > 0)) min = m;
        }
        return min;
    }

    private Move avoidTrick(SealedGameInfo info) {
        Suit s = info.CurrentSuit();
        Trick t = info.CurrentTrick();
        Card high = t.Highest();
        List<Card> possible = new ArrayList<>();
        for (Card c : info.GetHand()) {
            if (c.Suit == s && c.Value.compareTo(high.Value) < 0) possible.add(c);
        }
        Card move = GameUtils.HighestOfSuit(possible, s);
        if (move != null) return new Move(move);
        if (t.AllCards().size() < 3) return new Move(GameUtils.LowestOfSuit(info.GetHand(), s));
        return new Move(GameUtils.HighestOfSuit(info.GetHand(), s));
    }

    @Override
    public String toString() {
        return super.toString() + " (Rule)";
    }
}
