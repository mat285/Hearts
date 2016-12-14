package gui;

import game.IPlayer;
import main.Resolver;
import main.Players;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class ChoosePlayerFrame extends JFrame{
    private HeartsFrame _gui;
    private JComboBox[] _selected = new JComboBox[4];
    private JPanel _contentPane;


    public ChoosePlayerFrame(HeartsFrame gui){
        _gui = gui;
        _contentPane = new JPanel();

        _contentPane.setLayout(new BoxLayout(_contentPane, BoxLayout.Y_AXIS));
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
            _selected[i].setFont(HeartsFrame.DEFAULT_FONT);
            label.setForeground(Color.WHITE);

            label.setFont(HeartsFrame.DEFAULT_FONT);
            panel.add(label);
            panel.add(_selected[i]);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(HeartsFrame.BACKGROUND);

        JButton ok = new JButton("Ok");
        JButton cancel = new JButton("Cancel");

        ok.setFont(HeartsFrame.DEFAULT_FONT);
        cancel.setFont(HeartsFrame.DEFAULT_FONT);

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

        panel.setBackground(HeartsFrame.BACKGROUND);

        _contentPane.add(panel);
        _contentPane.add(Box.createRigidArea(new Dimension(0,10)));
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
        _contentPane.setBackground(HeartsFrame.BACKGROUND);
        setContentPane(_contentPane);
        pack();
        setVisible(true);
        setResizable(false);
    }
}
