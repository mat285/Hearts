package ai;

import game.*;
import card.*;

import java.util.List;

public class MonteCarlo {

    public static Move Simulate(SealedGameInfo info, IPlayer[] players, int currentPlayer, HeuristicFunction fn) {
        GameInfo g = GameInfo.DistributeRandomly(info,players,currentPlayer);
        List<Move> valid = GameUtils.GetAllValidMoves(info);
        double min = 100;
        Move minCard = null;
        for (Move m : valid) {
            double score = simulateMove(g, m, fn);
            if (score < min) {
                minCard = m;
                min = score;
            }
        }
        return minCard;
    }

    private static double simulateMove(GameInfo info, Move m, HeuristicFunction fn) {
        IPlayer current = info.CurrentPlayer();
        info.ExecuteMove(m,info.CurrentPlayer());
        if (info.IsRoundOver()); // return the evaluation here i guess
        GameState state = GameState.PLAYER_TURN;
        if (info.IsTrickDone()) state = GameState.END_TRICK;
        Game g = Game.StartFromState(info, state);
        List<ScoredPlayer> ranking = g.RunGame();
        for (ScoredPlayer p : ranking) {
            if (p.Player() == current) return p.Score();
        }
        return 100;
    }
}
