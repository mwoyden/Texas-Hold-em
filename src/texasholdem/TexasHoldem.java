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
import static java.lang.Thread.sleep;
import javax.imageio.*;
import javax.swing.*;
import static texasholdem.GUI.*;

public class TexasHoldem extends JPanel {

    //Global variables
    public static JFrame jf;
    public static GUI test;
    public static TexasHoldem game;
    public static Card[] deck, board;
    public static Person[] players;
    public static BufferedImage image;
    public static String path;
    public static boolean won = false;
    public static int pot, round, currentBet, smallBlind, bigBlind;

    //Final variables
    private static final int CLUB = 0, SPADE = 1, HEART = 2, DIAMOND = 3;
    private static final int DECK_LENGTH = 104;
    private static final int CARD_WIDTH = 59, CARD_HEIGHT = 80;
    private static final int FRAME_WIDTH = 400, FRAME_HEIGHT = 300;
    private static final int NUM_PLAYERS = 5;
    private static final int SB_BET = 50, BB_BET = 100;

    //Deck Map is used to map values in program to sprites in the GUI
    private static final String dir = System.getProperty("user.dir") + "/src/texasholdem/sprites/";
    public static final String[] DECK_MAP = new String[]{
        dir + "ace_clubs.jpg", dir + "ace_spades.jpg", dir + "ace_hearts.jpg", dir + "ace_diamonds.jpg",
        dir + "two_clubs.jpg", dir + "two_spades.jpg", dir + "two_hearts.jpg", dir + "two_diamonds.jpg",
        dir + "three_clubs.jpg", dir + "three_spades.jpg", dir + "three_hearts.jpg", dir + "three_diamonds.jpg",
        dir + "four_clubs.jpg", dir + "four_spades.jpg", dir + "four_hearts.jpg", dir + "four_diamonds.jpg",
        dir + "five_clubs.jpg", dir + "five_spades.jpg", dir + "five_hearts.jpg", dir + "five_diamonds.jpg",
        dir + "six_clubs.jpg", dir + "six_spades.jpg", dir + "six_hearts.jpg", dir + "six_diamonds.jpg",
        dir + "seven_clubs.jpg", dir + "seven_spades.jpg", dir + "seven_hearts.jpg", dir + "seven_diamonds.jpg",
        dir + "eight_clubs.jpg", dir + "eight_spades.jpg", dir + "eight_hearts.jpg", dir + "eight_diamonds.jpg",
        dir + "nine_clubs.jpg", dir + "nine_spades.jpg", dir + "nine_hearts.jpg", dir + "nine_diamonds.jpg",
        dir + "ten_clubs.jpg", dir + "ten_spades.jpg", dir + "ten_hearts.jpg", dir + "ten_diamonds.jpg",
        dir + "jack_clubs.jpg", dir + "jack_spades.jpg", dir + "jack_hearts.jpg", dir + "jack_diamonds.jpg",
        dir + "queen_clubs.jpg", dir + "queen_spades.jpg", dir + "queen_hearts.jpg", dir + "queen_diamonds.jpg",
        dir + "king_clubs.jpg", dir + "king_spades.jpg", dir + "king_hearts.jpg", dir + "king_diamonds.jpg",
        dir + "ace_clubs.jpg", dir + "ace_spades.jpg", dir + "ace_hearts.jpg", dir + "ace_diamonds.jpg",
        dir + "two_clubs.jpg", dir + "two_spades.jpg", dir + "two_hearts.jpg", dir + "two_diamonds.jpg",
        dir + "three_clubs.jpg", dir + "three_spades.jpg", dir + "three_hearts.jpg", dir + "three_diamonds.jpg",
        dir + "four_clubs.jpg", dir + "four_spades.jpg", dir + "four_hearts.jpg", dir + "four_diamonds.jpg",
        dir + "five_clubs.jpg", dir + "five_spades.jpg", dir + "five_hearts.jpg", dir + "five_diamonds.jpg",
        dir + "six_clubs.jpg", dir + "six_spades.jpg", dir + "six_hearts.jpg", dir + "six_diamonds.jpg",
        dir + "seven_clubs.jpg", dir + "seven_spades.jpg", dir + "seven_hearts.jpg", dir + "seven_diamonds.jpg",
        dir + "eight_clubs.jpg", dir + "eight_spades.jpg", dir + "eight_hearts.jpg", dir + "eight_diamonds.jpg",
        dir + "nine_clubs.jpg", dir + "nine_spades.jpg", dir + "nine_hearts.jpg", dir + "nine_diamonds.jpg",
        dir + "ten_clubs.jpg", dir + "ten_spades.jpg", dir + "ten_hearts.jpg", dir + "ten_diamonds.jpg",
        dir + "jack_clubs.jpg", dir + "jack_spades.jpg", dir + "jack_hearts.jpg", dir + "jack_diamonds.jpg",
        dir + "queen_clubs.jpg", dir + "queen_spades.jpg", dir + "queen_hearts.jpg", dir + "queen_diamonds.jpg",
        dir + "king_clubs.jpg", dir + "king_spades.jpg", dir + "king_hearts.jpg", dir + "king_diamonds.jpg"};

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

    /**
     * Sleeps the GUI animation
     *
     * @param time in ms for GUI to sleep
     */
    public static void sleepGUI(int time) {
        try {
            sleep(time);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Main game loop that controls the flow of the game including betting,
     * flop, turn, river, and game control
     */
    public static void play() {
        round = 1;
        while (true) {
            System.out.println("ROUND: " + round);
            switch (round) {
                case 1:
                    deal();
                    placeBets();
                    break;
                case 2:
                    flop();
                    placeBets();
                    break;
                case 3:
                    turn();
                    placeBets();
                    break;
                case 4:
                    river();
                    //initGUI();
                    placeBets();
                    checkWinner();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TexasHoldem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //initGUI();
                    //return;
                    break;

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

    /**
     * Checks to see who has the highest hand at the end of each game
     */
    public static void checkWinner() {
        int[] hands = new int[5];
        int i = 0;
        //Evaluates the CPUs' final hands
        for (i = 0; i < NUM_PLAYERS; i++) {
            if (i != 2) {
                players[i].evaluate(board);
                hands[i] = players[i].hand;
            } else {
                //EVALUATE PLAYER HAND
            }
        }
        int winners = 1;
        //Finds if there is a tie
        Set<Integer> dups = new HashSet();
        for (int h : hands) {
            if (dups.contains(h)) {
                winners++;
            }
            dups.add(h);
        }
        int winner = 0, max = 0;
        //Finds the value of the highest hand based on logic in "person" class
        for (i = 0; i < NUM_PLAYERS; i++) {
            if (hands[i] > max) {
                max = hands[i];
                winner = i;
            }
        }
        //If no tie, award pot to winner
        if (winners == 1) {
            awardPot(winner); //Doesn't account for the players hand
        } else {
            for (i = 0; i < NUM_PLAYERS; i++) {
                if (players[i].hand < max) {
                    players[i].status = 0;
                }
            }
            int high = 0;
            for (i = 0; i < NUM_PLAYERS; i++) {
                if (players[i].status == 1) {
                    if (players[i].highCard > high) {
                        high = players[i].highCard;
                        winner = i;
                    }
                }
            }
            awardPot(winner);
        }
    }

    /**
     * Called by placeBets(), checks to see if there is a winner before the game
     * has concluded all of the rounds of betting
     *
     * @return true it there is one player left, false otherwise
     */
    public static boolean checkPlayers() {
        int in = 0, i = 0, winner = 0;
        for (i = 0; i < NUM_PLAYERS; i++) {
            if (players[i].status == 1) {
                winner = i;
                in++;
            }
        }
        if (in == 1) {
            awardPot(winner);
            return true;
        }
        return false;
    }

    /**
     * Awards the pot to the winning player
     *
     * @param winner index in players[] of the winner
     */
    public static void awardPot(int winner) {
        players[winner].chips += pot;
        System.out.println("PLAYER " + winner + " WINS!!!");
        reset();
    }

    /**
     * Moves the blind, resets the round, and the players and collects all the
     * cards
     */
    public static void reset() {
        System.out.println("RESETTING CARDS...");
        round = 0;
        smallBlind = (smallBlind + 1) % NUM_PLAYERS;
        bigBlind = (bigBlind + 1) % NUM_PLAYERS;
        pot = 0;
        resetPlayers();
        resetDeck();
    }

    /**
     * Resets all of the players variables and puts them back in the game if
     * their chip count is non-zero
     */
    public static void resetPlayers() {
        System.out.println("RESETTING PLAYERS...");
        for (Person p : players) {
            if (p.chips > 0) {
                p.fold();
                p.status = 1;

            }
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
        printBoard(2);
    }

    public static void turn() {
        System.out.println("TURN...");
        board[3] = new Card();
        board[3] = draw();
        printBoard(3);
    }

    public static void river() {
        System.out.println("RIVER...");
        board[4] = new Card();
        board[4] = draw();
        printBoard(4);
    }

    public static void placeBets() {
        System.out.println("PLACING BETS IN ROUND: " + round);
        currentBet = BB_BET;
        int oldBet = currentBet, i = smallBlind, j = 0;
        while (j < NUM_PLAYERS) { //make sure each player goes
            //System.out.println("=========CURRENT BET 1: " + currentBet + "==============");
            if (j == 0) {
                currentBet = 0;
            }
            //System.out.println("=========CURRENT BET 2: " + currentBet + "==============");
            if (currentBet < BB_BET && round == 1) {
                currentBet = BB_BET;
            }
            //oldBet = currentBet;
            //System.out.println("=========CURRENT BET 3: " + currentBet + "==============");
            if (players[i].status != 0) {
                if (players[i].name.equals("Player")) {
                    //turn();
                    players[i].status = 0;
                    System.out.println("Player betting...");
                } else if (players[i].name.contains("CPU")) {
                    System.out.println("CPU " + i + " BETTING...");
                    if ((currentBet = players[i].decide(round, currentBet, board, i, smallBlind, bigBlind)) == 0) { //CPU folded or checked
                        //System.out.println("=========CURRENT BET 4: " + currentBet + "==============");
                        if (i == bigBlind && round == 1 && players[i].status == 1) { //If big blind checks in round 1
                            System.out.println("BIG BLIND (CPU " + i + ") CHECKED");
                            players[i].bet += BB_BET;
                            pot += currentBet;
                            //oldBet = currentBet;
                        }

                        //System.out.println("=========CURRENT BET 5: " + currentBet + "==============");
                        if (players[i].status == 1) {
                            System.out.println("CPU " + i + " CHECKED");
                        } else {
                            System.out.println("CPU " + i + " FOLDED");
                            if (i == smallBlind && round == 1) {
                                currentBet = BB_BET;
                            } else {
                                //currentBet = oldBet;
                            }
                            //System.out.println("=========CURRENT BET 6: " + currentBet + "==============");
                        }
                    } else {
                        System.out.println("CPU " + i + " BET " + currentBet);
                        if (i == smallBlind && round == 1) { //make sure small blind doesnt double call
                            players[i].bet += SB_BET;
                        }
                        pot += currentBet;
                        //oldBet = currentBet;
                    }
                }
            }
            if (checkPlayers()) {
                return;
            }
            i = (i + 1) % NUM_PLAYERS;
            j++;
        }
        j = 0;
        i = smallBlind;
        while (j < NUM_PLAYERS) {
            if (players[i].status != 0) {
                if (players[i].bet < currentBet) {
                    if (players[i].call(round, currentBet, board)) {
                        System.out.println("CPU " + i + " CALLED");
                        pot -= players[i].bet;
                        pot += currentBet;
                    }
                }
            }
            i = (i + 1) % NUM_PLAYERS;
            j++;
        }
        System.out.println(pot);
    }

    public static void printBoard(int round) {
        System.out.println("BOARD:");
        String s = "";
        int i = 0;
        for (i = 0; i <= round; i++) {
            int st = board[i].suit;
            int v = board[i].value;
            switch (st) {
                case 0:
                    s = "Clubs";
                    break;
                case 1:
                    s = "Spades";
                    break;
                case 2:
                    s = "Hearts";
                    break;
                case 3:
                    s = "Diamonds";
                    break;
                default:
                    break;
            }
            System.out.println(v + " of " + s);
        }
    }

    public static void deal() {
        int i = 0;
        Random rand = new Random();
        System.out.println("DEALING...");
        while (i < NUM_PLAYERS) {
            if (i == smallBlind) { //If the player being dealt is the small blind
                if (players[i].name.contains("CPU")) {
                    System.out.println("CPU " + i + " BET SB: " + SB_BET);
                } else {
                    System.out.println("Player BET SB: " + SB_BET);
                }
                pot += SB_BET; //add the small blind to the pot
                players[i].bet(SB_BET); //small blind bets SB_BET value
            }
            if (i == bigBlind) { //If the player being dealt is the big blind
                if (players[i].name.contains("CPU")) {
                    System.out.println("CPU " + i + " BET BB: " + BB_BET);
                } else {
                    System.out.println("Player BET BB: " + BB_BET);
                }
                pot += BB_BET; //add the big blind to the pot
                players[i].bet(BB_BET); //big blind bets BB_BET value
            }
            players[i].hole[0] = draw();
            players[i].hole[1] = draw();
            System.out.println(players[i].hole[0].id + " " + players[i].hole[1].id);
            i++;
        }
    }

    /**
     * Draw randomly takes a card from the deck that hasn't already been drawn
     *
     * @return the card that was taken from the deck
     */
    public static Card draw() {
        Random rand = new Random();
        int card = rand.nextInt(DECK_LENGTH);
        while (deck[card].dealt) {
            card = rand.nextInt(DECK_LENGTH);
        }
        deck[card].dealt = true;
        return deck[card];
    }

    /**
     * Initializes the GUI frame and handles card animation
     */
    public static void initGUI() {
        test = new GUI();

        jf = new JFrame();
        jf.setTitle("Texas Hold'em");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //adds table and first card
        jf.add(test);
        jf.setSize(600, 400);
        jf.setVisible(true);

        //wait 1.5 seconds to deal the flop face down
        sleepGUI(3000);
        setFlopDealing();

        //wait 1.5 seconds to deal the turn
        sleepGUI(1000);
        setTurnDealing();

        //wait 1.5 seconds to deal the turn
        sleepGUI(1000);
        setRiverDealing();
    }

    /**
     * Initializes the game by calling the constructor
     */
    public static void init() {
        System.out.println("INITIALIZING GAME...");
        game = new TexasHoldem();
        //initGUI();

    }

    /**
     * Initializes all the players and the small blind to 0 and big blind to 1
     *
     * @param chips is the starting chip value for each player
     */
    public static void initPlayers(int chips) {
        System.out.println("INITIALIZING PLAYERS...");
        //players[0] = new Dealer();
        players[0] = new CPU("CPU 0", chips, 1, 1, 1);
        players[1] = new CPU("CPU 1", chips, 2, 2, 2);
        players[2] = new Player("Player", chips, 0, 0, 0);
        players[3] = new CPU("CPU 3", chips, 3, 3, 0);
        players[4] = new CPU("CPU 4", chips, 4, 4, 0);
        smallBlind = 0;
        bigBlind = 1;
    }

    /**
     * Initializes the deck with a length of two deck lengths Puts in the ID,
     * suit, and value for each card
     */
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

    /**
     * Constructor called by init(), initializes the deck and the players
     */
    public TexasHoldem() {
        players = new Person[NUM_PLAYERS];
        initPlayers(10000);
        initDeck();
    }
}
