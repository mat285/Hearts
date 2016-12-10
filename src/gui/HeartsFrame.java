package gui;

import ai.AIPlayer;
import game.*;
import player.RandomPlayer;
import player.RuleBasedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class HeartsFrame extends JFrame {
    private PlayerPanel[] _playerPanels;
    private IPlayer[] _players;
    private TrickPanel _trickPanel;
    private ScorePanel _scorePanel;
    private ControlBar _controls;
    private JPanel _gamePanel;
    private JPanel _currentboard;
    private JMenuBar _menu;

    private Game _game;

    public HeartsFrame(){
        super("Hearts");
        try{
            init();
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

        _gamePanel.add(_trickPanel, BorderLayout.CENTER);
        _gamePanel.add(_playerPanels[0], BorderLayout.SOUTH);
        _gamePanel.add(_playerPanels[1], BorderLayout.WEST);
        _gamePanel.add(_playerPanels[2], BorderLayout.NORTH);
        _gamePanel.add(_playerPanels[3], BorderLayout.EAST);
        _gamePanel.setBackground(new Color(15,117,51));
        _scorePanel.setBackground(new Color(15,117,51));
        _currentboard = _gamePanel;
        add(_currentboard);
        add(_controls);
    }

    public void SetPlayers(IPlayer[] players) throws Exception {
        if(players == null){
            //default settings
            _players = new IPlayer[4];
            for(int i = 0; i < 4; i++){
                _players[i] = new RandomPlayer();
            }
            return;
        }
        if(players.length != 4) throw new Exception("Invalid number of players");
        _players = players;
    }

    public void SwitchMode(Mode mode){
        remove(_currentboard);
        switch (mode){
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

    public void Step(Timer timer){
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
                    _trickPanel.HideHeartsBroken();
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
                        panel.Unhighlight();
                    }
                    _trickPanel.Update(info.CurrentTrick(), info.PlayerStartingTrick());

                    if(info.IsHeartsBroken()) _trickPanel.DisplayHeartsBroken();

                    break;
                case END_TRICK:
                    int winner = info.TrickWinner();
                    _playerPanels[winner].Highlight();

                    _game.Step();
                    _trickPanel.Update(info.CurrentTrick(), 0);
                    for (int i = 0; i < _players.length; i++) {
                        _playerPanels[i].UpdateScore(info.GetRoundScores()[i]);
                    }

                    if(timer != null) Thread.sleep(2000);
                    break;
                case END_ROUND:
                    _game.Step();
                    _scorePanel.AddRoundScores(info.RoundNumber(), info.GetRoundScores());
                    SwitchMode(Mode.SCORE);
                    if(timer != null) timer.stop();
                    break;
                case GAME_OVER:
                    _scorePanel.AddRoundScores("Total", info.GetGameScores());
                    SwitchMode(Mode.SCORE);
                    if(timer != null) timer.stop();
                    _game = null;
                    break;
            }
            //info.PrintDebugInfo();
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    public Timer GetGameTimer(){
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                Step((Timer) e.getSource());
            }
        });
        return timer;
    }

    public void NewGame(){
        try{
            _game = new Game(_players);
            GameInfo info = _game.Info();
            _scorePanel.ClearScores();
            SwitchMode(Mode.GAME);
            _game.Step();
            for (PlayerPanel panel : _playerPanels) {
                panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
            }
            _trickPanel.Update(info.CurrentTrick(), 0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createAndShowGui(){
        try{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(2000,2000));
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            setBackground(Color.GREEN);
            pack();
            setVisible(true);
            setResizable(false);
            NewGame();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        HeartsFrame frame = new HeartsFrame();
        frame.createAndShowGui();
    }

}
