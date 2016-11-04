package gui;

import card.Trick;
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
        setLayout(new BorderLayout());
    }

    public void Update(Trick trick) throws IOException {
        removeAll();

        _trick = trick;
        _cards[0] = CardImage.Card(_trick.First());
        _cards[1] = CardImage.Card(_trick.Second());
        _cards[2] = CardImage.Card(_trick.Third());
        _cards[3] = CardImage.Card(_trick.Fourth());

        add(_cards[0], BorderLayout.SOUTH);
        add(_cards[1], BorderLayout.EAST);
        add(_cards[2], BorderLayout.NORTH);
        add(_cards[3], BorderLayout.WEST);

        revalidate();
        repaint();
    }



}
