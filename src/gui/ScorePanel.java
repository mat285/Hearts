package gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
    private Vector<String> _colNames;
    private Vector<Vector> _rowData;
    private DefaultTableModel _model;

    public ScorePanel(String[] names){
        _colNames = new Vector<>();
        _colNames.add("Round");

        for(String name : names){
            _colNames.add(name);
        }

        _rowData = new Vector<>();
        _model = new DefaultTableModel(_rowData, _colNames);

        _table = new JTable(_model);
        JScrollPane pane = new JScrollPane(_table);
        //pane.setPreferredSize(new Dimension(1000,1000));
        add(pane);
        setOpaque(true);

        _table.getTableHeader().setFont(HeartsFrame.DEFAULT_FONT);
        _table.setFont(HeartsFrame.DEFAULT_FONT);
        _table.setRowHeight(40);
    }

    public <T> void AddRoundScores(T round, int[] scores){
        Vector row = new Vector<>();
        row.add(round);
        for(int score : scores){
            row.add(score);
        }
        _rowData.add(row);
        _model.addRow(row);
        _model.fireTableDataChanged();

        revalidate();
        repaint();
    }

    public void ClearScores(){
        _rowData = new Vector<>();
        revalidate();
        repaint();
    }
}
