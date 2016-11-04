package gui;

import card.Card;
import game.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel extends JPanel {
    private AbstractPlayer _player;
    private boolean _humanPlayer;
    private List<CardImage> handLabel;

    public PlayerPanel(AbstractPlayer player, int orientation){
        _player = player;
        setLayout(new BoxLayout(this, orientation));

        handLabel = new ArrayList<>();
        _humanPlayer = false;

    }


    public AbstractPlayer Player(){return _player;}

    public void SwapCards(Card remove, Card replace) throws IOException {
        int index = handLabel.indexOf(CardImage.Card(remove));
        handLabel.remove(index);
        handLabel.add(index, CardImage.Card(replace));
        revalidate();
        repaint();

    }

    public void AddCard(Card card) throws IOException {
        CardImage image = CardImage.Card(card);
        handLabel.add(image);
        add(image);

        revalidate();
        repaint();

    }

    public void DeleteCard(Card card) throws IOException {
        CardImage image = CardImage.Card(card);
        handLabel.remove(image);
        remove(image);

        revalidate();
        repaint();
    }

    public void DrawHand(List<Card> hand) throws IOException {
        removeAll();
        for(int i = 0; i < hand.size(); i++){
            handLabel.add(CardImage.Card(hand.get(i)));
            add(handLabel.get(i));
        }

        revalidate();
        repaint();
    }

}
