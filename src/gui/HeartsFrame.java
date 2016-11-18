package gui;

import game.*;
import player.RuleBasedPlayer;

import javax.swing.*;
import java.awt.*;

public class HeartsFrame extends JFrame {
    private PlayerPanel[] _playerPanels;
    private IPlayer[] _players;
    private TrickPanel _trickPanel;
    private ScorePanel _scorePanel;
    private ControlBar _controls;
    private JPanel _gameboard;
    private JPanel _scoreboard;
    private JPanel _currentboard;
    private JMenuBar _menu;

    private Game _game;

    public HeartsFrame(){
        super("Hearts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        init();

        pack();
        setVisible(true);
    }



    private void init(){
        _menu = new JMenuBar();
        add(_menu);

        _gameboard = new JPanel();
        _scoreboard = new JPanel();
        _controls = new ControlBar(this);

        _gameboard.setLayout(new BorderLayout());

        _players = new IPlayer[4];
        _playerPanels = new PlayerPanel[4];

        String[] names = new String[4];

        for(int i = 0; i < _playerPanels.length; i++){
            _players[i] = new RuleBasedPlayer();
            int layout = (i % 2 == 0 ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS);
            _playerPanels[i] = new PlayerPanel(i, layout);
            names[i] = _playerPanels[i].Name();
        }

        _trickPanel = new TrickPanel();
        _scorePanel = new ScorePanel(names);

        _gameboard.add(_trickPanel, BorderLayout.CENTER);
        _gameboard.add(_playerPanels[0], BorderLayout.SOUTH);
        _gameboard.add(_playerPanels[1], BorderLayout.WEST);
        _gameboard.add(_playerPanels[2], BorderLayout.NORTH);
        _gameboard.add(_playerPanels[3], BorderLayout.EAST);

        _scoreboard.add(_scorePanel);

        _currentboard = _gameboard;
        add(_currentboard);
        add(_controls);

    }

    private void switchMode(Mode mode){
        remove(_currentboard);

        switch (mode){
            case HOME:
                break;
            case GAME:
                _currentboard = _gameboard;
                //add(_gameboard, 0);
                //setContentPane(_gameboard);
                break;
            case SCORE:
                _currentboard = _scoreboard;
                //setContentPane(_scoreboard);
        }
        add(_currentboard, 0);
        revalidate();
        repaint();
        //pack();
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void DisplayNextMove() throws Exception {
        if(_game == null){ _game = new Game(_players);}
        GameInfo info = _game.Info();
        switch (_game.State()){
            case START_ROUND:
                switchMode(Mode.GAME);
                _game.Step();
                for(PlayerPanel panel : _playerPanels){
                    panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
                }
                _trickPanel.Update(info.CurrentTrick(), 0);

                break;
            case PASS_CARDS:
                _game.Step();
                for(PlayerPanel panel : _playerPanels){
                    panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
                }
                break;
            case PLAYER_TURN:
                _game.Step();
                for(PlayerPanel panel : _playerPanels){
                    panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
                }
                _trickPanel.Update(info.CurrentTrick(), info.PlayerStartingTrick());

                break;
            case END_TRICK:
                _game.Step();
                _trickPanel.Update(info.CurrentTrick(), 0);
                for(int i = 0; i < _players.length; i++){
                    _playerPanels[i].UpdateScore(info.GetRoundScores()[i]);
                }
                break;
            case END_ROUND:
                _game.Step();
                _scorePanel.AddRoundScores(info.RoundNumber(), info.GetRoundScores());
                System.out.println(_scorePanel);
                switchMode(Mode.SCORE);
                break;
            case GAME_OVER:
                _scorePanel.AddRoundScores("Total", info.GetGameScores());
                switchMode(Mode.SCORE);
                return;
        }
        System.out.println(_game.State());
        info.PrintDebugInfo();

    }

    public void NewGame() throws Exception {
        _game = new Game(_players);
        _scorePanel.ClearScores();
        DisplayNextMove();
    }

    public static void main(String[] args) throws Exception {
        HeartsFrame frame = new HeartsFrame();
        //frame.RunGame();
    }
}
