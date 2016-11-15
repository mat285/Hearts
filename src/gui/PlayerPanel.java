package gui;

import card.Card;
import card.Deck;
import game.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel extends JPanel {
    private final String[] DEFAULT_NAMES = new String[]{"South","West","North","East"};
    private int _id;
    private HandPanel _hand;
    private JLabel _name;
    private JLabel _score;
    private static final Dimension VERT_DIMENSION = new Dimension(300,1000);
    private static final Dimension HORI_DIMENSION = new Dimension(1000,300);
    private static final Font DEFAULT_FONT = new Font("Serif", Font.BOLD, 20);


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

    public void DeleteCard(Card card) throws Exception{
        assert _hand.contains(card);
        _hand.DeleteCard(card);

    }

    public void UpdateScore(int score) throws Exception{
        _score.setText(score + "");
    }

    private class HandPanel extends JLayeredPane{
        private int _orientation;
        private List<CardImage> _cards;
        private Point _origin;

        private HandPanel(int orientation){
            super();
            _orientation = orientation;
            _cards = new ArrayList<>();
            _origin = this.getLocation();

            if(orientation == BoxLayout.X_AXIS){
                _origin.x += 50;
            }else{
                _origin.y += 50;
            }


            setBorder(BorderFactory.createLineBorder(Color.black));
            setLayout(new BoxLayout(this, orientation));
            setLayout(null);
        }

        private void update(List<Card> hand) throws Exception {
            _cards.clear();
            removeAll();
            Deck.Sort(hand);

            Point start = new Point(_origin);
            for(int i = 0; i < hand.size(); i++){
                CardImage card = CardImage.Card(hand.get(i));
                card.setBounds(start.x, start.y, card.getWidth(), card.getHeight()+10);
                //card.setBorder(BorderFactory.createLineBorder(Color.red, 10));
                _cards.add(card);
                add(card, new Integer(i));

                Rectangle r = card.getBounds();
                System.out.println(card + " " + r);

                if(_orientation == BoxLayout.X_AXIS){start.x += 75;}
                else{start.y += 75;}

            }
            revalidate();
            repaint();
        }

        private void AddCard(Card card) throws Exception {
            CardImage image = CardImage.Card(card);
            _cards.add(image);
            add(image);

            revalidate();
            repaint();
        }

        public void SwapCards(Card remove, Card replace) throws Exception {
            int index = _cards.indexOf(CardImage.Card(remove));
            _cards.remove(index);
            _cards.add(index, CardImage.Card(replace));
            revalidate();
            repaint();

        }

        public void DeleteCard(Card card) throws Exception {
            CardImage image = CardImage.Card(card);
            _cards.remove(image);
            remove(image);

            revalidate();
            repaint();
        }

        private boolean contains(Card card){
            return _cards.contains(card);
        }

    }

}
