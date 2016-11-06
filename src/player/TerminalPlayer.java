package player;

import game.*;
import card.*;
import java.util.*;

public class TerminalPlayer extends AbstractPlayer implements IPlayer {

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return GameUtils.RandomCardPass(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        List<Card> hand = new ArrayList<>(info.GetHand());
        Collections.sort(hand);
        System.out.println("Hand: " + hand);
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
}
