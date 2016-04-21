
package texasholdem;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import javax.imageio.ImageIO;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.*;

import static texasholdem.TexasHoldem.*;


public class startGUI extends JPanel implements ActionListener{
    
    Image image;
    String path;
    ImageIcon i;
    String cardName;
    
    //draws the table
    public void createTable(Graphics g) {
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/table6.jpg"));
        image = i.getImage();
        g.drawImage(image, 0, 0, /*WIDTH*/ FRAME_WIDTH, /*HEIGHT*/ FRAME_HEIGHT, this);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //draws the table
        createTable(g);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
    }
}
