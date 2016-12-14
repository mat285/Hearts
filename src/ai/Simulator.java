package ai;

import game.*;
import player.RuleBasedPlayer;

import java.util.*;

public class Simulator {
    public static final double DEFAULT_C = 2.0;
    public static final int DEFAULT_NUM_SIMS = 300;

    private double _c = 2.0;
    private int _numSims = 300;
    private int _playerID;
    private IPlayer _player;

    public Simulator(int playerID, int numSims, double c){
        _playerID = playerID;
        _numSims = numSims;
        _c = c;
    }

    public Simulator(int playerID){
        this(playerID, 300, 2.0);
    }

    private IPlayer[] players(){
        IPlayer[] players = new IPlayer[4];
        for(int i = 0; i < players.length; i++){
            players[i] = new RuleBasedPlayer();
            if(i == _playerID) _player = players[i];
        }
        return players;
    }

    public Move GetMove(SealedGameInfo info){
        Map<State, Integer> plays = new HashMap<>();
        Map<State, Integer> wins = new HashMap<>();

        State root = new State(info, null);
        List<Move> moves = GameUtils.GetAllValidMoves(info);

        if(moves.isEmpty()) return null;
        if(moves.size() == 1) return moves.get(0);

        plays.put(root, 0);
        wins.put(root, 0);

        runSimulation(root, plays, wins);

        //find best move from possible move states
        Move best = null;
        double highest = -1.0;
        //System.out.println("Num moves: " + root.Children().size());
        for(State state : root.Children()){
            if(!plays.containsKey(state)) continue;

            //System.out.println(wins.get(state) / (double) plays.get(state));

            if(highest < wins.get(state) / (double) plays.get(state)){
                highest = wins.get(state) / (double) plays.get(state);
                best = state.Move();
            }
        }
        return best;
    }

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

    private void runSimulation(State root, Map<State, Integer> plays, Map<State, Integer> wins){
        //System.out.println("*******Round " + root.GameInfo().RoundNumber()+"***********");

        for(int i = 1; i <= _numSims; i++){
            State current = root;
            Set<State> visited = new HashSet<>();

            while(plays.containsKey(current)){
                visited.add(current);

                if(current.Children() == null) setChildren(current);

                List<State> children = current.Children();
                //List<State> children = getChildren(current);

                if(children.isEmpty()){
                    break;
                }

                /*if(plays.keySet().containsAll(children)){
                    double max = Double.MIN_VALUE;
                    for(State state : children){
                        double value = wins.get(state) / (double) plays.get(state) + C * Math.sqrt(Math.log(i) / plays.get(state));
                        if(value > max){
                            max = value;
                            current = state;
                        }
                    }
                }else{
                    int n = r.nextInt(children.size());
                    current = children.get(n);
                }*/
                double max = -1.0;
                //System.out.println("NUM CHILDREN " + children.size());
                for(State state : children){
                    plays.putIfAbsent(state, 0);
                    wins.putIfAbsent(state, 0);

                    double n = (double) plays.get(state);
                    if(plays.get(state) == 0) n = 0.0001;

                    double value = wins.get(state) / n + _c * Math.sqrt(Math.log(i) / n);
                    //System.out.println(value);
                    if(value > max){
                        max = value;
                        current = state;
                    }
                }
            }

            visited.add(current);

            //System.out.println(current);

            //simulate

            GameInfo info = GameInfo.DistributeRandomly(current.GameInfo().Clone(), players(), _playerID);
            int[] roundScores;

            //if game ended
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

                plays.put(state, plays.get(state) + 1);
                if(hasWon){
                    wins.put(state, wins.get(state) + 1);
                }
            }
        }
    }

}
