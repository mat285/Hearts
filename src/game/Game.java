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
        _state = GameState.INITIALIZE;
    }

    public void Step() {
        switch (_state) {
            case INITIALIZE:
                _deck.Shuffle();
                _info.NextRound(_deck.Deal());
                _state = GameState.START_ROUND;
                break;

            case START_ROUND:
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
                while(!_info.IsTrickDone()) {
                    _info.NextPlayerPlay();
                }
                _state = GameState.END_TRICK;
                break;

            case END_TRICK:
                _info.NextTrick();
                if (_info.IsRoundOver()) _state = GameState.END_ROUND;
                else _state = GameState.PLAYER_TURN;
                break;

            case END_ROUND:

                break;

            case GAME_OVER:

                break;
        }
    }

}
