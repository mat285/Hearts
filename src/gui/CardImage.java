package gui;

import card.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TBD
 * <p>
 * User: elee
 * Date: 11/3/2016
 * Time: 6:19 PM
 */
public class CardImage extends JPanel implements Cloneable{
    private static Map<Card, CardImage> _images = new HashMap<>();
    private static CardImage _emptyCard;
    private Card _card;
    private BufferedImage _original;
    private BufferedImage _image;
    private JLabel _icon;
    private static final int minHeight = 100;
    private static final int maxHeight = 300;
    private static double aspectRatio;

    private CardImage(Card card) throws IOException {
        super();
        //setMinimumSize(new Dimension(100,200));
        setLayout(new BorderLayout());

        _card = card;
        File file;
        if(card == null) {
            file = new File("src/assets/cards/blank.png");
        }else{
            file = new File("src/assets/cards/" + card.toString()+".png");
        }

        _original = ImageIO.read(file);
        aspectRatio = (double) _original.getWidth() / _original.getHeight();

        int height = 250;
        int width = (int) (height*aspectRatio);
        _image = new BufferedImage(width, height, _original.getType());
        Graphics2D g2D = _image.createGraphics();
        g2D.drawImage(_original, 0, 0, width, height, null);
        g2D.dispose();

        _icon = new JLabel(new ImageIcon(_image));
        add(_icon);

        setBounds(0,0,width, height);

    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);

        int h1 = (int) d.getHeight();
        int w1 = (int) (h1 * aspectRatio);

        int w2 = (int) d.getWidth();
        int h2 = (int) (w2 * 1 / aspectRatio);

        int width = w1;
        int height = h1;
        _image = new BufferedImage(width, height, _original.getType());
        Graphics2D g2D = _image.createGraphics();
        g2D.drawImage(_original, 0, 0, width, height, null);
        g2D.dispose();

        _icon.setIcon(new ImageIcon(_image));

        setBounds(0,0,width, height);
        repaint();
        revalidate();

    }

    public static CardImage Card(Card card) throws IOException, CloneNotSupportedException {
        if(_emptyCard == null){ _emptyCard = new CardImage(null);}

        if(card == null){return (CardImage) _emptyCard.clone();}

        _images.putIfAbsent(card, new CardImage(card));
        return _images.get(card);

    }

    public static CardImage Card(Card card, Dimension d) throws IOException, CloneNotSupportedException {
        if(_emptyCard == null){ _emptyCard = new CardImage(null);}

        if(card == null){
            CardImage c = (CardImage) _emptyCard.clone();
            c.setSize(d);
            return c;
        }

        _images.putIfAbsent(card, new CardImage(card));
        CardImage c = _images.get(card);
        c.setSize(d);
        return c;

    }

    public String toString(){
        if(_card == null) return "blank";
        return _card.toString();
    }




}
