package ai;

import game.*;

public interface ExpansionFunction {
    public boolean ExpandMove(Move m, SealedGameInfo info);
    public boolean DepthLimit(int depth);
}
