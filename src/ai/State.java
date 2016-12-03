package ai;

import game.Move;
import game.SealedGameInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * TBD
 * <p>
 * User: elee
 * Date: 11/25/2016
 * Time: 4:12 PM
 */
public class State {
    private SealedGameInfo _info;
    private Move _move;
    private List<State> _children;

    public State(SealedGameInfo info, Move move){
        _info = info;
        _move = move;
    }

    public Move Move(){return _move;}

    public SealedGameInfo GameInfo(){return _info;}

    public List<State> Children(){return _children;}

    public void SetChildren(List<State> children){_children = children;}

    public @Override boolean equals(Object o){
        if(_move == null) return o instanceof State && ((State) o).GameInfo().equals(_info);
        return o instanceof State && ((State) o).Move().equals(_move) && ((State) o).GameInfo().equals(_info) && ((State) o).Children().equals(_children);
    }

    public @Override int hashCode(){
        int result = 1;
        if(_move == null){
            return result*37 + _info.hashCode();
        }
        result = result*37 + _move.hashCode();
        result = result*37 + _info.hashCode();
        return result;
    }

    public @Override String toString(){
        if(_move == null) return "No move - Round " + _info.RoundNumber();
        return _move.toString() + " - Round " + _info.RoundNumber();
    }

}
