package ai;

import game.*;

public class GameTreeInfo {
    private GameInfo _info;

    public GameTreeInfo(GameInfo info) {
        _info = info;
    }

    public SealedGameInfo Info() {
        return _info.Seal(_info.CurrentPlayer());
    }

    /**
     * Simulates playing the move by creating a new game tree info
     * @param m the move to play
     * @return a new game tree info with m having been played
     */
    public GameTreeInfo PlayMove(Move m) {
        GameInfo g = _info.Clone();
        g.ExecuteMove(m,_info.CurrentPlayer());
        g.NextPlayer();
        if (g.IsTrickDone()) g.NextTrick();
        return new GameTreeInfo(g);
    }

    public int CurrentPlayer() {
        return _info.CurrentPlayerNumber();
    }
}
