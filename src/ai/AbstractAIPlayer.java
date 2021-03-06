package ai;

import game.*;
import player.*;

import java.util.List;

public abstract class AbstractAIPlayer extends AbstractPlayer implements IPlayer {

    private RuleBasedPlayer _rule;

    public AbstractAIPlayer() {
        super();
        _rule = new RuleBasedPlayer();
    }

    protected RuleBasedPlayer RulePlayer() { return _rule; }

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        CardPassMove move = CardPassSimulator.GetMove(info);
        if (move != null) return move;
        return _rule.PassCards(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        List<Move> valid = GameUtils.GetAllValidMoves(info);
        if (valid.size() == 1) return valid.get(0);
        Move m = GameTree.Simulate(info, ID(),GetHeuristic(info), GetExpansionFunction(info), NumberOfSimulations(info));
        return m;
    }
    /**
     * Creates an expansion function that stops search after the specified depth
     * @param depth the depth to halt search after
     * @return an expansion function limiting depth of the search tree
     */
    public ExpansionFunction DepthLimitedExpansionFunction(final int depth) {
        return new ExpansionFunction() {
            @Override
            public boolean ExpandMove(Move m, SealedGameInfo info) {
                return true;
            }

            @Override
            public boolean DepthLimit(int d) {
                return d == depth;
            }
        };
    }


    public abstract HeuristicFunction GetHeuristic(SealedGameInfo info);
    public abstract ExpansionFunction GetExpansionFunction(SealedGameInfo info);
    public abstract int NumberOfSimulations(SealedGameInfo info);

    public String toString() {
        return super.toString() + " (AI)";
    }
}
