package gui;

import game.*;
import player.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class HeartsFrame extends JFrame {
    public static final Font DEFAULT_FONT = new Font("Calibri", Font.BOLD, 30);
    public static final Color BACKGROUND = new Color(15,117,51);

    public static final String[] LOCATIONS = {BorderLayout.SOUTH, BorderLayout.WEST, BorderLayout.NORTH, BorderLayout.WEST};

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
    private JMenuItem _runTrials;
    private JMenu _speed;
    private JRadioButtonMenuItem _halfSpeed;
    private JRadioButtonMenuItem _regSpeed;
    private JRadioButtonMenuItem _twiceSpeed;
    private JRadioButtonMenuItem _fourSpeed;

    private int _width;
    private int _height;

    private double _speedMult = 1.0;
    private int _gameSpeed = 400;

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

        _height = (int) (resolution.getHeight() * 0.8);
        _width = (int) (_height * 1.5);
        if(_width > resolution.getWidth()){_width = (int) (resolution.getWidth());}

        CardImage.SetHeight(_height / 8);
        PlayerPanel.SetDimension(_width * 2, _height / 8);
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
        _gamePanel.setBackground(BACKGROUND);
        _scorePanel.setBackground(BACKGROUND);
        _controls.setBackground(BACKGROUND.darker());

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

        _runTrials = new JMenuItem("Run Trials");
        _runTrials.addActionListener(_controls.RunHeadless());

        _options.add(_changePlayers);
        _options.add(_newGame);
        _options.add(_runTrials);

        _speed = new JMenu("Speed");
        _halfSpeed = new JRadioButtonMenuItem("0.5x");
        _regSpeed = new JRadioButtonMenuItem("1.0x");
        _twiceSpeed = new JRadioButtonMenuItem("2.0x");
        _fourSpeed = new JRadioButtonMenuItem("4.0x");

        ButtonGroup b = new ButtonGroup();
        b.add(_halfSpeed);
        b.add(_regSpeed);
        b.add(_twiceSpeed);
        b.add(_fourSpeed);
        _regSpeed.setSelected(true);

        _halfSpeed.addActionListener(getSpeedListener(2.0));
        _regSpeed.addActionListener(getSpeedListener(1.0));
        _twiceSpeed.addActionListener(getSpeedListener(0.5));
        _fourSpeed.addActionListener(getSpeedListener(0.25));

        _speed.add(_halfSpeed);
        _speed.add(_regSpeed);
        _speed.add(_twiceSpeed);
        _speed.add(_fourSpeed);

        _menu.add(_options);
        _menu.add(_speed);

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
            if (_players[i] instanceof GUIPlayer) {
                Pipe p = new Pipe();
                _playerPanels[i].WireUpUser(p);
                ((GUIPlayer) _players[i]).SetPipe(p);
            }
            else {
                _playerPanels[i].WireDownUser();
            }
            _playerPanels[i].SetName(_players[i].getClass().getSimpleName());
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

                    if(timer != null) Thread.sleep((int) (1000 * _speedMult));
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
        Timer timer = new Timer((int) (_gameSpeed * _speedMult), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                Step((Timer) e.getSource());
            }
        });
        return timer;
    }

    public IPlayer[] GetPlayers() {
        return _players;
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
                panel.UpdateScore(0);
            }
            _trickPanel.Update(info.CurrentTrick(), 0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createAndShowGui(){
        try{
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(_width, _height));
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            setIconImage(ImageIO.read(new File("src/assets/misc/heart.png")));
            pack();
            setVisible(true);
            setResizable(false);
            NewGame();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ActionListener getSpeedListener(final double speedMult) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _speedMult = speedMult;
                _controls.ResetTimer((int) (_speedMult*_gameSpeed));
            }
        };
    }

    public static void main(String[] args){
        HeartsFrame frame = new HeartsFrame();
        frame.createAndShowGui();
    }

}
