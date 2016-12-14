package ai;


import game.*;
import card.*;
import player.*;

import java.util.*;

public class CardPassSimulator {

    public static final int NUM_TRIALS = 20;
    public static final int NUM_ATTEMPTS = 50;

    /**
     * Runs many trials to find a good card pass move
     * @param info the current game info
     * @return a good card pass move
     */
    public static CardPassMove GetMove(SealedGameInfo info) {
        List<Card> hand = new ArrayList<>(info.GetHand());
        long curr = System.currentTimeMillis();
        int max = 0;
        CardPassMove best = null;
        for (int i = 0; i < NUM_ATTEMPTS; i++) {
            CardPassMove move = GameUtils.RandomCardPass(info);
            int score = 0;
            for (int n = 0; n < NUM_TRIALS; n++) {
                IPlayer[] players = getPlayers(info.CurrentPlayer(), move);
                GameInfo gi = GameInfo.DistributeRandomly(info,players, info.CurrentPlayer());
                Game g = Game.StartFromState(gi, GameState.START_ROUND);
                List<ScoredPlayer> ranks = g.RunGame();
                for (int w = 0; w < ranks.size(); w++) {
                    if (ranks.get(w).Player().equals(players[info.CurrentPlayer()])) score += 3 - w;
                }
            }
            if (score > max || best == null) {
                max = score;
                best = move;
            }
        }
        return best;
    }

    private static IPlayer[] getPlayers(int currentPlayer, final CardPassMove move) {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i == currentPlayer) players[i] = new AbstractPlayer() {
                @Override
                public CardPassMove PassCards(SealedGameInfo info) {
                    return move;
                }

                @Override
                public Move Play(SealedGameInfo state) {
                    return GameUtils.RandomMove(state);
                }
            };
            players[i] = new RandomPlayer();
        }
        return players;
    }
}
