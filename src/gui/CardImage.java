package gui;

import card.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardImage extends JPanel implements Cloneable{
    public static int Height;
    public static double aspectRatio;
    private static Map<Card, CardImage> _images = new HashMap<>();
    private static CardImage _emptyCard;
    private Card _card;
    private BufferedImage _original;
    private BufferedImage _image;
    private JLabel _icon;

    /**
     * Creates a new Cardimage from the given card
     * @param card
     * */
    private CardImage(Card card) throws IOException {
        super();
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

        int width = (int) (Height *aspectRatio);
        _image = new BufferedImage(width, Height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = _image.createGraphics();
        g2D.drawImage(_original, 0, 0, width, Height, null);
        g2D.dispose();

        _icon = new JLabel(new ImageIcon(_image));
        add(_icon);

        setBounds(0,0,width, Height);
        _icon.setOpaque(false);
        setOpaque(false);
    }

    public Card GetCard() { return _card; }

    public static CardImage Card(Card card) throws IOException, CloneNotSupportedException {
        if(_emptyCard == null){ _emptyCard = new CardImage(null);}

        if(card == null){return (CardImage) _emptyCard.clone();}

        _images.putIfAbsent(card, new CardImage(card));
        return _images.get(card);
    }

    public String toString(){
        if(_card == null) return "blank";
        return _card.toString();
    }

    public static void SetHeight(int h){
        Height = h;
    }

    public static int GetWidth(){
        if(aspectRatio == 0.0){
            try{
                BufferedImage i = ImageIO.read(new File("src/assets/cards/blank.png"));
                aspectRatio = (double) i.getWidth() / i.getHeight();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return (int) (Height * aspectRatio);
    }




}
