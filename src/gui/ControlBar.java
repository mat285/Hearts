package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ControlBar extends JPanel {
    private List<JButton> _gameButtons = new ArrayList<>();
    private List<JButton> _menuButtons = new ArrayList<>();
    private HeartsFrame _gui;

    public ControlBar(HeartsFrame gui){
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        _gui = gui;
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
                    _gui.NewGame();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                //Object[] options = {"AI","Random","Rule"};
                /*JOptionPane.showInputDialog(gui, "Player 1: \t", "Choose Players",
                        JOptionPane.PLAIN_MESSAGE, null, options, "AI");*/

            }
        });
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    _gui.DisplayNextMove();
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
