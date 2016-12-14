package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlBar extends JPanel {
    private HeartsFrame _gui;
    private Timer _timer;
    private static final Font DEFAULT_FONT = new Font("Calibri", Font.BOLD, 40);

    public ControlBar(HeartsFrame gui){
        _gui = gui;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton changePlayersButton = new JButton("Change Players");
        JButton newGameButton = new JButton("New Game");
        JButton startButton = new JButton("Play");
        JButton stepButton = new JButton("Step");
        JButton stopButton = new JButton("Pause");

        changePlayersButton.setFont(DEFAULT_FONT);
        newGameButton.setFont(DEFAULT_FONT);
        startButton.setFont(DEFAULT_FONT);
        stepButton.setFont(DEFAULT_FONT);
        stopButton.setFont(DEFAULT_FONT);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _gui.NewGame();
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(_timer == null){
                    _timer = _gui.GetGameTimer();
                }
                _timer.start();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(_timer == null) return;
                _timer.stop();

            }
        });
        changePlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(_timer != null) _timer.stop();
                ChoosePlayerFrame frame = new ChoosePlayerFrame(_gui);
                frame.createAndShowGui();
            }
        });
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(_timer != null) _timer.stop();
                _gui.Step(null);
            }
        });

        add(changePlayersButton);
        add(newGameButton);
        add(startButton);
        add(stepButton);
        add(stopButton);
    }
}
