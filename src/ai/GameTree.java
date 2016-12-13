package ai;

import game.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class GameTree {

    private List<GameTree> _children;
    private GameTreeInfo _info;
    private Move _move;
    private MinimaxVector _values;

    private static int _threadCount = 0;
    private static Semaphore _lock = new Semaphore(1);
    private static final int MAX_NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public GameTree(GameTreeInfo info, Move move) {
        _info = info;
        _move = move;
    }

    public void Expand(ExpansionFunction exp) {
        List<Move> moves = GameUtils.GetAllValidMoves(_info.Info());
        _children = new ArrayList<>();
        for (Move m : moves) {
            if (!exp.ExpandMove(m,_info.Info())) continue;
            GameTree child = new GameTree(_info.PlayMove(m),m);
            _children.add(child);
        }
    }

    public void Minimax(HeuristicFunction fn, ExpansionFunction exp, int depth) {
        if (exp.DepthLimit(depth)) {
            _values = fn.Evaluate(_info.Info());
            return;
        }
        Expand(exp);
        if (_children == null || _children.size() < 1) {
            _values = fn.Evaluate(_info.Info());
            return;
        }
        for (GameTree child : _children) {
            child.Minimax(fn, exp, depth+1);
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

    public Move BestMove(HeuristicFunction fn, ExpansionFunction exp) {
        GameTreeExplorer e = new GameTreeExplorer(this, fn, exp, 0);//Minimax(fn, exp, 0);
        try {
            //if (1==1) throw new Exception();
            e.start();
            e.join();
        } catch (Exception i) {
            Minimax(fn, exp, 0);
        }
        resetThreadCount();
        return bestMove();
    }

    private static boolean shouldFork() {
        try {
            _lock.acquire();
            boolean fork = _threadCount < MAX_NUM_THREADS;
            if (fork) _threadCount++;
            _lock.release();
            return fork;
        } catch (Exception e) {
            return false;
        }
    }

    private static void releaseFork() {
        try {
            _lock.acquire();
            _threadCount--;
            _lock.release();
        } catch (Exception e) {

        }
    }

    private static void resetThreadCount() {
        try {
            _lock.acquire();
            _threadCount = 0;
            _lock.release();
        } catch (Exception e) {

        }
    }

    private class GameTreeExplorer extends Thread {

        private GameTree _tree;
        private HeuristicFunction _fn;
        private ExpansionFunction _exp;
        private int _depth;

        public GameTreeExplorer(GameTree tree, HeuristicFunction fn, ExpansionFunction exp, int depth) {
            _tree= tree;
            _fn = fn;
            _exp = exp;
            _depth = depth;
        }

        @Override
        public void run(){
            try {
                Minimax(_fn,_exp,_depth);
            } catch (Exception e) {
                _tree.Minimax(_fn, _exp, _depth);
            }
        }

        public void Minimax(HeuristicFunction fn, ExpansionFunction exp, int depth) throws Exception {
            if (exp.DepthLimit(depth)) {
                _tree._values = fn.Evaluate(_tree._info.Info());
                return;
            }
            _tree.Expand(exp);
            if (_tree._children == null || _tree._children.size() < 1) {
                _tree._values = fn.Evaluate(_tree._info.Info());
                return;
            }
            List<GameTreeExplorer> threads = new ArrayList<>();
            for (GameTree child : _tree._children) {
                if (shouldFork()) {
                    GameTreeExplorer e = new GameTreeExplorer(child, fn, exp, depth+1);
                    threads.add(e);
                    e.start();
                }
                else child.Minimax(fn, exp, depth+1);
            }
            for (GameTreeExplorer e : threads) {
                e.join();
                releaseFork();
            }
            GameTree max = _tree._children.get(0);
            for (int i = 1; i < _tree._children.size(); i++) {
                int curr = _tree._info.CurrentPlayer();
                if (_tree._children.get(i)._values.Scores[curr] > max._values.Scores[curr]) {
                    max = _children.get(i);
                }
            }
            _tree._values = max._values;
        }
    }
}
