package gui;

import javax.swing.*;
import java.awt.*;

public class HeartsFrame extends JFrame {
    private PlayerPanel[] _playerPanels;
    private AbstractPlayer[] _players;
    private TrickPanel _trickPanel;
    private Game _game;

    public HeartsFrame(){
        super("Hearts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        init();

        pack();
        setVisible(true);
    }

    private void init(){
        _trickPanel = new TrickPanel();
        _players = new Player[4];
        _playerPanels = new PlayerPanel[4];
        for(int i = 0; i < _playerPanels.length; i++){
            _players[i] = new Player();
            if(i % 2 == 0){
                _playerPanels[i] = new PlayerPanel(_players[i], BoxLayout.X_AXIS);
            }else{
                _playerPanels[i] = new PlayerPanel(_players[i], BoxLayout.Y_AXIS);
            }
        }

        add(_trickPanel, BorderLayout.CENTER);
        add(_playerPanels[0], BorderLayout.SOUTH);
        add(_playerPanels[1], BorderLayout.EAST);
        add(_playerPanels[2], BorderLayout.NORTH);
        add(_playerPanels[3], BorderLayout.WEST);
    }

    public void RunGame(){
        _game = new Game(_players);
        GameInfo info = _game.Info();

        for(PlayerPanel panel : _playerPanels){
            panel.DrawHand(info.HandOfPlayer(panel.Player()));
        }

        _trickPanel.Update(info.CurrentTrick());

        while(_game.State() != GameState.GAME_OVER){

        }

    }
}
