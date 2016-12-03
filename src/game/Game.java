package game;

import card.*;
import java.util.*;

public class Game {
    private GameInfo _info;
    private GameState _state;
    private IPlayer[] _players;
    private Deck _deck;

    /**
     * Creates a new Game object with the given players
     * @param players The players of the game
     */
    public Game(IPlayer[] players) {
        _deck = new Deck();
        _players = players;
        NewGame();
    }

    private Game() {
        _deck = new Deck();
    }

    /**
     * Resets this Game object to start a new game
     */
    public void NewGame() {
        _info = new GameInfo(_players);
        for (int i = 0; i < _players.length; i++) {
            _players[i].Initialize(i);
        }
        _state = GameState.START_ROUND;
    }

    /**
     * Steps one more move through the current game based on the state
     * @return List of ScoredPlayers from first to last place if the game is over, null otherwise
     */
    public List<ScoredPlayer> Step() {
        switch (_state) {
            case START_ROUND:
                _deck.Shuffle();
                _info.NextRound(_deck.Deal());
                _state = GameState.PASS_CARDS;
                break;

            case PASS_CARDS:
                if (_info.PassDirection() != Direction.STAY) {
                    IPlayer[] players = _info.Players();
                    CardPassMove[] moves = new CardPassMove[players.length];
                    for (int i = 0; i < players.length; i++) {
                        moves[i] = players[i].PassCards(_info.Seal(players[i]));
                    }
                    _info.PassCards(moves);
                }
                _info.FindAndSetFirstPlayer();
                _state = GameState.PLAYER_TURN;
                break;

            case PLAYER_TURN:
                _info.NextPlayerPlay();
                if (_info.IsTrickDone()) _state = GameState.END_TRICK;
                break;

            case END_TRICK:
                _info.NextTrick();
                _info.SanityCheck();
                if (_info.IsRoundOver()) _state = GameState.END_ROUND;
                else _state = GameState.PLAYER_TURN;
                break;

            case END_ROUND:
                _info.EndRound();
                if (_info.IsGameOver()) _state = GameState.GAME_OVER;
                else _state = GameState.START_ROUND;
                break;

            case GAME_OVER:
                return _info.GetRanking();
        }
        return null;
    }

    /**
     * Runs through the rest of the game until the game is over
     * @return List of scored players from first to last
     */
    public List<ScoredPlayer> RunGame() {
        List<ScoredPlayer> s = null;
        while (s == null) {
            s = Step();
        }
        return s;
    }

    /**
     * Returns the GameInfo object maintained by this game
     * @return the GameInfo object maintaining information about this game
     */
    public GameInfo Info() {
        return _info;
    }

    /**
     * Returns the current state of the game
     * @return the current Game State
     */
    public GameState State() {
        return _state;
    }

    /**
     * Creates a new game starting the game from the current state
     * @param info the GameInfo about the game
     * @param state the current Game state
     * @return
     */
    public static Game StartFromState(GameInfo info, GameState state) {
        Game g = new Game();
        g._info = info;
        g._state = state;
        return g;
    }

    public static Game StartFromMove(GameInfo info, Move move){
        Game g = StartFromState(info, GameState.PLAYER_TURN);
        info.NextPlayerPlay(move);
        if (info.IsTrickDone()) g._state = GameState.END_TRICK;
        return g;
    }
}


