package gui;

import ai.AIPlayer;
import game.*;
import player.RandomPlayer;
import player.RuleBasedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeartsFrame extends JFrame {
    private PlayerPanel[] _playerPanels;
    private IPlayer[] _players;
    private TrickPanel _trickPanel;
    private ScorePanel _scorePanel;
    private ControlBar _controls;
    private JPanel _gamePanel;
    private ChoosePlayerPanel _choosePlayerPanel;
    private JPanel _currentboard;
    private JMenuBar _menu;

    private Game _game;

    public HeartsFrame(){
        super("Hearts");
        try{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(2000,2000));
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            init();
            pack();
            setVisible(true);
            setResizable(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() throws Exception{
        _menu = new JMenuBar();
        add(_menu);

        _gamePanel = new JPanel();
        _controls = new ControlBar(this);

        _gamePanel.setLayout(new BorderLayout());


        _playerPanels = new PlayerPanel[4];

        String[] names = new String[4];

        for(int i = 0; i < _playerPanels.length; i++){
            int layout = (i % 2 == 0 ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS);
            _playerPanels[i] = new PlayerPanel(i, layout);
            names[i] = _playerPanels[i].Name();
        }

        SetPlayers(null);
        _trickPanel = new TrickPanel();
        _scorePanel = new ScorePanel(names);
        _choosePlayerPanel = new ChoosePlayerPanel(this);

        _gamePanel.add(_trickPanel, BorderLayout.CENTER);
        _gamePanel.add(_playerPanels[0], BorderLayout.SOUTH);
        _gamePanel.add(_playerPanels[1], BorderLayout.WEST);
        _gamePanel.add(_playerPanels[2], BorderLayout.NORTH);
        _gamePanel.add(_playerPanels[3], BorderLayout.EAST);

        _currentboard = _gamePanel;
        add(_currentboard);
        add(_controls);
    }

    public void SetPlayers(IPlayer[] players) throws Exception {
        if(players == null){
            //default settings
            _players = new IPlayer[4];
            _players[0] = new AIPlayer();
            _players[1] = new RuleBasedPlayer();
            _players[2] = new RandomPlayer();
            _players[3] = new RandomPlayer();
            return;
        }
        if(players.length != 4) throw new Exception("Invalid number of players");
        _players = players;

/*        if(_playerPanels != null){
            for(int i = 0; i < _players.length; i++){
                _playerPanels[i].SetName(_players[i].getClass().getSimpleName());
            }
        }*/
    }

    public void SwitchMode(Mode mode){
        remove(_currentboard);
        switch (mode){
            case CHOOSEPLAYER:
                _currentboard = _choosePlayerPanel;
                break;
            case GAME:
                _currentboard = _gamePanel;
                break;
            case SCORE:
                _currentboard = _scorePanel;
        }
        add(_currentboard, 0);
        revalidate();
        repaint();
    }

    public Timer GetGameTimer() throws Exception {
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(_game == null){
                    _game = new Game(_players);
                }
                GameInfo info = _game.Info();
                try {
                    switch (_game.State()) {
                        case START_ROUND:
                            SwitchMode(Mode.GAME);
                            _game.Step();
                            for (PlayerPanel panel : _playerPanels) {
                                panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
                                panel.UpdateScore(0);
                            }
                            _trickPanel.Update(info.CurrentTrick(), 0);
                            break;
                        case PASS_CARDS:
                            _game.Step();
                            for (PlayerPanel panel : _playerPanels) {
                                panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
                            }
                            break;
                        case PLAYER_TURN:
                            _game.Step();
                            for (PlayerPanel panel : _playerPanels) {
                                panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
                            }
                            _trickPanel.Update(info.CurrentTrick(), info.PlayerStartingTrick());

                            break;
                        case END_TRICK:
                            _game.Step();
                            _trickPanel.Update(info.CurrentTrick(), 0);
                            for (int i = 0; i < _players.length; i++) {
                                _playerPanels[i].UpdateScore(info.GetRoundScores()[i]);
                            }
                            break;
                        case END_ROUND:
                            _game.Step();
                            _scorePanel.AddRoundScores(info.RoundNumber(), info.GetRoundScores());
                            SwitchMode(Mode.SCORE);
                            break;
                        case GAME_OVER:
                            _scorePanel.AddRoundScores("Total", info.GetGameScores());
                            SwitchMode(Mode.SCORE);
                            break;
                    }
                }catch (Exception exp){
                    exp.printStackTrace();
                }
            }
        });
        return timer;
    }

    public void NewGame() throws Exception {
        _game = new Game(_players);
        GameInfo info = _game.Info();
        _scorePanel.ClearScores();
        SwitchMode(Mode.GAME);
        _game.Step();
        for (PlayerPanel panel : _playerPanels) {
            panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
        }
        _trickPanel.Update(info.CurrentTrick(), 0);
    }


    public static void main(String[] args) throws Exception {
        HeartsFrame frame = new HeartsFrame();
/*
        ChoosePlayerPanel panel = new ChoosePlayerPanel(frame);
        frame.setContentPane(panel);
        frame.pack();
*/

    }
}
