package main;

import game.Game;
import game.GameState;
import game.IPlayer;
import player.RandomPlayer;
import player.RuleBasedPlayer;
import player.TerminalPlayer;

public class TerminalGame {

    public static void RunSinglePlayerGame() {
        IPlayer[] players = getPlayers();
        Game g = new Game(players);
        g.Step();
        while (g.State() != GameState.GAME_OVER) {
            //System.out.println(g.State());
            g.Step();
            if (g.State() == GameState.END_ROUND) g.Info().PrintDebugInfo();
        }
        System.out.println("Ranking: " + g.Step());
    }

    public static IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i == 0) players[i] = new RuleBasedPlayer();
            if (i == 1) players[i] = new TerminalPlayer();
            else players[i] = new RandomPlayer();
        }
        return players;
    }
}
