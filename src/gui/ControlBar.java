package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControlBar extends JPanel {
    private HeartsFrame _gui;
    private Timer _timer;

    public ControlBar(HeartsFrame gui){
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        _gui = gui;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton changePlayersButton = new JButton("Change Players");
        JButton newGameButton = new JButton("New Game");
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Pause");

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(_timer != null) _timer.stop();
                    _gui.NewGame();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(_timer == null){
                        _timer = _gui.GetGameTimer();
                    }
                    _timer.start();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(_timer == null) return;
                    _timer.stop();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        changePlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(_timer != null) _timer.stop();
                    //_gui.setEnabled(false);
                    ChoosePlayerFrame frame = new ChoosePlayerFrame(_gui);
                    frame.createAndShowGui();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        add(changePlayersButton);
        add(newGameButton);
        add(startButton);
        add(stopButton);

    }
}
