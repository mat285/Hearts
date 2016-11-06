package gui;

import card.Card;
import card.Suit;
import card.Trick;
import card.Value;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * TBD
 * <p/>
 * User: elee
 * Date: 10/14/2016
 * Time: 12:38 PM
 */
public class TrickPanel extends JPanel{
    private Trick _trick;
    private CardImage[] _cards;

    public TrickPanel(){
        super();
        _cards = new CardImage[4];
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Color.CYAN));
    }

    public void Update(Trick trick) throws Exception {
        removeAll();
        _trick = trick;

        //make this the starting player's index
        int i = 0;
        _cards[i] = CardImage.Card(_trick.First());
        i = (i+1) % 4;
        _cards[i] = CardImage.Card(_trick.Second());
        i = (i+1) % 4;
        _cards[i] = CardImage.Card(_trick.Third());
        i = (i+1) % 4;
        _cards[i] = CardImage.Card(_trick.Fourth());

        add(_cards[0], 2, 1);
        add(_cards[1], 1, 0);
        add(_cards[2], 0, 1);
        add(_cards[3], 1, 2);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        add(new JLabel("Adding " + trick.LastCardAdded()), c);


        setPreferredSize(getPreferredSize());
        revalidate();
        repaint();
    }

    private void add(CardImage card, int x, int y){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        add(card, c);
    }


}
