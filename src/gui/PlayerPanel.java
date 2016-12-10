package gui;

import card.Card;
import card.Deck;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel extends JPanel {
    private final String[] DEFAULT_NAMES = new String[]{"South","West","North","East"};
    private int _id;
    private HandPanel _hand;
    private JLabel _name;
    private JLabel _score;

    private static final Dimension VERT_DIMENSION = new Dimension(250,1000);
    private static final Dimension HORI_DIMENSION = new Dimension(1000,250);
    private static final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 40);

    public PlayerPanel(String name, int id, int orientation){
        assert(id > 0 && id < 4);
        _id = id;
        _name = new JLabel(name);
        _score = new JLabel("0");

        _hand = new HandPanel(orientation);

        setLayout(new BoxLayout(this, orientation));
        if(orientation == BoxLayout.X_AXIS){
            setPreferredSize(HORI_DIMENSION);
        }else{
            setPreferredSize(VERT_DIMENSION);
        }

        _name.setFont(DEFAULT_FONT);
        _score.setFont(DEFAULT_FONT);
        _name.setForeground(Color.WHITE);
        _score.setForeground(Color.WHITE);
        setOpaque(false);
        add(_name);
        add(_score);
    }

    public PlayerPanel(int id, int orientation){
        this("", id, orientation);
        SetName(DEFAULT_NAMES[id]);
    }

    public int Id(){return _id;}

    public String Name(){return _name.getText();}

    public void SetName(String name){_name.setText(name);}

    public void UpdateHand(List<Card> hand) throws Exception {
        assert _hand != null;
        remove(_hand);
        _hand.update(hand);
        add(_hand);
        revalidate();
        repaint();
    }


    public void UpdateScore(int score) throws Exception{
        _score.setText(score + "");
    }

    private class HandPanel extends JLayeredPane{
        private int _orientation;
        private List<CardImage> _cards;
        private Point _origin;
        private static final int OFFSET = 75;

        private HandPanel(int orientation){
            super();
            _orientation = orientation;
            _cards = new ArrayList<>();
            _origin = this.getLocation();

            if(orientation == BoxLayout.X_AXIS){
                _origin.x += OFFSET;
            }else{
                _origin.y += OFFSET;
            }

            setLayout(null);
            setOpaque(false);
        }

        private void update(List<Card> hand) throws Exception {
            _cards.clear();
            removeAll();
            Deck.Sort(hand);

            Point start = new Point(_origin);
            for(int i = 0; i < hand.size(); i++){
                CardImage card = CardImage.Card(hand.get(i));
                card.setBounds(start.x, start.y, card.getWidth(), card.getHeight());
                _cards.add(card);
                add(card, new Integer(i));

                if(_orientation == BoxLayout.X_AXIS){start.x += OFFSET;}
                else{start.y += OFFSET;}

            }
            revalidate();
            repaint();
        }
    }

}
