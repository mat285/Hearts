package game;

import ai.AIPlayer;
import player.RandomPlayer;
import player.RuleBasedPlayer;
import player.TerminalPlayer;
import test.Assert;
import java.util.*;
import card.*;

public class GameTest {

    public static void Test() {
        IPlayer[] players = getPlayers();
        Game g = new Game(players);
        Assert.Equal(g.State(), GameState.START_ROUND);
        g.Step();
        Assert.Equal(g.State(), GameState.PASS_CARDS);
        while (g.State() != GameState.GAME_OVER) {
            //System.out.println(g.State());
            g.Step();
            if (g.State() == GameState.END_ROUND) g.Info().PrintDebugInfo();
        }
        System.out.println("Ranking: " + g.Step());
        System.exit(0);
        List<Double> places = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            places.add(0.0);
        }

        int trials = 10;
        for (int i = 0; i < trials; i++) {
            g.NewGame();
            List<ScoredPlayer> rankings = g.RunGame();
            for (int j = 0; j < rankings.size(); j++) {
                if (rankings.get(j).Player() == players[0]) places.set(j, places.get(j) + 1.0);
            }
        }
        for (int i = 0; i < places.size(); i++) {
            places.set(i, places.get(i) / trials);
        }
        System.out.println("Rule based performance: " + places);
    }

    public static void TestHighestOfSuit() {
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Suit.CLUBS, Value.QUEEN));
        hand.add(new Card(Suit.CLUBS, Value.FOUR));
        Assert.Equal(GameUtils.HighestOfSuit(hand, Suit.CLUBS), hand.get(0));
    }

    public static void Run() {
        TestHighestOfSuit();
        Test();
    }

    public static IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i == 0) players[i] = new AIPlayer();
            else if (i == 1) players[i] = new RuleBasedPlayer();
            else players[i] = new RandomPlayer();
        }
        return players;
    }
}
