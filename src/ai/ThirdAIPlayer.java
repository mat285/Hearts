package ai;

import game.*;
import player.RuleBasedPlayer;

public class ThirdAIPlayer extends AbstractPlayer {

    private RuleBasedPlayer _rule;

    public ThirdAIPlayer() {
        super();
        _rule = new RuleBasedPlayer();
    }

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return _rule.PassCards(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        Simulator s = new Simulator(this.ID());
        return s.GetMove(info);
    }

    @Override
    public String toString() {
        return super.toString() + " (3)";
    }
}
