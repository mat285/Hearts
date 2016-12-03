package gui;

import ai.AIPlayer;
import game.IPlayer;
import main.Resolver;
import player.Players;
import player.RandomPlayer;
import player.RuleBasedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.GroupLayout.*;


/**
 * TBD
 * <p>
 * User: elee
 * Date: 11/17/2016
 * Time: 6:44 PM
 */
public class ChoosePlayerFrame extends JFrame{
    private HeartsFrame _gui;
    private JComboBox[] _selected = new JComboBox[4];
    private static final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 40);
    private JPanel _contentPane;


    public ChoosePlayerFrame(HeartsFrame gui){
        _gui = gui;
        _contentPane = new JPanel();

        _contentPane.setLayout(new BoxLayout(_contentPane   , BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(4,4);
        panel.setLayout(layout);

        Vector<Type> types = new Vector<>();
        for(Class type : Players.PLAYER_TYPES){
            types.add(new Type(type.getSimpleName(), type));
        }

        for(int i = 0; i < 4; i++){
            JLabel label = new JLabel("Player " + (i+1));
            _selected[i] = new JComboBox(types);
            _selected[i].setSize(label.getSize());
            _selected[i].setFont(DEFAULT_FONT);

            label.setFont(DEFAULT_FONT);
            panel.add(label);
            panel.add(_selected[i]);
        }

        JPanel buttonPanel = new JPanel();
        JButton ok = new JButton("Ok");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                _gui.setEnabled(true);
                updatePlayers();
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                _gui.setEnabled(true);
            }
        });
        buttonPanel.add(cancel);
        buttonPanel.add(ok);

        _contentPane.add(panel);
        _contentPane.add(buttonPanel);
    }

    private void updatePlayers(){
        try{
            IPlayer[] players = new IPlayer[4];
            for(int i = 0; i < _selected.length; i++){
                Type type = (Type) _selected[i].getSelectedItem();
                players[i] = Resolver.ResolvePlayer(type.getType());
            }
            _gui.SetPlayers(players);
            _gui.NewGame();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class Type{
        private Class _type;
        private String _desc;


        private Type(String desc, Class type){
            _desc = desc;
            _type = type;
        }

        private String getDesc(){return _desc;}

        private Class getType(){return _type;}

        private void setDesc(String desc){_desc = desc;}

        private void setType(Class type){_type = type;}

        public @Override String toString(){return _desc;}
    }

    public void createAndShowGui(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000,500));
        setContentPane(_contentPane);
        pack();
        setVisible(true);
        setResizable(false);
    }
}
