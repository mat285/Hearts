package ai;

import game.*;
import player.RuleBasedPlayer;

import java.util.*;

public class MonteCarloSimulator {
    public static final double DEFAULT_C = 2.0;
    public static final int DEFAULT_NUM_SIMS = 300;

    private double _c;
    private int _numSims;
    private int _playerID;
    private IPlayer _player;
    private State _root;
    private Map<State, Integer> _plays;
    private Map<State, Integer> _wins;

    /**
     * Creates a new Monte Carlo game tree simulator
     * @param playerID the id of the player who's using the simulator
     * @param numSims the number of simulations this simulator runs
     * @param c the constant used in the selection formula
     * */
    public MonteCarloSimulator(int playerID, int numSims, double c){
        _playerID = playerID;
        _numSims = numSims;
        _c = c;
    }

    /**
     * Creates an array with 4 rule based players
     * @return an array with 4 rule based players
     * */
    private IPlayer[] players(){
        IPlayer[] players = new IPlayer[4];
        for(int i = 0; i < players.length; i++){
            players[i] = new RuleBasedPlayer();
            if(i == _playerID) _player = players[i];
        }
        return players;
    }

    /**
     * Returns the best move found by the Monte Carlo search algorithm from
     * the current SealedGameInfo
     * @param info
     * @return the best move generated from the simulator
     * */
    public Move GetMove(SealedGameInfo info){
        _plays = new HashMap<>();
        _wins = new HashMap<>();

        _root = new State(info, null);
        List<Move> moves = GameUtils.GetAllValidMoves(info);

        if(moves.isEmpty()) return null;
        if(moves.size() == 1) return moves.get(0);

        _plays.put(_root, 0);
        _wins.put(_root, 0);

        runSimulation();

        //find best move from possible move states
        Move best = null;
        double highest = -1.0;
        for(State state : _root.Children()){
            if(!_plays.containsKey(state)) continue;
            if(highest < _wins.get(state) / (double) _plays.get(state)){
                highest = _wins.get(state) / (double) _plays.get(state);
                best = state.Move();
            }
        }
        return best;
    }

    /**
     * Creates child nodes for the given state
     * @param state the current state to create children
     * */
    private void setChildren(State state){
        List<State> children = new ArrayList<>();
        List<Move> moves = GameUtils.GetAllValidMoves(state.GameInfo());

        for(Move move : moves){
            SealedGameInfo gameInfo = state.GameInfo().Clone();
            GameInfo info = GameInfo.DistributeRandomly(gameInfo, players(), _playerID);
            Game g = Game.StartFromMove(info, move);

            //find next state
            while(g.State() != GameState.PLAYER_TURN || info.CurrentPlayerNumber() != _playerID){
                if(g.State() == GameState.GAME_OVER || g.State() == GameState.START_ROUND){
                    break;
                }
                g.Step();
            }

            children.add(new State(info.Seal(_player), move));
        }

        state.SetChildren(children);
    }

    /**
     * Runs repeated simulations of the game tree search algorithm
     * */
    private void runSimulation(){
        for(int i = 1; i <= _numSims; i++){
            State current = _root;
            Set<State> visited = new HashSet<>();
            while(_plays.containsKey(current)){
                visited.add(current);

                if(current.Children() == null) setChildren(current);

                List<State> children = current.Children();

                if(children.isEmpty()){
                    break;
                }

                double max = -1.0;
                for(State state : children){
                    _plays.putIfAbsent(state, 0);
                    _wins.putIfAbsent(state, 0);

                    double n = (double) _plays.get(state);
                    if(_plays.get(state) == 0) n = 0.0001;

                    double value = _wins.get(state) / n + _c * Math.sqrt(Math.log(i) / n);
                    if(value > max){
                        max = value;
                        current = state;
                    }
                }
            }

            visited.add(current);

            GameInfo info = GameInfo.DistributeRandomly(current.GameInfo().Clone(), players(), _playerID);
            int[] roundScores;

            if(info.IsRoundOver() || info.IsGameOver()){
                roundScores = info.GetRoundScores();
            }else{
                Game game = Game.StartFromState(info, GameState.PLAYER_TURN);
                game.RunGame();
                roundScores = info.GetRoundScores();
            }

            boolean hasWon = roundScores[_playerID] ==
                    Math.min(roundScores[0],
                            Math.min(roundScores[1],
                                    Math.min(roundScores[2], roundScores[3])));


            for(State state : visited){

                _plays.put(state, _plays.get(state) + 1);
                if(hasWon){
                    _wins.put(state, _wins.get(state) + 1);
                }
            }
        }
    }

}
