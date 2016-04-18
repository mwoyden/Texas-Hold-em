package texasholdem;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.*;
import javax.swing.JPanel;
import java.awt.image.*;
import static java.lang.Thread.sleep;
import javax.swing.*;
import static texasholdem.GUI.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TexasHoldem extends JFrame {

    //Global variables
    public static JFrame jf;
    public static GUI gui;
    public static TexasHoldem game;
    public static Card[] deck, board;
    public static Person[] players;
    public static BufferedImage image;
    public static String path;
    public static int pot, round, currentBet, smallBlind, bigBlind;
    public static boolean called = false, folded = false;

    public static JLabel cpu1, cpu2, cpu3, cpu4, player;

    //Final variables
    private static final int DECK_LENGTH = 104;
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 600;
    private static final int NUM_PLAYERS = 5;
    private static final int SB_BET = 50, BB_BET = 100;
    private static final int TEXT_WIDTH = 100, TEXT_HEIGHT = 30;

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

    /**
     * Main
     *
     * @param args
     */
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
        round = 1; //Set the round to pre-flop
        while (true) { //Loop until the game exits
            System.out.println("ROUND: " + round);
            called = false;
            folded = false;
            switch (round) {
                case 1: //Before the flop
                    deal(); //Deal the hole
                    placeBets();
                    break;
                case 2: //After the flop
                    flop(); //Deal the flop
                    placeBets();
                    break;
                case 3: //The turn
                    turn(); //Deal the turn
                    placeBets();
                    break;
                case 4: //The river
                    river(); //Deal the river
                    placeBets();
                    if (pot != 0) { //Catches if someone folds after the river
                        checkWinner(); //Award the pot to the highest hand
                    }
                    break;
                default: //Error catching
                    reset();
                    break;
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
            players[i].evaluate(board, round);
            hands[i] = players[i].hand;
        }
        int winners = 1;
        //Finds if there is a tie
        Set<Integer> dups = new HashSet();
        for (int h : hands) {
            if (dups.contains(h)) { //If there is a duplicate
                winners++; //Increment the amount of dinners
            }
            dups.add(h);
        }
        int winner = 0, max = 0;
        //Finds the value of the highest hand based on logic in "person" class
        for (i = 0; i < NUM_PLAYERS; i++) {
            if (hands[i] > max) {
                System.out.println("PLAYER " + i + "'S HAND IS " + hands[i]);
                max = hands[i];
                winner = i;
            }
        }
        //If no tie, award pot to winner
        if (winners == 1) {
            awardPot(winner);
        } else {
            for (i = 0; i < NUM_PLAYERS; i++) { //Removes all players who lost
                if (players[i].hand < max) {
                    players[i].status = 0;
                }
            }
            int high = 0;
            for (i = 0; i < NUM_PLAYERS; i++) { //Finds the highcard
                if (players[i].status == 1) {
                    if (players[i].highCard > high) {
                        high = players[i].highCard;
                        winner = i; //Sets the winner 
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
        smallBlind = (smallBlind + 1) % NUM_PLAYERS; //Rotate small blind
        bigBlind = (bigBlind + 1) % NUM_PLAYERS; //Rotate big blind
        pot = 0; //Reset the pot to 0
        resetGUI();
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
                p.fold(); //Ensures all hand related variables are zeroed
                p.status = 1; //Puts the players in the game
            } else {
                p.status = 0;
            }
        }
    }

    /**
     * Collects the cards
     */
    public static void resetDeck() {
        System.out.println("RESETTING DECK...");
        int i = 0;
        for (i = 0; i < DECK_LENGTH; i++) {
            deck[i].dealt = false;
        }
    }

    /**
     * Deals the flop and signals GUI
     */
    public static void flop() {
        System.out.println("FLOP...");
        int i = 0;
        board = new Card[5]; //Create the board
        while (i < 3) { //Deal 3 cards
            board[i] = new Card();
            board[i] = draw();
            i++;
        }
        setFlopDealing(); //Signal the GUI
        printBoard(2);
    }

    /**
     * Deals the turn and signals the GUI
     */
    public static void turn() {
        System.out.println("TURN...");
        board[3] = new Card();
        board[3] = draw();
        setTurnDealing();
        printBoard(3);
    }

    /**
     * Deals the river and signals the GUI
     */
    public static void river() {
        System.out.println("RIVER...");
        board[4] = new Card();
        board[4] = draw();
        setRiverDealing();
        printBoard(4);
    }

    /**
     * Creates a simple text based interface for player to play a game with the
     * CPUs
     */
    public static void takeTurn() {
        /*
        Scanner console = new Scanner(System.in);
        System.out.println("PLAYER'S TURN:");
        players[2].printInfo();
        System.out.println("Enter bet, fold, or call");
        String s = console.next();
        players[2].bet = 0;
        if (s.equalsIgnoreCase("bet")) {
            System.out.println("Enter amount to bet");
            int b = console.nextInt();
            if (b > players[2].chips || b < currentBet) {
                System.out.println("You folded. douche");
                players[2].fold();
                return;
            }
            players[2].bet(b);
            currentBet = b;
            pot += b;
        } else if (s.equalsIgnoreCase("fold")) {
            players[2].fold();
        } else if (s.equalsIgnoreCase("call")) {
            players[2].call(currentBet);
            pot += players[2].bet;
        }
        */
        while (!called && !folded) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(TexasHoldem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Makes sure that the highest bet is always the call bet
     */
    public static void checkBets() {
        int max = 0;
        for (Person p : players) {
            if (p.bet > max) {
                max = p.bet;
            }
        }
        currentBet = max;
    }

    /**
     * Makes sure the bets in previous rounds don't linger
     */
    public static void resetBets() {
        for (Person p : players) {
            p.bet = 0;
        }
    }

    /**
     * Allows all players to bet and handles a lot of CPU betting logic
     */
    public static void placeBets() {
        System.out.println("PLACING BETS IN ROUND: " + round);
        currentBet = BB_BET; //Set the minumum bet to the BB_BET (100)
        int i = smallBlind, j = 0;
        String s = ""; //String used to waitCPU steps
        while (j < NUM_PLAYERS) { //make sure each player goes
            if (j == 0) { //Accomodates small blind
                currentBet = 0;
                resetBets();
            }
            if (currentBet < BB_BET && round == 1) { //Makes sure players after BB bet the BB_BET
                currentBet = BB_BET;
            }
            checkBets();
            if (players[i].status != 0) { //If the person has not folded
                if (players[i].name.equals("Player")) { //If the person is the player
                    System.out.println("Player betting...");
                    takeTurn(); //Take your turn to decide
                    //players[i].status = 0; //folds the player for CPU simulation
                } else if (players[i].name.contains("CPU")) { //If the person is the CPU
                    s = "CPU " + i + " BETTING...";
                    waitCPU(i, s);
                    if ((currentBet = players[i].decide(round, currentBet, board, i, smallBlind, bigBlind)) == 0) { //CPU folded or checked
                        if (i == bigBlind && round == 1 && players[i].status == 1) { //If big blind checks in round 1
                            s = "BIG BLIND (CPU " + i + ") CHECKED";
                            waitCPU(i, s);
                            players[i].bet += BB_BET; //Error handles the big blind from calling later on
                            pot += currentBet; //Adds the bet to the pot
                        }
                        if (players[i].status == 1) { //If CPU checks
                            s = "CPU " + i + " CHECKED";
                            waitCPU(i, s);
                        } else { //If CPU folded
                            s = "CPU " + i + " FOLDED";
                            waitCPU(i, s);
                            if (i == smallBlind && round == 1) { //Reset the bet the BB_BET
                                currentBet = BB_BET;
                            }
                        }
                    } else { //If CPU bets in first round or raises
                        s = "CPU " + i + " BET " + currentBet;
                        waitCPU(i, s);
                        if (i == smallBlind && round == 1) { //make sure small blind doesnt double call
                            players[i].bet += SB_BET;
                        }
                        pot += currentBet; //Adds the bet to the pot
                    }
                }
            }
            if (checkPlayers()) { //If everyone folded
                return;
            }
            i = (i + 1) % NUM_PLAYERS; //Circle around the table
            j++;
        }
        j = 0;
        i = smallBlind;
        checkBets();
        while (j < NUM_PLAYERS) { //For each player
            if (players[i].status != 0) { //If they haven't folded
                System.out.println("CPU'S BET: " + players[i].bet);
                System.out.println("CURRENT BET: " + currentBet);
                if (players[i].bet < currentBet) { //If there was a raise on the table
                    if (i != 2) {
                        if (players[i].call(round, currentBet, board)) { //CPU decides to call
                            System.out.println("CPU " + i + " CALLED");
                            pot += currentBet - players[i].bet; //CPU adds new bet
                        } else {
                            players[i].fold();
                            System.out.println("DIDN'T WANT TO CALL");
                        }
                    } else { //Allow the player to call CPU raise
                        System.out.println("Do you call " + currentBet + "?");
                        Scanner console = new Scanner(System.in);
                        System.out.println("Yes/No");
                        String y = console.next();
                        if (y.equalsIgnoreCase("yes")) {
                            players[i].call(currentBet);
                            System.out.println("Player called");
                        } else {
                            System.out.println("Bitch. you folded");
                            players[i].fold();
                        }
                    }
                }
            }
            i = (i + 1) % NUM_PLAYERS; //Circle around the table
            j++;
        }
        System.out.println(pot);
    }

    /**
     * Used to postpone CPUs actions to line up with GUI
     *
     * @param i which CPU is waiting
     * @param s string indicates what step the CPU is at in placeBets()
     */
    public static void waitCPU(int i, String s) {
        System.out.println(s); //Prints the step
        try {
            sleep(500);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Prints out the cards on the board
     *
     * @param round specifies how many cards to try and print
     */
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

    /**
     * Deals the hole and forces SB and BB bets
     */
    public static void deal() {
        int i = 0;
        Random rand = new Random();
        System.out.println("DEALING...");
        if (players[2].chips > 0) {
            players[2].status = 1;
        }
        while (i < NUM_PLAYERS) { //For each player
            if (i == smallBlind) { //If the player being dealt is the small blind
                if (players[i].name.contains("CPU")) { //If players is CPU
                    System.out.println("CPU " + i + " BET SB: " + SB_BET);
                } else { //If player is you
                    System.out.println("Player BET SB: " + SB_BET);
                }
                pot += SB_BET; //add the small blind to the pot
                players[i].bet(SB_BET); //small blind bets SB_BET value
            }
            if (i == bigBlind) { //If the player being dealt is the big blind
                if (players[i].name.contains("CPU")) { //If player is CPU
                    System.out.println("CPU " + i + " BET BB: " + BB_BET);
                } else { //If player is you
                    System.out.println("Player BET BB: " + BB_BET);
                }
                pot += BB_BET; //add the big blind to the pot
                players[i].bet(BB_BET); //big blind bets BB_BET value
            }
            players[i].hole[0] = draw(); //Deal the hole
            players[i].hole[1] = draw();
            System.out.println(players[i].hole[0].id + " " + players[i].hole[1].id);
            i++;
        }
        setDealing();
    }

    /**
     * Draw randomly takes a card from the deck that hasn't already been drawn
     *
     * @return the card that was taken from the deck
     */
    public static Card draw() {
        Random rand = new Random(); //Random number object
        int card = rand.nextInt(DECK_LENGTH); //Creates a number from 0-103
        while (deck[card].dealt) { //Makes sure it doesn't deal the same card twice
            card = rand.nextInt(DECK_LENGTH);
        }
        deck[card].dealt = true; //Sets card to dealt
        return deck[card]; //Returns the card object
    }

    /**
     * Creates text
     *
     * @param name
     * @param posX
     * @param posY
     * @param sizeX
     * @param sizeY
     * @return
     */
    public static JLabel createText(String name, int posX, int posY, int sizeX, int sizeY) {
        JLabel label = new JLabel("<html>" +name + "<br></html>");

        //label.setLayout(null);
        label.setBounds(posX, posY, sizeX, sizeY);

        label.setForeground(Color.white);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);

        label.setLocation(posX, posY);

        return label;
    }

    /**
     * Initializes the GUI frame
     */
    public static void initGUI() {
        System.out.println("INITIALIZING GUI...");
        gui = new GUI();
        jf = new JFrame();

        
        final JPanel panel = (JPanel) jf.getGlassPane();
        final JButton call = new JButton("Call"); //creates call button
        final JButton fold = new JButton("Fold"); //creates fold button
        
        //creates/initializes the text, but the update/rewritten gets done in GUI.ActionPerformed
        cpu1 = createText(String.valueOf(players[0].chips), 680, 70, TEXT_WIDTH, TEXT_HEIGHT);
        cpu2 = createText(String.valueOf(players[1].chips), 680, 460, TEXT_WIDTH, TEXT_HEIGHT);
        player = createText(String.valueOf(players[2].chips), 360, 530, TEXT_WIDTH, TEXT_HEIGHT);
        cpu3 = createText(String.valueOf(players[3].chips), 70, 460, TEXT_WIDTH, TEXT_HEIGHT);
        cpu4 = createText(String.valueOf(players[4].chips), 70, 70, TEXT_WIDTH, TEXT_HEIGHT);

        //location for call and fold button
        call.setBounds(275, 510, 59 , 25);
        call.setLocation(275, 510);
        fold.setBounds(275, 480, 59 , 25);
        fold.setLocation(275, 480);
        
        call.setLayout(null);
        panel.setLayout(null);

        panel.setBounds(360, 550, 60, 25);

        //adds table and deck spirte
        jf.add(gui);

        panel.setVisible(true);

        //add cpu's labels
        panel.add(cpu1);
        panel.add(cpu2);
        panel.add(cpu3);
        panel.add(cpu4);
        panel.add(player);
        panel.add(call);
        panel.add(fold);

        //set the action when the call button is pressed
        call.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players[2].call(currentBet);
                called = true;

            }
        });
        
        //set the action when the fold button is pressed
        fold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players[2].fold();
                folded = true;
            }
        });


        jf.setTitle("Texas Hold'em");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        jf.setVisible(true);

        //wait 1 seconds to deal the flop face up
        sleepGUI(1000);

    }

    /**
     * Initializes the game by calling the constructor
     */
    public static void init() {
        System.out.println("INITIALIZING GAME...");
        game = new TexasHoldem(); //Calls constructor
    }

    /**
     * Initializes all the players and the small blind to 0 and big blind to 1
     *
     * @param chips is the starting chip value for each player
     */
    public static void initPlayers(int chips) {
        System.out.println("INITIALIZING PLAYERS...");
        players = new Person[NUM_PLAYERS]; //Initializes the players
        players[0] = new CPU("CPU 0", chips, 1, 1); //Args are name, chip count, xpos, ypos
        players[1] = new CPU("CPU 1", chips, 2, 2);
        players[2] = new Player("Player", chips, 0, 0);
        players[3] = new CPU("CPU 3", chips, 3, 3);
        players[4] = new CPU("CPU 4", chips, 4, 4);
        smallBlind = 0; //CPU 0 starts as small blind
        bigBlind = 1; //CPU 1 starts as big blind
    }

    /**
     * Initializes the deck with a length of two deck lengths Puts in the ID,
     * suit, and value for each card
     */
    public static void initDeck() {
        System.out.println("INITIALIZING DECK...");
        deck = new Card[DECK_LENGTH]; //Create the deck object
        int i, s = 0, v = 1; //loop variable (also id), suit, card value
        for (i = 0; i < DECK_LENGTH; i++) { //Loops through the deck and creates each card
            if (i % 52 == 0) { //Reset value to Ace for second 52 cards
                v = 1;
            }
            deck[i] = new Card(i, s, v); //Create the card with the id, suit, and value
            s = (i + 1) % 4; //Resets the suit
            if (s == 0) { //Increments the card value once each card of that suit is created
                v++;
            }
        }
    }

    /**
     * Constructor called by init(), initializes the deck and the players
     */
    public TexasHoldem() {
        initPlayers(1000);
        initDeck();
        initGUI();
    }
}
