package ai;

import game.*;
import java.util.*;

public class GameTree {

    private List<GameTree> _children;
    private GameTreeInfo _info;
    private Move _move;
    private MinimaxVector _values;


    public GameTree(GameTreeInfo info, Move move) {
        _info = info;
        _move = move;
    }

    public void Expand() {
        List<Move> moves = GameUtils.GetAllValidMoves(_info.Info());
        _children = new ArrayList<>();
        for (Move m : moves) {
            GameTree child = new GameTree(_info.PlayMove(m),m);
            _children.add(child);
        }
    }

    public void Minimax(HeuristicFunction fn, int depth) {
        if (depth == 0) {
            _values = fn.Evaluate(_info.Info());
            return;
        }
        Expand();
        if (_children == null || _children.size() < 1) {
            _values = fn.Evaluate(_info.Info());
            return;
        }
        for (GameTree child : _children) {
            child.Minimax(fn, depth-1);
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

    private Move bestMove() {
        if (_children == null) return null;
        Collections.shuffle(_children);
        for (GameTree child : _children) {
            if (child._values.equals(this._values)) return child._move;
        }
        return null;
    }

    public Move BestMove(HeuristicFunction fn, int depth) {
        Minimax(fn, depth);
        return bestMove();
    }
}
