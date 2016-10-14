package game;

import java.util.*;
import card.*;

public class Game {
    private GameState _state;
    private Deck _deck;

    public Game(IPlayer[] players) {
        _deck = new Deck();
        _state = new GameState(players);
    }

}
