package gui;

import card.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
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
public class CardImage extends JPanel {
    private static Map<Card, CardImage> _images = new HashMap<>();
    private static CardImage _emptyCard;

    private CardImage(Card card) throws IOException {
        super();
        BufferedImage image;

        if(card == null) {
            image = ImageIO.read(this.getClass().getResource("assets/cards/blank.png"));
        }else{
            image = ImageIO.read(this.getClass().getResource("/assets/cards/"
                    + card.toString()+".png"));
        }

        JLabel icon = new JLabel(new ImageIcon(image));
        add(icon);
    }


    public static CardImage Card(Card card) throws IOException {
        if(_emptyCard == null){ _emptyCard = new CardImage(null);}

        if(card == null){return _emptyCard;}

        if(_images.get(card) == null){
            _images.put(card, new CardImage(card));
        }
        return _images.get(card);

    }




}
