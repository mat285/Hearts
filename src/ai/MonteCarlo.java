package ai;

import game.*;

import java.util.*;

public class MonteCarlo {

    public static Move Simulate(SealedGameInfo info, IPlayer[] players, int currentPlayer, HeuristicFunction fn) {
        return Simulate(info,players,currentPlayer,fn,20,8);
    }

    public static Move Simulate(SealedGameInfo info, IPlayer[] players, int currentPlayer, HeuristicFunction fn, int times, int depth) {
        if (times < 1 || depth < 1) return null;
        Map<Move,Integer> map = new HashMap<>();
        Move max = null;
        for (int i = 0; i < times; i++) {
            GameInfo g = GameInfo.DistributeRandomly(info, players, currentPlayer);
            GameTree tree = new GameTree(new GameTreeInfo(g), depth);
            Move m = tree.BestMove(fn);
            if (!map.containsKey(m)) map.put(m,0);
            map.put(m,map.get(m)+1);
            if (max == null || map.get(max) < map.get(m)) max = m;
        }
        System.out.println(map.get(max));
        return max;
    }

}
