package ai;

import game.*;
import card.*;

public interface HeuristicFunction {
    public MinimaxVector Evaluate(SealedGameInfo info);
}
