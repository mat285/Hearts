package gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.util.List;
import java.util.Vector;

/**
 * TBD
 * <p>
 * User: elee
 * Date: 11/5/2016
 * Time: 5:55 PM
 */
public class ScorePanel extends JPanel{
    private JTable _table;
    private int _roundNum = 1;
    private Vector<String> _colNames;
    private Vector<Vector> _rowData;

    public ScorePanel(String[] names){
        _colNames = new Vector<>();
        _colNames.add("Round");

        for(String name : names){
            _colNames.add(name);
        }

        _rowData = new Vector<>();

        _table = new JTable(_rowData, _colNames);
        add(new JScrollPane(_table));
    }

    public <T> void AddRoundScores(T round, int[] scores){
        Vector row = new Vector<>();
        row.add(round);
        for(int score : scores){
            row.add(score);
        }
        _rowData.add(row);
        revalidate();
        repaint();
    }

    public void ClearScores(){
        _rowData = new Vector<>();
    }


}
