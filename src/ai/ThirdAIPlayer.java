package ai;

import game.*;
import player.RuleBasedPlayer;

public class ThirdAIPlayer extends AbstractPlayer {
    private double _c;
    private int _numSims;
    private MonteCarloSimulator _s;

    private RuleBasedPlayer _rule;

    public ThirdAIPlayer() {
        this(MonteCarloSimulator.DEFAULT_C, MonteCarloSimulator.DEFAULT_NUM_SIMS);
    }

    public ThirdAIPlayer(double c, int numSims) {
        super();
        _rule = new RuleBasedPlayer();
        _c = c;
        _numSims = numSims;
        _s = new MonteCarloSimulator(this.ID(), _numSims, _c);
    }

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return _rule.PassCards(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        return _s.GetMove(info);
    }

    @Override
    public String toString() {
        return "Third AI";
    }
}
