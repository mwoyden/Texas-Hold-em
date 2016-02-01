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
    public static Card[] deck, board;
    public static Person[] players;
    public static BufferedImage image;
    public static String path;
    public static int pot, round, currentBet, smallBlind, bigBlind;

    private static final int CLUB = 0, SPADE = 1, HEART = 2, DIAMOND = 3;
    private static final int DECK_LENGTH = 104;
    private static final int CARD_WIDTH = 59, CARD_HEIGHT = 80;
    private static final int FRAME_WIDTH = 400, FRAME_HEIGHT = 300;
    private static final int NUM_PLAYERS = 6;
    private static final int SMALL_BLIND = 1, BIG_BLIND = 2, SB_BET = 50, BB_BET = 100;

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
        round = 1;
        while (true) {
            System.out.println("ROUND: " + round);
            switch (round) {
                case 1:
                    deal();
                    placeBets();
                    flop();
                    break;
                case 2:
                    placeBets();
                    turn();
                    break;
                case 3:
                    placeBets();
                    river();
                    break;
                case 4:
                    placeBets();
                default:
                    reset();
                    break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(TexasHoldem.class.getName()).log(Level.SEVERE, null, ex);
            }
            round++;
        }
    }

    public static void reset() {
        System.out.println("RESETTING CARDS...");
        round = 0;
        smallBlind = (smallBlind + 1) % NUM_PLAYERS;
        bigBlind = (bigBlind + 1) % NUM_PLAYERS;
        pot = 0;
        resetPlayers();
        resetDeck();
    }
    
    public static void resetPlayers() {
        System.out.println("RESETTING PLAYERS...");
        for (Person p : players) {
            p.status = 1;
        }
    }

    public static void resetDeck() {
        System.out.println("RESETTING DECK...");
        int i = 0;
        for (i = 0; i < DECK_LENGTH; i++) {
            deck[i].dealt = false;
        }
    }

    public static void flop() {
        System.out.println("FLOP...");
        int i = 0;
        board = new Card[5];
        while (i < 3) {
            board[i] = new Card();
            board[i] = draw();
            i++;
        }
    }

    public static void turn() {
        System.out.println("TURN...");
        board[3] = new Card();
        board[3] = draw();
    }

    public static void river() {
        System.out.println("RIVER...");
        board[4] = new Card();
        board[4] = draw();
    }

    public static void placeBets() {
        System.out.println("PLACING BETS IN ROUND:" + round);
        currentBet = SB_BET;
        int oldBet = currentBet, i = smallBlind, j = 0;
        while (j < NUM_PLAYERS) {
            if (players[i].status != 0) {
                if (players[i].name.equals("Player")) {
                    //turn();
                    System.out.println("Player betting...");
                } else if (players[i].name.equals("Dealer")) {
                    players[i].bet(currentBet);
                    pot += currentBet;
                } else if (players[i].name.contains("CPU")) {
                    System.out.println("CPU " + i + "BETTING...");
                    if ((currentBet = players[i].decide(round, currentBet, board)) == 0) { //CPU folded
                        System.out.println("CPU " + i + "FOLDED");
                        if (i == smallBlind) {
                            currentBet = BB_BET;
                        } else {
                            currentBet = oldBet;
                        }
                    } else {
                        System.out.println("CPU " + i + "BET");
                        pot += currentBet;
                        oldBet = currentBet;
                    }
                }
            }
            i = (i + 1) % NUM_PLAYERS;
            j++;
        }
        System.out.println(pot);
    }

    public static void deal() {
        int i = 0;
        Random rand = new Random();
        System.out.println("DAELING...");
        while (i < NUM_PLAYERS) {
            players[i].hand[0] = draw();
            players[i].hand[1] = draw();
            System.out.println(players[i].hand[0].id + " " + players[i].hand[1].id);
            i++;
        }
    }

    public static Card draw() {
        Random rand = new Random();
        int card = rand.nextInt(DECK_LENGTH);
        while (deck[card].dealt) {
            card = rand.nextInt(DECK_LENGTH);
        }
        deck[card].dealt = true;
        return deck[card];
    }

    public static void init() {
        System.out.println("INITIALIZING GAME...");
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

    public static void initPlayers(int chips) {
        System.out.println("INITIALIZING PLAYERS...");
        players[0] = new Dealer();
        players[1] = new CPU("CPU 1", chips, 1, 1, 1);
        players[2] = new CPU("CPU 2", chips, 2, 2, 2);
        players[3] = new Player("Player", chips, 0, 0, 0);
        players[4] = new CPU("CPU 3", chips, 3, 3, 0);
        players[5] = new CPU("CPU 4", chips, 4, 4, 0);
        smallBlind = 1;
        bigBlind = 2;
    }

    public static void initDeck() {
        System.out.println("INITIALIZING DECK...");
        deck = new Card[DECK_LENGTH];
        int i, s = 0, v = 1;
        for (i = 0; i < DECK_LENGTH; i++) {
            if (i % 52 == 0) {
                v = 1;
            }
            deck[i] = new Card(i, s, v);
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
        players = new Person[NUM_PLAYERS];
        initPlayers(1000);
        initDeck();
    }
}
