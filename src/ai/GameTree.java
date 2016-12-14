package ai;

import game.*;
import player.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class GameTree {

    public static final int DEFAULT_SEARCH_DEPTH = 6;
    public static final int DEFAULT_NUMBER_SIMULATIONS = 10;

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

    /**
     * Expands the desired children into game tree nodes
     * @param exp the expansion function to determine expansion
     */
    public void Expand(ExpansionFunction exp) {
        List<Move> moves = GameUtils.GetAllValidMoves(_info.Info());
        _children = new ArrayList<>();
        for (Move m : moves) {
            if (!exp.ExpandMove(m,_info.Info())) continue;
            GameTree child = new GameTree(_info.PlayMove(m),m);
            _children.add(child);
        }
    }

    /**
     * Run the minimax algorithm on this game tree producing values for all nodes
     * @param fn the heuristic evaluation function for leaf nodes
     * @param exp the expansion function to determine node expansion
     * @param depth the current depth of the search
     */
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

    /**
     * Gets the best move from the minimax values computed already
     * @return the best move to make
     */
    private Move bestMove() {
        if (_children == null) return null;
        Collections.shuffle(_children);
        for (GameTree child : _children) {
            if (child._values.equals(this._values)) return child._move;
        }
        return null;
    }

    /**
     * Runs minimax in parallel and returns the best move
     * @param fn the heuristic evaluation function
     * @param exp the node expansion function
     * @return the best move found
     */
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

    /**
     * Simulates many games and picks the move that won most of them by distributing the remaining cards randomly and
     * then building a game tree and running Minimax on the tree up to the specified depth
     * @param info The game info for the current state of the game
     * @param currentPlayer the id of player currently making the move
     * @param fn the heuristic function to evaluate leaf nodes
     * @param exp the expansion function that determines whether to expand a node or not
     * @param times the number of times to simulate
     * @return The best move from the simulated games
     */
    public static Move Simulate(SealedGameInfo info, int currentPlayer, HeuristicFunction fn, ExpansionFunction exp, int times) {
        if (times < 1) return null;
        Map<Move,Integer> map = new HashMap<>();
        Move max = null;
        for (int i = 0; i < times; i++) {
            GameInfo g = GameInfo.DistributeRandomly(info, getPlayers(), currentPlayer);
            GameTree tree = new GameTree(new GameTreeInfo(g),null);
            Move m = tree.BestMove(fn,exp);
            if (!map.containsKey(m)) map.put(m,0);
            map.put(m,map.get(m)+1);
            if (max == null || map.get(max) < map.get(m)) max = m;
        }
        return max;
    }

    /**
     * Creates an array of random players to use in the card distribution
     * @return an array of four players
     */
    private static IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[4];
        for (int i = 0; i < players.length; i++) {
            players[i] = new RandomPlayer();
        }
        return players;
    }

    /* Parallel game tree exploring functions*/

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
