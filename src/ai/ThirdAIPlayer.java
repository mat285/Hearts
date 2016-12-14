package ai;

import game.*;
import player.RuleBasedPlayer;

public class ThirdAIPlayer extends AbstractPlayer {
    private double _c;
    private int _numSims;

    private RuleBasedPlayer _rule;

    public ThirdAIPlayer() {
        this(MonteCarloSimulator.DEFAULT_C, MonteCarloSimulator.DEFAULT_NUM_SIMS);
    }

    public ThirdAIPlayer(double c, int numSims) {
        super();
        _rule = new RuleBasedPlayer();
        _c = c;
        _numSims = numSims;
    }


    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return _rule.PassCards(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        MonteCarloSimulator s = new MonteCarloSimulator(this.ID(), _numSims, _c);
        return s.GetMove(info);
    }

    @Override
    public String toString() {
        return super.toString() + " (AI) (3)";
    }
}
