package gui;

import card.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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

    private CardImage(Card card) throws IOException {
        super();

        _card = card;
        BufferedImage image;
        File file;
        if(card == null) {
            file = new File("src/assets/cards/blank.png");
        }else{
            file = new File("src/assets/cards/" + card.toString()+".png");
        }

        image = ImageIO.read(file);
        int scaledWidth = (int) (0.35 * image.getWidth());
        int scaledHeight = (int) (0.35 * image.getHeight());

        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
        Graphics2D g2D = outputImage.createGraphics();
        g2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g2D.dispose();


        JLabel icon = new JLabel(new ImageIcon(outputImage));
        add(icon);

        setBounds(0,0,scaledWidth, scaledHeight);
        //setIcon(new ImageIcon(outputImage));

    }





    public static CardImage Card(Card card) throws IOException, CloneNotSupportedException {
        if(_emptyCard == null){ _emptyCard = new CardImage(null);}

        if(card == null){return (CardImage) _emptyCard.clone();}

        if(_images.get(card) == null){
            _images.put(card, new CardImage(card));
        }
        return _images.get(card);

    }

    public String toString(){
        if(_card == null) return "blank";
        return _card.toString();
    }




}
