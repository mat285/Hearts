package ai;

import card.*;
import game.*;
import java.util.*;

public class GameTree {

    private List<GameTree> _children;
    private GameTreeInfo _info;
    private Move _move;
    private MinimaxVector _values;

    public GameTree(GameTreeInfo info, int depth) {
        _info = info;
        if (depth < 1) return;
        List<Move> moves = GameUtils.GetAllValidMoves(_info.Info());
        if (moves.size() == 1) depth = 0;
        _children = new ArrayList<>();
        for (Move m : moves) {
            GameTree child = new GameTree(info.PlayMove(m), depth-1,m);
            _children.add(child);
        }
    }

    public GameTree(GameTreeInfo info, int depth, Move move) {
        this(info,depth);
        _move = move;
    }

    public void Minimax(HeuristicFunction fn) {
        if (_children == null || _children.size() < 1) {
            _values = fn.Evaluate(_info.Info());
            return;
        }
        for (GameTree child : _children) {
            child.Minimax(fn);
        }
        GameTree max = _children.get(0);
        for (int i = 1; i < _children.size(); i++) {
            int curr = _info.CurrentPlayer();
            if (_children.get(i)._values.Scores[curr] > max._values.Scores[curr]) {
                max = _children.get(i);
            }
        }
        _values = max._values;
    }

    public Move BestMove() {
        if (_children == null) return null;
        for (GameTree child : _children) {
            if (child._values.equals(this._values)) return child._move;
        }
        return null;
    }

    public Move BestMove(HeuristicFunction fn) {
        Minimax(fn);
        return BestMove();
    }
}
