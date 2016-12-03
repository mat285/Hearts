package main;

import game.IPlayer;
import player.Players;

/**
 * TBD
 * <p>
 * User: elee
 * Date: 12/2/2016
 * Time: 6:32 PM
 */
public class Resolver {

    public static IPlayer ResolvePlayer(Class type){
        IPlayer player;
        try{
            if(Players.KnownType(type)) player = (IPlayer) type.newInstance();
            else throw new IllegalArgumentException("Unknown class type");
            return player;
        }catch(IllegalArgumentException i){
            throw i;
        }catch(Exception e){
            throw new IllegalArgumentException(e);
        }
    }
}
