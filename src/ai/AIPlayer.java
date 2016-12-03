package ai;

import game.*;
import player.*;

public class AIPlayer extends AbstractPlayer implements IPlayer {

    private RuleBasedPlayer _rule;

    public AIPlayer() {
        super();
        _rule = new RuleBasedPlayer();
    }

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return _rule.PassCards(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        Move m = MonteCarlo.Simulate(info, ID(),getHeuristic());
        //Following runs a pure monte carlo simulation
/*        Simulator sim = new Simulator(ID());
        Move m = sim.GetMove(info);*/
        return m;
    }

    private HeuristicFunction getHeuristic() {
        return new HeuristicFunction() {
            @Override
            public MinimaxVector Evaluate(SealedGameInfo info) {
                MinimaxVector v = new MinimaxVector();
                int[] r = info.RoundScore();
                for (int i = 0; i < v.Scores.length; i++) {
                    v.Scores[i] = -r[i];
                }
                return v;
            }
        };
    }

    public String toString() {
        return super.toString() + " (AI)";
    }
}
