package player;

import game.*;
import card.*;
import java.util.*;

public class TerminalPlayer extends AbstractPlayer implements IPlayer {

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        System.out.println("Hand: " + getHand(info));
        System.out.println("Which Cards?");
        Scanner s = new Scanner(System.in);
        Card c1=null, c2=null, c3=null;
        int tries = 3;
        do {
            tries--;
            String[] cards = s.nextLine().split(", ");
            if (cards.length != 3) continue;
            c1 = Cards.Parse(cards[0]);
            c2 = Cards.Parse(cards[1]);
            c3 = Cards.Parse(cards[2]);
        } while ((c1 == null || c2 == null || c3 == null) && tries > 0);
        return new CardPassMove(c1,c2,c3);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        System.out.println("Hand: " + getHand(info));
        System.out.println("Trick: " + info.CurrentTrick().AllCards());
        System.out.println("Which Card?");
        Scanner s = new Scanner(System.in);
        Card c = null;
        int tries = 3;
        do {
            c = Cards.Parse(s.nextLine());
            tries--;
        } while (c == null && tries > 0);
        return new Move(c);
    }

    private List<Card> getHand(SealedGameInfo info) {
        List<Card> hand = new ArrayList<>(info.GetHand());
        Collections.sort(hand);
        return hand;
    }
}
