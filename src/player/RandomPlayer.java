package player;

import game.*;

public class RandomPlayer extends AbstractPlayer implements IPlayer{

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
       return GameUtils.RandomCardPass(info);
    }

    @Override
    public Move Play(SealedGameInfo state) {
        return GameUtils.RandomMove(state);
    }

    @Override
    public String toString() {
        return "Random";
    }
}
