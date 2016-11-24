package ai;

import game.*;

public class SecondAIPlayer extends AbstractAIPlayer implements IPlayer {

    public HeuristicFunction GetHeuristic(SealedGameInfo info) {
        return new HeuristicFunction() {
            @Override
            public MinimaxVector Evaluate(SealedGameInfo info) {
                MinimaxVector v = new MinimaxVector();
                int[] r = info.RoundScore();
                for (int i = 0; i < v.Scores.length; i++) {
                    v.Scores[i] = 100 - r[i];
                }
                return v;
            }
        };
    }

    @Override
    public int SearchDepth(SealedGameInfo info) {
        return MonteCarlo.DEFAULT_SEARCH_DEPTH+1;
    }

    @Override
    public int NumberOfSimulations(SealedGameInfo info) {
        return MonteCarlo.DEFAULT_NUMBER_SIMULATIONS;
    }

    @Override
    public String toString() {
        return super.toString() + " (2)";
    }
}
