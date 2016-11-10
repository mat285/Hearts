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
        return MonteCarlo.Simulate(info,getPlayers(),ID(),null);
    }

    private IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            if (i == ID()) players[i] = this;
            else players[i] = new RandomPlayer();
        }
        return players;
    }
}
