package ai;

import game.*;
import player.RuleBasedPlayer;

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
    public ExpansionFunction GetExpansionFunction(SealedGameInfo info) {
        return new ExpansionFunction() {
            @Override
            public boolean ExpandMove(Move m, SealedGameInfo info) {
                RuleBasedPlayer r = RulePlayer();
                SealedGameInfo s = info.Clone();
                for (int i = 0; i < 2; i++) {
                    Move b = r.Play(s);
                    if (b == null) return true;
                    if (b.equals(m)) return true;
                    s.GetHand().remove(b.Card());
                }
                return false;
            }

            @Override
            public boolean DepthLimit(int depth) {
                return depth > 12;
            }
        };
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
