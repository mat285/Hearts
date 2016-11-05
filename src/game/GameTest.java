package game;

import player.RandomPlayer;
import player.RuleBasedPlayer;
import test.Assert;

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
    }


    public static void Run() {
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
