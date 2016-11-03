package game;

import card.*;

public class Game {
    private GameInfo _info;
    private GameState _state;
    private Deck _deck;

    public Game(IPlayer[] players) {
        _deck = new Deck();
        _info = new GameInfo(players);
        for (int i = 0; i < players.length; i++) {
            players[i].Initialize(i);
        }
        _state = GameState.START_ROUND;
    }

    public void Step() {
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
                        moves[i] = players[i].PassCards();
                    }
                    _info.PassCards(moves);
                }
                _state = GameState.PLAYER_TURN;
                break;

            case PLAYER_TURN:
                _info.NextPlayerPlay();
                if (_info.IsTrickDone()) _state = GameState.END_TRICK;
                break;

            case END_TRICK:
                _info.NextTrick();
                if (_info.IsRoundOver()) _state = GameState.END_ROUND;
                else _state = GameState.PLAYER_TURN;
                break;

            case END_ROUND:
                _info.EndRound();
                if (_info.IsGameOver()) _state = GameState.GAME_OVER;
                else _state = GameState.START_ROUND;
                break;

            case GAME_OVER:

                break;
        }
    }

    public GameInfo Info() {
        return _info;
    }

    public GameState State() {
        return _state;
    }

}
