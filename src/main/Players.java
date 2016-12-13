package main;

import ai.*;
import player.RandomPlayer;
import player.RuleBasedPlayer;

/**
 * TBD
 * <p>
 * User: elee
 * Date: 12/2/2016
 * Time: 6:30 PM
 */
public class Players {
    public static final Class[] PLAYER_TYPES = {RandomPlayer.class, RuleBasedPlayer.class, FirstAIPlayer.class, SecondAIPlayer.class, ThirdAIPlayer.class};

    public static boolean KnownType(Class type){
        for(Class c : PLAYER_TYPES){
            if(c.equals(type)) return true;
        }
        return false;
    }
}
