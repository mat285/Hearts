package ai;

import game.*;
import card.*;

import java.util.List;

public class MonteCarlo {

    public static double Simulate(SealedGameInfo info, IPlayer[] players, int currentPlayer, HeuristicFunction fn) {
        GameInfo g = GameInfo.DistributeRandomly(info,players,currentPlayer);
        return 0;
    }

    private static double simulateMove(GameInfo info, Move m, HeuristicFunction fn) {
        info.ExecuteMove(m,info.CurrentPlayer());
        if (info.IsRoundOver()); // return the evaluation here i guess
        GameState state = GameState.PLAYER_TURN;
        if (info.IsTrickDone()) state = GameState.END_TRICK;
        Game g = Game.StartFromState(info, state);
        List<ScoredPlayer> ranking = g.RunGame();
        return 0;
    }
}
