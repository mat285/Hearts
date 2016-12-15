package gui;

import game.*;
import player.RuleBasedPlayer;

public class GUIPlayer extends AbstractPlayer {

    private Pipe _pipe;

    public GUIPlayer() {
        super();
    }

    public void SetPipe(Pipe pipe) {
        _pipe = pipe;
    }

    @Override
    public CardPassMove PassCards(SealedGameInfo info) {
        return new RuleBasedPlayer().PassCards(info);
    }

    @Override
    public Move Play(SealedGameInfo state) {
        _pipe.Flush();
        _pipe.Enable();
        Move m = new Move(_pipe.Get());
        _pipe.Disable();
        return m;
    }
}
