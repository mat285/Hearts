package game;

import player.RandomPlayer;
import player.RuleBasedPlayer;
import test.Assert;
import java.util.*;
import card.*;

public class GameTest {

    public static void Test() {
        Game g = new Game(getPlayers());
        Assert.Equal(g.State(), GameState.START_ROUND);
        g.Step();
        Assert.Equal(g.State(), GameState.PASS_CARDS);
        while (g.State() != GameState.GAME_OVER) {
            System.out.println(g.State());
            g.Step();
            g.Info().PrintDebugInfo();
        }
        System.out.println("Ranking: " + g.Step());
        List<Integer> places = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            places.add(0);
        }

        for (int i = 0; i < 1000; i++) {
            g.NewGame();
            List<ScoredPlayer> rankings = g.RunGame();
            for (int j = 0; j < rankings.size(); j++) {
                if (rankings.get(j).Player() instanceof RuleBasedPlayer) places.set(j, places.get(j) + 1);
            }
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
        final String[] names = new String[]{"Elaine", "Michael", "Chase", "Alex"};
        for (int i = 0; i < players.length; i++) {
            if (i == 1) players[i] = new RuleBasedPlayer();
            else players[i] = new RandomPlayer();
        }
        return players;
    }
}
