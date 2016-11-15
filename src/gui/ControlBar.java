package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControlBar extends JPanel {
    List<JButton> _gameButtons = new ArrayList<>();
    List<JButton> _menuButtons = new ArrayList<>();
    HeartsFrame _view;

    public ControlBar(HeartsFrame view){
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        _view = view;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton newGameButton = new JButton("New Game");
        JButton stepButton = new JButton("Step");

        /*_gameButtons.add(newGameButton);
        _gameButtons.add(playButton);*/
        _gameButtons.add(newGameButton);
        _gameButtons.add(stepButton);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    _view.NewGame();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    _view.DisplayNextMove();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        for(JButton button : _gameButtons){
            add(button);
        }

    }
}
