package gui;

import card.Trick;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private BufferedImage _heartBrokenImage;
    private JLabel _heartBrokenLabel;

    public TrickPanel(){
        super();
        _cards = new CardImage[4];
        _heartBrokenLabel = new JLabel();
        setOpaque(false);
        setLayout(new GridBagLayout());
    }

    public void DisplayHeartsBroken(){
        try{
            if(_heartBrokenImage == null){
                BufferedImage image = ImageIO.read(new File("src/assets/misc/hearts_broken.png"));
                int h = CardImage.HEIGHT/2;
                int w = image.getWidth() * h / image.getHeight();
                _heartBrokenImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2D = _heartBrokenImage.createGraphics();
                g2D.drawImage(image,0,0,w,h,null);
                g2D.dispose();
                _heartBrokenLabel.setIcon(new ImageIcon(_heartBrokenImage));
            }
            _heartBrokenLabel.setVisible(true);
            revalidate();
            repaint();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void HideHeartsBroken(){
        _heartBrokenLabel.setVisible(false);
        revalidate();
        repaint();
        System.out.println("Hiding hearts");
    }

    public void Update(Trick trick, int start) throws Exception {
        removeAll();
        _trick = trick;

        //make this the starting player's index
        int i = start % 4;
        _cards[i] = CardImage.Card(_trick.First());
        i = (i+1) % 4;
        _cards[i] = CardImage.Card(_trick.Second());
        i = (i+1) % 4;
        _cards[i] = CardImage.Card(_trick.Third());
        i = (i+1) % 4;
        _cards[i] = CardImage.Card(_trick.Fourth());

        add(_cards[0], 1, 2);
        add(_cards[1], 0, 1);
        add(_cards[2], 1, 0);
        add(_cards[3], 2, 1);
        add(_heartBrokenLabel, 1, 1);

        revalidate();
        repaint();
    }


    private void add(Component component, int x, int y){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        add(component, c);
    }
}
