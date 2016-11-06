package gui;

import card.Card;
import card.Deck;
import game.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel extends JPanel {
    private IPlayer _player;
    private HandPanel _hand;
    private JLabel _name;
    private JLabel _score;

    public PlayerPanel(String name, IPlayer player, int orientation){
        _player = player;

        _name = new JLabel(name);
        _score = new JLabel("0");

        _hand = new HandPanel(orientation);

        setLayout(new BoxLayout(this, orientation));
        add(_name);
        add(_score);

    }


    public IPlayer Player(){return _player;}


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
        _score.paintImmediately(_score.getVisibleRect());
    }

    private class HandPanel extends JLayeredPane{
        private int _orientation;
        private List<CardImage> _cards;

        private HandPanel(int orientation){
            super();
            _orientation = orientation;
            _cards = new ArrayList<>();
            setBorder(BorderFactory.createLineBorder(Color.black));
            //setLayout(null);
            setLayout(new BoxLayout(this, orientation));
        }

        private void update(List<Card> hand) throws Exception {
            _cards.clear();
            removeAll();
            Deck.Sort(hand);
            Point origin = this.getLocation();

            for(int i = 0; i < hand.size(); i++){
                CardImage card = CardImage.Card(hand.get(i));
                Rectangle r = card.getBounds();

                card.setBounds(origin.x, origin.y, 100, 100);
                card.setBorder(BorderFactory.createLineBorder(Color.red, 10));
                _cards.add(card);
                add(card, new Integer(i));

                if(_orientation == BoxLayout.X_AXIS){origin.x += 1;}
                else{origin.y += 1;}

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
