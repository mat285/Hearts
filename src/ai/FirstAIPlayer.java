package ai;

import game.*;
import player.*;

public class FirstAIPlayer extends AbstractAIPlayer implements IPlayer {

    private int _searchDepth;
    private int _numSimulations;

    public FirstAIPlayer(int searchDepth, int numSimulations) {
        _searchDepth = searchDepth;
        _numSimulations = numSimulations;
    }

    public FirstAIPlayer() {
        this(MonteCarlo.DEFAULT_SEARCH_DEPTH, MonteCarlo.DEFAULT_NUMBER_SIMULATIONS);
    }

    @Override
    public HeuristicFunction GetHeuristic(SealedGameInfo info) {
        return new HeuristicFunction() {
            @Override
            public MinimaxVector Evaluate(SealedGameInfo info) {
                MinimaxVector v = new MinimaxVector();
                int[] r = info.RoundScore();
                for (int i = 0; i < v.Scores.length; i++) {
                    v.Scores[i] = - r[i];
                }
                return v;
            }
        };
    }

    @Override
    public ExpansionFunction GetExpansionFunction(SealedGameInfo info) {
        return DepthLimitedExpansionFunction(_searchDepth);
    }

    @Override
    public int NumberOfSimulations(SealedGameInfo info) {
        return _numSimulations;
    }

    @Override
    public String toString() {
        return super.toString() + " (1)";
    }
}
