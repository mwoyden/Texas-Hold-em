package texasholdem;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.*;
import javax.swing.JPanel;
import java.awt.image.*;
import static java.lang.Thread.sleep;
import javax.swing.*;
import static texasholdem.GUI.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class TexasHoldem extends JFrame {

    //Global variables
    public static JFrame jf;
    public static GUI gui;
    public static TexasHoldem game;
    public static Card[] deck, board;
    public static Person[] players;
    public static BufferedImage image;
    public static String path, s = "", POT_TEXT = "POT: ";
    public static int pot, round, currentBet, smallBlind, bigBlind, playerBet, escrow = -1;
    public static boolean called = false, folded = false, betted = false, takingTurn = false, play = false;

    public static JLabel cpu1, cpu2, cpu3, cpu4, player, currentPot, showBet; //for the cpu labels and chip count 
    public static JLabel cpu1Action, cpu2Action, cpu3Action, cpu4Action, playerAction; //for the labels of the cpu actions
    public static JLabel names, title; // names for the main menu

    //Final variables
    private static final int DECK_LENGTH = 104;
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 600;
    private static final int NUM_PLAYERS = 5;
    private static final int SB_BET = 50, BB_BET = 100;
    private static final int TEXT_WIDTH = 100, TEXT_HEIGHT = 30;

    //Deck Map is used to map values in program to sprites in the GUI
    public static final String[] DECK_MAP = new String[]{
        "ace_clubs.jpg", "ace_spades.jpg", "ace_hearts.jpg", "ace_diamonds.jpg",
        "two_clubs.jpg", "two_spades.jpg", "two_hearts.jpg", "two_diamonds.jpg",
        "three_clubs.jpg", "three_spades.jpg", "three_hearts.jpg", "three_diamonds.jpg",
        "four_clubs.jpg", "four_spades.jpg", "four_hearts.jpg", "four_diamonds.jpg",
        "five_clubs.jpg", "five_spades.jpg", "five_hearts.jpg", "five_diamonds.jpg",
        "six_clubs.jpg", "six_spades.jpg", "six_hearts.jpg", "six_diamonds.jpg",
        "seven_clubs.jpg", "seven_spades.jpg", "seven_hearts.jpg", "seven_diamonds.jpg",
        "eight_clubs.jpg", "eight_spades.jpg", "eight_hearts.jpg", "eight_diamonds.jpg",
        "nine_clubs.jpg", "nine_spades.jpg", "nine_hearts.jpg", "nine_diamonds.jpg",
        "ten_clubs.jpg", "ten_spades.jpg", "ten_hearts.jpg", "ten_diamonds.jpg",
        "jack_clubs.jpg", "jack_spades.jpg", "jack_hearts.jpg", "jack_diamonds.jpg",
        "queen_clubs.jpg", "queen_spades.jpg", "queen_hearts.jpg", "queen_diamonds.jpg",
        "king_clubs.jpg", "king_spades.jpg", "king_hearts.jpg", "king_diamonds.jpg",
        "ace_clubs.jpg", "ace_spades.jpg", "ace_hearts.jpg", "ace_diamonds.jpg",
        "two_clubs.jpg", "two_spades.jpg", "two_hearts.jpg", "two_diamonds.jpg",
        "three_clubs.jpg", "three_spades.jpg", "three_hearts.jpg", "three_diamonds.jpg",
        "four_clubs.jpg", "four_spades.jpg", "four_hearts.jpg", "four_diamonds.jpg",
        "five_clubs.jpg", "five_spades.jpg", "five_hearts.jpg", "five_diamonds.jpg",
        "six_clubs.jpg", "six_spades.jpg", "six_hearts.jpg", "six_diamonds.jpg",
        "seven_clubs.jpg", "seven_spades.jpg", "seven_hearts.jpg", "seven_diamonds.jpg",
        "eight_clubs.jpg", "eight_spades.jpg", "eight_hearts.jpg", "eight_diamonds.jpg",
        "nine_clubs.jpg", "nine_spades.jpg", "nine_hearts.jpg", "nine_diamonds.jpg",
        "ten_clubs.jpg", "ten_spades.jpg", "ten_hearts.jpg", "ten_diamonds.jpg",
        "jack_clubs.jpg", "jack_spades.jpg", "jack_hearts.jpg", "jack_diamonds.jpg",
        "queen_clubs.jpg", "queen_spades.jpg", "queen_hearts.jpg", "queen_diamonds.jpg",
        "king_clubs.jpg", "king_spades.jpg", "king_hearts.jpg", "king_diamonds.jpg"};

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

        while (!play) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {

            }
        }
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
            if (players[i].status == 1) {
                players[i].evaluate(board, round);
                hands[i] = players[i].hand;
            }
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
        for (i = 0; i < NUM_PLAYERS; i++) {
            if (players[i].status == 0 && players[i].chips <= 0) {
                players[i].chips = 0;
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
        escrow = winner;
        if (winner == 2) {
            s = "PLAYER " + winner + " WINS!!!";
        } else {
            s = "CPU " + winner + " WINS!!!";
        }
        waitCPU(winner, s);
        checkGameEnd();
        reset();
    }

    /**
     * Check the status of the end of the game
     */
    public static void checkGameEnd() {
        int i = 0, out = 0;
        for (i = 0; i < NUM_PLAYERS; i++) {
            if (players[i].chips <= 0) {
                players[i].chips = 0;
                out++;
            }
            if (players[2].chips == 0) {
                endGame();
                return;
            }
        }
        if (out == 4) {
            winGame();
        }
    }

    /**
     * End the game if the player loses.
     */
    public static void endGame() {
        called = false;
        folded = false;
        betted = false;
        takingTurn = false;
        resetDealing();
        resetFlopDealing();
        resetTurnDealing();
        resetRiverDealing();
        cpuDealing = false;
        currentPot.setText("<html> <h2><strong>GAME OVER</strong></h2> </html>");
        POT_TEXT = "GAME OVER";
        gui.repaint();
        while (!cpuDealing) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
            }

        }
    }

    /**
     * If the player won the game
     */
    public static void winGame() {
        called = false;
        folded = false;
        betted = false;
        takingTurn = false;
        resetDealing();
        resetFlopDealing();
        resetTurnDealing();
        resetRiverDealing();
        cpuDealing = false;
        currentPot.setText("<html> <h2><strong>YOU WIN!!!</strong></h2></html>");
        POT_TEXT = "YOU WIN!!!";
        gui.repaint();
        while (!cpuDealing) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
            }

        }

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
        takingTurn = true;
        while (!called && !folded && !betted) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
            }
        }
        takingTurn = false;
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
        gui.playSound("cardSlide", 1, 8);
        currentBet = BB_BET; //Set the minumum bet to the BB_BET (100)
        int i = smallBlind, j = 0;
        //String s = ""; //String used to waitCPU steps
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
                    s = "Player betting...";
                    System.out.println(s);
                    called = false;
                    folded = false;
                    betted = false;
                    takeTurn(); //Take your turn to decide
                    //players[i].status = 0; //folds the player for CPU simulation
                } else if (players[i].name.contains("CPU")) { //If the person is the CPU
                    s = "CPU " + i + " THINKING...";
                    waitCPU(i, s);
                    if ((currentBet = players[i].decide(round, currentBet, board, i, smallBlind, bigBlind)) == 0) { //CPU folded or checked
                        if (i == bigBlind && round == 1 && players[i].status == 1) { //If big blind checks in round 1
                            //s = "BIG BLIND (CPU " + i + ") CHECKED";
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
                waitCPU(0, "");
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
                        /*
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
                         */
                        takeTurn();
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
            sleep(1000);
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
        escrow = -1;
        Random rand = new Random();
        System.out.println("DEALING...");
        if (players[2].chips > 0) {
            players[2].status = 1;
            called = false;
            folded = false;
            betted = false;
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
        JLabel label = new JLabel();

        if (name.contains("TEXAS")) {
            label.setText("<html><h1>" + name + "</h1></html>");
        } else if (name.contains("Created")) {
            label.setText("<html><h3>" + name + "</h3></html>");
        } else {
            label = new JLabel(name);
        }

        //label.setLayout(null);
        label.setBounds(posX, posY, sizeX, sizeY);

        label.setForeground(Color.white);

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

        final JPanel panel1 = (JPanel) jf.getGlassPane();
        final JPanel panel2 = (JPanel) jf.getGlassPane();

        //main menu buttons
        final JButton playButton = new JButton();
        final JButton quit = new JButton();

        final JButton leave = new JButton("Leave Game"); //creates a button to leave game and quit program
        final JButton call = new JButton("Call"); //creates call button
        final JButton fold = new JButton("Fold"); //creates fold button

        //creates/initializes the text, but the update/rewritten gets done in GUI.ActionPerformed
        final JButton bet = new JButton("Bet");
        final JButton plus = new JButton("+");
        final JButton minus = new JButton("-");

        panel1.setLayout(null);
        panel1.setVisible(true);

        //sets the text of the buttons
        playButton.setText("<html><h2>PLAY</h2></html>");
        quit.setText("<html><h2>QUIT</h2></html>");

        //location and size for buttons
        playButton.setBounds(300, 190, 200, 50);
        playButton.setLocation(300, 190);
        quit.setBounds(300, 260, 200, 50);
        quit.setLocation(300, 260);

        //create the label for the names on the main menu
        String t = "Created By: Michael Woyden, Jedrick Boca, Tristan Anderson";
        title = createText("TEXAS HOLD'EM", 300, 1, 500, TEXT_HEIGHT + 50);
        names = createText(t, 190, 340, 500, TEXT_HEIGHT);

        jf.add(gui);

        //adds the buttons/label
        panel1.add(playButton);
        panel1.add(quit);
        panel1.add(names);
        panel1.add(title);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play = true;

            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                System.exit(0);

            }
        });

        jf.setTitle("Texas Hold'em");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        jf.setVisible(true);

        while (!play) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {

            }
        }

        //remove the buttons and names when you hit play
        panel1.remove(playButton);
        panel1.remove(quit);
        panel1.remove(names);

        //create the cpu labels and chip count
        cpu1 = createText(String.valueOf(players[0].chips), 680, 70, TEXT_WIDTH, TEXT_HEIGHT);
        cpu2 = createText(String.valueOf(players[1].chips), 680, 460, TEXT_WIDTH, TEXT_HEIGHT);
        player = createText(String.valueOf(players[2].chips), 360, 530, TEXT_WIDTH, TEXT_HEIGHT);
        cpu3 = createText(String.valueOf(players[3].chips), 70, 460, TEXT_WIDTH, TEXT_HEIGHT);
        cpu4 = createText(String.valueOf(players[4].chips), 70, 70, TEXT_WIDTH, TEXT_HEIGHT);
        currentPot = createText(String.valueOf(pot), 360, 180, TEXT_WIDTH + 20, TEXT_HEIGHT + 50);
        showBet = createText(String.valueOf(playerBet), 220, 478, TEXT_WIDTH, TEXT_HEIGHT);

        //create the label for the cpu's actions
        cpu1Action = createText("", 640, 220, TEXT_WIDTH + 20, TEXT_HEIGHT);
        cpu2Action = createText("", 620, 430, TEXT_WIDTH + 20, TEXT_HEIGHT);
        cpu3Action = createText("", 80, 430, TEXT_WIDTH + 20, TEXT_HEIGHT);
        cpu4Action = createText("", 70, 220, TEXT_WIDTH + 20, TEXT_HEIGHT);
        playerAction = createText("", 460, 460, TEXT_WIDTH + 20, TEXT_HEIGHT);

        //location for call and fold button
        call.setBounds(275, 510, 59, 25);
        call.setLocation(275, 510);
        fold.setBounds(275, 450, 59, 25);
        fold.setLocation(275, 450);
        bet.setBounds(275, 480, 59, 25);
        bet.setLocation(275, 480);
        plus.setBounds(200, 450, 50, 25);
        plus.setLocation(200, 450);
        minus.setBounds(200, 510, 50, 25);
        minus.setLocation(200, 510);
        leave.setBounds(FRAME_WIDTH-350, FRAME_HEIGHT-70, 115, 25);
        leave.setLocation(FRAME_WIDTH-350, FRAME_HEIGHT-70);

        //call.setLayout(null);
        panel2.setLayout(null);

        panel2.setBounds(360, 550, 60, 25);

        panel2.setVisible(true);

        //add cpu's labels
        panel2.add(cpu1);
        panel2.add(cpu2);
        panel2.add(cpu3);
        panel2.add(cpu4);
        panel2.add(player);
        panel2.add(currentPot);
        panel2.add(showBet);
        panel2.add(call);
        panel2.add(fold);
        panel2.add(bet);
        panel2.add(plus);
        panel2.add(minus);
        panel2.add(leave);

        //add the cpu's action labels
        panel2.add(cpu1Action);
        panel2.add(cpu2Action);
        panel2.add(cpu3Action);
        panel2.add(cpu4Action);
        panel2.add(playerAction);

        //adds table and deck spirte
        jf.add(gui);

        call.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (takingTurn) {
                    players[2].call(currentBet);
                    called = true;
                    pot += currentBet;
                    playerBet = 0;
                    showBet.setBounds(220, 478, TEXT_WIDTH, TEXT_HEIGHT);
                    showBet.setLocation(220, 478);
                }
            }
        });

        //set the action when the call button is pressed
        fold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (takingTurn) {
                    players[2].fold();
                    folded = true;
                    playerBet = 0;
                    showBet.setBounds(220, 478, TEXT_WIDTH, TEXT_HEIGHT);
                    showBet.setLocation(220, 478);
                }
            }
        });

        bet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (takingTurn) {
                    if (round == 1 && playerBet < 100) {
                        return;
                    }
                    if (playerBet < currentBet) {
                        return;
                    }
                    players[2].bet(playerBet);
                    pot += playerBet;
                    betted = true;
                    playerBet = 0;
                    showBet.setBounds(220, 478, TEXT_WIDTH, TEXT_HEIGHT);
                    showBet.setLocation(220, 478);
                }
            }
        });

        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (takingTurn) {
                    if (players[2].chips >= playerBet + 10) {
                        playerBet += 10;
                    }
                    if (playerBet > 0 && playerBet < 100) {
                        showBet.setBounds(215, 478, TEXT_WIDTH, TEXT_HEIGHT);
                        showBet.setLocation(215, 478);
                    }
                    if (playerBet >= 100) {
                        showBet.setBounds(210, 478, TEXT_WIDTH, TEXT_HEIGHT);
                        showBet.setLocation(210, 478);
                    }
                }
            }
        });

        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (takingTurn) {
                    if (playerBet - 10 >= 0) {
                        playerBet -= 10;
                    }
                    if (playerBet == 0) {
                        showBet.setBounds(220, 478, TEXT_WIDTH, TEXT_HEIGHT);
                        showBet.setLocation(220, 478);
                    }
                    if (playerBet > 0 && playerBet < 100) {
                        showBet.setBounds(215, 478, TEXT_WIDTH, TEXT_HEIGHT);
                        showBet.setLocation(215, 478);
                    }
                    if (playerBet >= 100) {
                        showBet.setBounds(210, 478, TEXT_WIDTH, TEXT_HEIGHT);
                        showBet.setLocation(210, 478);
                    }
                }
            }
        });
        
        leave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                System.exit(0);
            }
        });

        jf.setTitle("Texas Hold'em");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        jf.setVisible(true);

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
        playerBet = 0;
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
