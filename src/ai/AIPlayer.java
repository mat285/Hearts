package ai;

import game.*;
import player.RandomPlayer;

public class AIPlayer extends AbstractPlayer implements IPlayer {
    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return GameUtils.RandomCardPass(info);
    }

    @Override
    public Move Play(SealedGameInfo info) {
        Move m = MonteCarlo.Simulate(info,getPlayers(),ID(),getHeuristic());
        System.out.println(GameUtils.ValidateMove(m,info));
        System.out.println(m);
        return m;
    }

    private IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i == ID()) players[i] = this;
            else players[i] = new RandomPlayer();
        }
        return players;
    }

    private HeuristicFunction getHeuristic() {
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
}
