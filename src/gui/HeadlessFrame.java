package gui;

import game.Game;
import game.IPlayer;
import game.ScoredPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.*;

public class HeadlessFrame extends JFrame {
    private JPanel _panel;
    private JLabel _trialsLabel;
    private JLabel _fileLabel;
    private JTextField _trialsField;
    private JTextField _fileField;
    private JButton _run;
    private JButton _cancel;
    private HeartsFrame _gui;

    public HeadlessFrame(HeartsFrame gui) {
        _gui = gui;
        _panel = new JPanel();
        _trialsLabel = new JLabel("Number of Trials: ");
        _trialsField = new JTextField(3);

        _fileLabel = new JLabel("Filename: ");
        _fileField = new JTextField(15);

        _run = new JButton("Run trials");
        _run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int n = Integer.parseInt(_trialsField.getText());
                    if (n > 1000 || n < 1) {
                        JOptionPane.showMessageDialog(_gui, "Please select a number between 1 and 1000");
                    }
                    JOptionPane.showMessageDialog(_gui, "Game Simulations will run in the background");
                    dispose();
                    _gui.dispose();
                    String fileName = _fileField.getText();
                    runTrials(_gui.GetPlayers(),n, fileName != "" ? fileName : "hearts_results.txt");
                    JOptionPane.showMessageDialog(_gui, "Game Simulations Done! Output to " + fileName);
                } catch (Exception a) {
                    JOptionPane.showMessageDialog(_gui, "Please enter a number");
                }
            }
        });

        _cancel = new JButton("Cancel");

        _cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                _gui.setEnabled(true);
            }
        });

        _panel.setLayout(new GridLayout(1,6));
        _panel.add(_fileLabel);
        _panel.add(_fileField);
        _panel.add(_trialsLabel);
        _panel.add(_trialsField);
        _panel.add(_run);
        add(_panel);
    }

    private void runTrials(IPlayer[] players, int n, String filename) {
        Game g = new Game(players);
        java.util.List<Double> places = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            places.add(0.0);
        }
        for (int i = 0; i < n; i++) {
            g.NewGame();
            java.util.List<ScoredPlayer> rankings = g.RunGame();
            for (int j = 0; j < players.length; j++) {
                if (rankings.get(0).Player() == players[j]) places.set(j, places.get(j) + 1.0);
            }
        }
        try {
            PrintWriter pw = new PrintWriter(filename);
            pw.println("Number of trials: " + n);
            for (int i = 0; i < players.length-1; i++) {
                pw.print(players[i].getClass().getSimpleName() + ", ");
            }
            pw.print(players[players.length-1].getClass().getSimpleName());
            pw.println();
            pw.println(places);
            pw.close();
        } catch (Exception e) {

        }
    }

    public void createAndShowGui(){
        setPreferredSize(new Dimension(600, 60));
        pack();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
