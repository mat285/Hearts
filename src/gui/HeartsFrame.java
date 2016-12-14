package gui;

import ai.*;
import game.*;
import player.*;
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
    private JPanel _currentBoard;

    private JMenuBar _menu;
    private JMenu _options;
    private JMenuItem _changePlayers;
    private JMenuItem _newGame;

    private int length;

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
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        length = (int) (resolution.getHeight() * 0.8);
        CardImage.SetHeight(length / 8);
        PlayerPanel.SetDimension(length, length / 8);
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
        _currentBoard = _gamePanel;
        add(_currentBoard);
        add(_controls);
        initMenuBar();
    }

    private void initMenuBar() {
        _menu = new JMenuBar();

        _options = new JMenu("Options");

        _changePlayers = new JMenuItem("Change Players");
        _changePlayers.addActionListener(_controls.ChangePlayers());

        _newGame = new JMenuItem("New Game");
        _newGame.addActionListener(_controls.NewGame());

        _options.add(_changePlayers);
        _options.add(_newGame);
        _menu.add(_options);

        setJMenuBar(_menu);
    }

    public void SetPlayers(IPlayer[] players) throws Exception {
        if(players == null){
            //default settings
            players = new IPlayer[4];
            for(int i = 0; i < 4; i++){
                players[i] = new RandomPlayer();
            }
        }
        if(players.length != 4) throw new Exception("Invalid number of players");
        _players = players;

        for (int i = 0; i < _players.length; i++) {
            _playerPanels[i].SetName(_players[i].toString());
        }
    }

    public void SwitchMode(Mode mode){
        remove(_currentBoard);
        switch (mode){
            case GAME:
                _currentBoard = _gamePanel;
                break;
            case SCORE:
                _currentBoard = _scorePanel;
        }
        add(_currentBoard, 0);
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
            _trickPanel.HideHeartsBroken();
            for (PlayerPanel panel : _playerPanels) {
                panel.UpdateHand(info.HandOfPlayer(_players[panel.Id()]));
                panel.Unhighlight();
            }
            _trickPanel.Update(info.CurrentTrick(), 0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createAndShowGui(){
        try{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(length,length));
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
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
