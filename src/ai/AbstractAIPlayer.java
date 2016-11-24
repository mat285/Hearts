package ai;

import game.*;
import player.*;

public abstract class AbstractAIPlayer extends AbstractPlayer implements IPlayer {

    private RuleBasedPlayer _rule;

    public AbstractAIPlayer() {
        super();
        _rule = new RuleBasedPlayer();
    }

    protected RuleBasedPlayer RulePlayer() { return _rule; }

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return _rule.PassCards(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        Move m = MonteCarlo.Simulate(info, ID(),GetHeuristic(info), NumberOfSimulations(info), SearchDepth(info));
        return m;
    }

    public abstract HeuristicFunction GetHeuristic(SealedGameInfo info);
    public abstract int SearchDepth(SealedGameInfo info);
    public abstract int NumberOfSimulations(SealedGameInfo info);

    public String toString() {
        return super.toString() + " (AI)";
    }
}
