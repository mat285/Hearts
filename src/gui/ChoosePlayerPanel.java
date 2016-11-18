package gui;

import game.IPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * TBD
 * <p>
 * User: elee
 * Date: 11/17/2016
 * Time: 6:44 PM
 */
public class ChoosePlayerPanel extends JPanel{
    private String[] _playerTypes;
    private HeartsFrame _gui;
    private JTable _table;
    private Vector<Vector> _options;

    public ChoosePlayerPanel(HeartsFrame gui){
        _gui = gui;
        _options = new Vector<>();
        _table = new JTable(_options, null);
        _table.setShowGrid(false);
        init();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(_table);

    }

    private void init(){
        for (int i = 1; i < 5; i++){
            Vector<Object> option = new Vector<>();
            option.add("Player " + i + ":");
            option.add(new JComboBox<>(_playerTypes));
            _options.add(option);
        }
    }










}
