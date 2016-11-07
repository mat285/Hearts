package ai;

import game.*;
import card.*;

public interface HeuristicFunction {
    public double Evaluate(Card card, SealedGameInfo info);
}
