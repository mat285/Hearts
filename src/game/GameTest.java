package game;

import ai.*;
import player.*;
import java.util.*;
import card.*;

public class GameTest {

    public static void Test() {
        long startTime = System.currentTimeMillis();
        IPlayer[] players = getPlayers();
        Game g = new Game(players);
        g.Step();
        while (g.State() != GameState.GAME_OVER) {
            //System.out.println(g.State());
            g.Step();
            if (g.State() == GameState.END_ROUND) g.Info().PrintDebugInfo();
        }
        System.out.println("Ranking: " + g.Step());
        System.out.println("Total execution time: " + (System.currentTimeMillis() - startTime)/1000.0 + "s");
        //System.exit(0);
        List<Double> places = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            places.add(0.0);
        }

        int trials = 10;

        for (int i = 0; i < trials; i++) {
            g.NewGame();
            List<ScoredPlayer> rankings = g.RunGame();
            for (int j = 0; j < players.length; j++) {
                if (rankings.get(0).Player() == players[j]) places.set(j, places.get(j) + 1.0);
            }
        }
        for (int i = 0; i < places.size(); i++) {
            places.set(i, places.get(i) / trials);
        }
        System.out.println("Performance: (AI 1), (AI 2), (Rule), (Rand)\n" + places);
    }

    public static void TestHighestOfSuit() {
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Suit.CLUBS, Value.QUEEN));
        hand.add(new Card(Suit.CLUBS, Value.FOUR));
    }

    public static void Run() {
        TestHighestOfSuit();
        Test();
    }

    public static IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i == 0) players[i] = new AbstractPlayer() {
                private RuleBasedPlayer r = new RuleBasedPlayer();
                @Override
                public CardPassMove PassCards(SealedGameInfo info) {
                    return CardPassSimulator.GetMove(info);
                }

                @Override
                public Move Play(SealedGameInfo state) {
                    return GameUtils.RandomMove(state);
                }
            };
            else players[i] = new RandomPlayer();
        }
        return players;
    }
}
