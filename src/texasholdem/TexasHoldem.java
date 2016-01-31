/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class TexasHoldem extends JPanel {

    public static JFrame frame;
    public static TexasHoldem game;
    public static JLayeredPane layer;
    public static Card[] Deck;
    public static Person[] players;
    public static int numPlayers;
    public static BufferedImage image;
    public static String path;

    private static final int CLUB = 0, SPADE = 1, HEART = 2, DIAMOND = 3;
    private static final int DECK_LENGTH = 104;
    private static final int CARD_WIDTH = 59, CARD_HEIGHT = 80;
    private static final int FRAME_WIDTH = 400, FRAME_HEIGHT = 300;

    private static final String dir = System.getProperty("user.dir") + "\\src\\texasholdem\\sprites\\";
    private static final String[] DECK_MAP = new String[]{
        dir + "ace_club.jpg", dir + "ace_spade.jpg", dir + "ace_heart.jpg", dir + "ace_diamond.jpg",
        dir + "two_club.jpg", dir + "two_spade.jpg", dir + "two_heart.jpg", dir + "two_diamond.jpg",
        dir + "three_club.jpg", dir + "three_spade.jpg", dir + "three_heart.jpg", dir + "three_diamond.jpg",
        dir + "four_club.jpg", dir + "four_spade.jpg", dir + "four_heart.jpg", dir + "four_diamond.jpg",
        dir + "five_club.jpg", dir + "five_spade.jpg", dir + "five_heart.jpg", dir + "five_diamond.jpg",
        dir + "six_club.jpg", dir + "six_spade.jpg", dir + "six_heart.jpg", dir + "six_diamond.jpg",
        dir + "seven_club.jpg", dir + "seven_spade.jpg", dir + "seven_heart.jpg", dir + "seven_diamond.jpg",
        dir + "eight_club.jpg", dir + "eight_spade.jpg", dir + "eight_heart.jpg", dir + "eight_diamond.jpg",
        dir + "nine_club.jpg", dir + "nine_spade.jpg", dir + "nine_heart.jpg", dir + "nine_diamond.jpg",
        dir + "ten_club.jpg", dir + "ten_spade.jpg", dir + "ten_heart.jpg", dir + "ten_diamond.jpg",
        dir + "jack_club.jpg", dir + "jack_spade.jpg", dir + "jack_heart.jpg", dir + "jack_diamond.jpg",
        dir + "queen_club.jpg", dir + "queen_spade.jpg", dir + "queen_heart.jpg", dir + "queen_diamond.jpg",
        dir + "king_club.jpg", dir + "king_spade.jpg", dir + "king_heart.jpg", dir + "king_diamond.jpg",
        dir + "ace_club.jpg", dir + "ace_spade.jpg", dir + "ace_heart.jpg", dir + "ace_diamond.jpg",
        dir + "two_club.jpg", dir + "two_spade.jpg", dir + "two_heart.jpg", dir + "two_diamond.jpg",
        dir + "three_club.jpg", dir + "three_spade.jpg", dir + "three_heart.jpg", dir + "three_diamond.jpg",
        dir + "four_club.jpg", dir + "four_spade.jpg", dir + "four_heart.jpg", dir + "four_diamond.jpg",
        dir + "five_club.jpg", dir + "five_spade.jpg", dir + "five_heart.jpg", dir + "five_diamond.jpg",
        dir + "six_club.jpg", dir + "six_spade.jpg", dir + "six_heart.jpg", dir + "six_diamond.jpg",
        dir + "seven_club.jpg", dir + "seven_spade.jpg", dir + "seven_heart.jpg", dir + "seven_diamond.jpg",
        dir + "eight_club.jpg", dir + "eight_spade.jpg", dir + "eight_heart.jpg", dir + "eight_diamond.jpg",
        dir + "nine_club.jpg", dir + "nine_spade.jpg", dir + "nine_heart.jpg", dir + "nine_diamond.jpg",
        dir + "ten_club.jpg", dir + "ten_spade.jpg", dir + "ten_heart.jpg", dir + "ten_diamond.jpg",
        dir + "jack_club.jpg", dir + "jack_spade.jpg", dir + "jack_heart.jpg", dir + "jack_diamond.jpg",
        dir + "queen_club.jpg", dir + "queen_spade.jpg", dir + "queen_heart.jpg", dir + "queen_diamond.jpg",
        dir + "king_club.jpg", dir + "king_spade.jpg", dir + "king_heart.jpg", dir + "king_diamond.jpg"};

    public static void main(String[] args) {
        init();
        /*
        int i = 0;
        for (i = 0; i < Deck.length; i++) {
            System.out.println("ID: " + Deck[i].id+ " SUIT: " + Deck[i].suit + " VALUE: " + Deck[i].value);
        }
        System.out.println(DECK_MAP[60]);
         //*/
        play();
    }

    public static void play() {


        while (true) {
            deal();
            //placeBets();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(TexasHoldem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void initPlayers(int size, int chips) {
        players[0] = new Dealer();
        int i = 1;
        for (i = 1; i < size; i++) {
            players[i] = new Player(chips, 0, 0);
        }

    }

    public static void deal() {
        int i = 0, one = 0, two = 0;
        Random rand = new Random();
        while (i < numPlayers) {
            one = rand.nextInt(DECK_LENGTH);
            while (Deck[one].dealt) {
                one = rand.nextInt(DECK_LENGTH);
            }
            Deck[one].dealt = true;
            two = rand.nextInt(DECK_LENGTH);
            while (Deck[two].dealt) {
                two = rand.nextInt(DECK_LENGTH);
            }
            Deck[two].dealt = true;
            players[i].hand[0] = Deck[one];
            players[i].hand[1] = Deck[two];
            //System.out.println(players[i].hand[0].id + " " + players[i].hand[1].id);
            i++;
        }
    }

    public static void init() {
        frame = new JFrame("Texas Hold'em");
        game = new TexasHoldem();
        layer = new JLayeredPane();
        layer.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        layer.add(game);
        frame.add(layer);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        path = System.getProperty("user.dir") + "\\src\\texasholdem\\sprites\\table.jpg";
        frame.getContentPane().add(initImage(path, 0, 0, FRAME_WIDTH, FRAME_HEIGHT));
        frame.pack();
        //USEFUL-- loads a card on top
        path = System.getProperty("user.dir") + "\\src\\texasholdem\\sprites\\card_back.jpg";
        frame.getContentPane().add(initImage(path, 325, 1, CARD_WIDTH, CARD_HEIGHT), 1);
        frame.setLocation(200, 200);
        frame.setVisible(true);
    }

    public static void initDeck() {
        Deck = new Card[104];
        int i, s = 0, v = 1;
        for (i = 0; i < DECK_LENGTH; i++) {
            if (i % 52 == 0) {
                v = 1;
            }
            Deck[i] = new Card(i, s, v);
            s = (i + 1) % 4;
            if (s == 0) {
                v++;
            }
        }
    }

    public static JLabel initImage(String path, int x, int y, int w, int h) {
        File file = new File(path);
        //BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(TexasHoldem.class.getName()).log(Level.SEVERE, null, ex);
        }
//        JLabel ret = new JLabel(new ImageIcon(image));
//        ret.repaint();
//        return ret;
        return new JLabel(new ImageIcon(image));
    }

    public TexasHoldem() {
        numPlayers = 2;
        players = new Person[numPlayers];
        initPlayers(2, 1000);
        initDeck();
    }

    public TexasHoldem(int size) {
        numPlayers = size;
        players = new Person[numPlayers];
        initPlayers(numPlayers, 1000);
        initDeck();
    }
}
