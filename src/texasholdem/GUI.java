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

public class GUI extends JPanel implements ActionListener {

    private static final int NUM_CPUS = 4;
    private static final int STARTING_X = 455;
    private static final int STARTING_Y = 100;
    private static final int VELOCITY = 3;
    private static final int RACK_Y = 100;
    public static BufferedImage image1;

    Timer clock = new Timer(10, this); //this ActionListener
    Image image;
    String path;
    ImageIcon i;
    String cardName;
    static boolean cpuDealing = false;
    static boolean flopDealing = false;
    static boolean turnDealing = false;
    static boolean riverDealing = false;

    /* EXTRA DECKS */
    int[] extraX = {STARTING_X, STARTING_X + 2, STARTING_X + 4};
    int[] extraY = {STARTING_Y, STARTING_Y + 2, STARTING_Y + 4};

    static int[] chipsX = {335, 361, 384, 406, 427};
    static int[] chipsY = {RACK_Y, RACK_Y, RACK_Y, RACK_Y, RACK_Y};

    /* CPUS GLOBAL VARIABLES*/
    static int[] cpuX = {STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X};
    static int[] cpuY = {STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y};
    static int[] YVel = {VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY};
    static int[] XVel = {VELOCITY, VELOCITY, VELOCITY, VELOCITY, -VELOCITY, -VELOCITY, -VELOCITY, -VELOCITY, VELOCITY};

    /* PLAYER'S GLOBAL VARIABLES*/
    static int[] playerX = {STARTING_X, STARTING_X};
    static int[] playerY = {STARTING_Y, STARTING_Y};
    static int[] playerXVel = {-VELOCITY, -VELOCITY};
    static int[] playerYVel = {VELOCITY, VELOCITY};

    /* FLOP'S GLOBAL VARIABLES*/
    static int[] flopX = {STARTING_X, STARTING_X, STARTING_X};
    static int[] flopY = {STARTING_Y, STARTING_Y, STARTING_Y};
    static int[] flopXVel = {-VELOCITY, -VELOCITY, -VELOCITY};
    static int[] flopYVel = {VELOCITY, VELOCITY, VELOCITY};


    /* TURN'S GLOBAL VARIABLES*/
    static int turnX = STARTING_X;
    static int turnY = STARTING_Y;
    static int turnXVel = -VELOCITY;
    static int turnYVel = VELOCITY;

    /* RIVER'S GLOBAL VARIABLE*/
    static int riverX = STARTING_X;
    static int riverY = STARTING_Y;
    static int riverXVel = VELOCITY;
    static int riverYVel = VELOCITY;

    int j = 0, k = 0;

    //draws the table
    public void createTable(Graphics g) {
        /* DEFAULT JFRAME SIZE IS 385 X 600*/

        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/table6.jpg"));
        image = i.getImage();
        g.drawImage(image, 0, 0, /*WIDTH*/ FRAME_WIDTH, /*HEIGHT*/ FRAME_HEIGHT, this);
    }

    public void dealCPU(Graphics g) {

        for (j = 0; j < NUM_CPUS * 2; j++) {
            i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
            image = i.getImage();
            g.drawImage(image, cpuX[j], cpuY[j], this);
        }

    }

    public void dealCPU0(Graphics g) {
        //first card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[0], cpuY[0], this);

        //second card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[1], cpuY[1], this);

    }

    public void dealCPU1(Graphics g) {
        //first card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[2], cpuY[2], this);

        //second card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[3], cpuY[3], this);

    }

    public void dealCPU2(Graphics g) {
        //first card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[4], cpuY[4], this);

        //second card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[5], cpuY[5], this);

    }

    public void dealCPU3(Graphics g) {
        //first card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[6], cpuY[6], this);

        //second card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, cpuX[7], cpuY[7], this);

    }

    public void dealPlayer(Graphics g, String card, String card2) {

        //first card
        path = card;

        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/" + path));
        image = i.getImage();
        g.drawImage(image, playerX[0], playerY[0], this);

        //second card
        path = card2;
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/" + path));
        image = i.getImage();
        g.drawImage(image, playerX[1], playerY[1], this);

    }

    public void dealFlop(Graphics g, String card, String card2, String card3) {
        //first card
        path = card;
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/" + path));
        image = i.getImage();
        g.drawImage(image, flopX[0], flopY[0], this);

        //second card
        path = card2;
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/" + path));
        image = i.getImage();
        g.drawImage(image, flopX[1], flopY[1], this);

        //third card
        path = card3;
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/" + path));
        image = i.getImage();
        g.drawImage(image, flopX[2], flopY[2], this);
    }

    public void dealTurn(Graphics g, String card) {
        //first card
        path = card;
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/" + path));
        image = i.getImage();
        g.drawImage(image, turnX, turnY, this);
    }

    public void dealRiver(Graphics g, String card) {
        //first card
        path = card;
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/" + path));
        image = i.getImage();
        g.drawImage(image, riverX, riverY, this);
    }

    public void showExtraCards(Graphics g) {
        //third card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, extraX[2], extraY[2], this);

        //second card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, extraX[1], extraY[1], this);

        //first card
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/card_back.jpg"));
        image = i.getImage();
        g.drawImage(image, extraX[0], extraY[0], this);

    }

    public void drawChipRack(Graphics g) {
        //Show blue chips
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/poker_chips_blue.jpg"));
        image = i.getImage();
        g.drawImage(image, chipsX[0], chipsY[0], this);
        //Show black chips
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/poker_chips_black.jpg"));
        image = i.getImage();
        g.drawImage(image, chipsX[1], chipsY[1], this);
        //Show green chips
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/poker_chips_green.jpg"));
        image = i.getImage();
        g.drawImage(image, chipsX[2], chipsY[2], this);
        //Show white chips
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/poker_chips_white.jpg"));
        image = i.getImage();
        g.drawImage(image, chipsX[3], chipsY[3], this);
        //Show red chips
        i = new ImageIcon(this.getClass().getResource("/texasholdem/sprites/poker_chips_red.jpg"));
        image = i.getImage();
        g.drawImage(image, chipsX[4], chipsY[4], this);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draws the table
        createTable(g);
        
        //Draw chips rack
        drawChipRack(g);

        if (players[2].status == 1) {
            //deals face up cards for the player
            dealPlayer(g, DECK_MAP[players[2].hole[0].id], DECK_MAP[players[2].hole[1].id]);
        }

        if (cpuDealing) {
            //deals facedown cards for the CPU's
            if (players[0].status == 1) {
                dealCPU0(g);
            }
            if (players[1].status == 1) {
                dealCPU1(g);
            }
            if (players[3].status == 1) {
                dealCPU2(g);
            }
            if (players[4].status == 1) {
                dealCPU3(g);
            }
        }

        //deals three cards face up for the flop
        if (flopDealing) {
            dealFlop(g, DECK_MAP[board[0].id], DECK_MAP[board[1].id], DECK_MAP[board[2].id]);
        }

        //deals one card for the turn
        if (turnDealing) {
            dealTurn(g, DECK_MAP[board[3].id]);
        }

        //deals one card for the river
        if (riverDealing) {
            dealRiver(g, DECK_MAP[board[4].id]);
        }

        //shows extra cards on table
        showExtraCards(g);

        //important-starts the timer
        clock.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //positions for each cpu
        //first number is for the first card, next number is for the second card for each cpu...
        int posX[] = {640, 660, 620, 640, 100, 120, 80, 100};
        int posY[] = {110, 130, 320, 340, 320, 340, 110, 130};

        //positions for the player
        int playerPosX = 370;
        int playerPosY = 400;
        int increment = 20;

        //positions for the flop
        int flopPosX = 247;
        int flopPosY = 270;
        int flopIncrement = 60;

        //positions for the turn
        int turnPosX = flopPosX + (flopIncrement * 3);
        int turnPosY = flopPosY;

        /*(240) + (flopIncrement * 4);*/
        //positions for the river
        int riverPosX = 483;
        int riverPosY = flopPosY;

        if (cpuDealing) {

            //deal cpu0's first and second card
            for (j = 0; j < 2; j++) {
                if ((cpuX[j] > posX[j])) {
                    XVel[j] = 0; //makes the image stop at x value.
                }

                if (cpuY[j] > posY[j]) {
                    YVel[j] = 0;
                }

                cpuX[j] += XVel[j]; // moves the image
                cpuY[j] += YVel[j];

                posX[j] += increment;
                posY[j] += increment;

            }

            //deal cpu1's first and second card
            for (j = 2; j < 4; j++) {
                if ((cpuX[j] > posX[j])) {
                    XVel[j] = 0; //makes the image stop at x value.
                }

                if (cpuY[j] > posY[j]) {
                    YVel[j] = 0;
                }

                cpuX[j] += XVel[j]; // moves the image
                cpuY[j] += YVel[j];

                posX[j] += increment;
                posY[j] += increment;

            }

            //deal cpu2's first and second card
            for (j = 4; j < 6; j++) {
                if ((cpuX[j] < posX[j])) {
                    XVel[j] = 0; //makes the image stop at x value.
                }

                if (cpuY[j] > posY[j]) {
                    YVel[j] = 0;
                }

                cpuX[j] += XVel[j]; // moves the image
                cpuY[j] += YVel[j];

                posX[j] += increment;
                posY[j] += increment;

            }

            //deal cpu2's first and second card
            for (j = 6; j < 8; j++) {
                if ((cpuX[j] < posX[j])) {
                    XVel[j] = 0; //makes the image stop at x value.
                }

                if (cpuY[j] > posY[j]) {
                    YVel[j] = 0;
                }

                cpuX[j] += XVel[j]; // moves the image
                cpuY[j] += YVel[j];

                posX[j] += increment;
                posY[j] += increment;

            }

            //players animations
            for (j = 0; j < 2; j++) {
                //deals Player's two cards
                if ((playerX[j] < playerPosX)) {
                    playerXVel[j] = 0;
                }

                if (playerY[j] > playerPosY) {
                    playerYVel[j] = 0;
                }

                playerX[j] += playerXVel[j]; // moves the image
                playerY[j] += playerYVel[j];

                //increment the second card
                playerPosX += increment;
                playerPosY += increment;
            }
        }

        if (flopDealing) {
            for (j = 0; j < 3; j++) {
                //deals the three cards for flop
                if (flopX[j] < flopPosX) {
                    flopXVel[j] = 0;
                }

                if (flopY[j] > flopPosY) {
                    flopYVel[j] = 0;
                }

                flopX[j] += flopXVel[j]; //moves image
                flopY[j] += flopYVel[j];

                //increment the second and third card over 60 pixels
                flopPosX += flopIncrement;

            }
        }

        //deals with the turn
        if (turnDealing) {
            if (turnX < turnPosX) {
                turnXVel = 0;
            }

            if (turnY > turnPosY) {
                turnYVel = 0;
            }

            turnX += turnXVel; //moves image
            turnY += turnYVel;
        }

        //deals with the river
        if (riverDealing) {
            if (riverX > riverPosX) {
                riverXVel = 0;
            }

            if (riverY > riverPosY) {
                riverYVel = 0;
            }

            riverX += riverXVel; //moves image
            riverY += riverYVel;
        }

        //UPDATE THE CHIP COUNT OF THE CPUS/PLAYER
        cpu1.setText("<html> <strong>CPU 1</strong> <br> Chips: " + String.valueOf(players[0].chips) + " </html>");
        cpu2.setText("<html> <strong>CPU 2</strong> <br> Chips: " + String.valueOf(players[1].chips) + " </html>");
        player.setText("<html> <strong>PLAYER</strong> <br> Chips: " + String.valueOf(players[2].chips) + " </html>");
        cpu3.setText("<html> <strong>CPU 3</strong> <br> Chips: " + String.valueOf(players[3].chips) + " </html>");
        cpu4.setText("<html> <strong>CPU 4</strong> <br> Chips: " + String.valueOf(players[4].chips) + " </html>");
        currentPot.setText("<html> <h2><strong>POT: </strong>" + String.valueOf(pot) + "</h2> </html>");
        showBet.setText("<html><h2>" + String.valueOf(playerBet) + "</h2></html>");
        
        //UPDATE THE ACTION LABELS OF THE CPUS
        cpu1Action.setText(parseCPU0(s));
        cpu2Action.setText(parseCPU1(s));
        cpu3Action.setText(parseCPU2(s));
        cpu4Action.setText(parseCPU3(s));
        playerAction.setText(parsePlayer(s));
        
        repaint(); //repaints the image every 10 milliseconds

    }

    public static String parseCPU0(String text) {
        String newText = "";
        CharSequence cpu0 = "CPU 0";

        if (text.contains(cpu0)) {
            newText = text;
            return newText;
        }

        return newText;

    }

    public static String parseCPU1(String text) {
        String newText = "";
        CharSequence cpu1 = "CPU 1";

        if (text.contains(cpu1)) {
            newText = text;
            return newText;
        }

        return newText;

    }

    public static String parseCPU2(String text) {
        String newText = "";
        CharSequence cpu3 = "CPU 3";

        if (text.contains(cpu3)) {
            newText = text;
            return newText;
        }

        return newText;

    }

    public static String parseCPU3(String text) {
        String newText = "";
        CharSequence cpu4 = "CPU 4";

        if (text.contains(cpu4)) {
            newText = text;
            return newText;
        }

        return newText;

    }
    
    public static String parsePlayer(String text) {
        String newText = "";
        CharSequence player = "Player";

        if (text.contains(player)) {
            newText = "YOUR TURN!";
            return newText;
        }

        return newText;

    }

    public static void setDealing() {
        cpuDealing = true;
    }

    public static void setFlopDealing() {
        flopDealing = true;
    }

    public static void setTurnDealing() {
        turnDealing = true;
    }

    public static void setRiverDealing() {
        riverDealing = true;
    }

    public static void resetDealing() {
        cpuDealing = false;
    }

    public static void resetFlopDealing() {
        flopDealing = false;
    }

    public static void resetTurnDealing() {
        turnDealing = false;
    }

    public static void resetRiverDealing() {
        riverDealing = false;
    }

    /**
     * Resets the position of each card and the velocity. Also resets the card
     * dealing flags
     */
    public static void resetGUI() {
        int i = 0;
        for (i = 0; i < cpuX.length; i++) {
            cpuX[i] = STARTING_X;
            cpuY[i] = STARTING_Y;
        }
        for (i = 0; i < YVel.length; i++) {
            YVel[i] = VELOCITY;
            XVel[i] = VELOCITY;
            if (i == 4 || i == 5 || i == 6 || i == 7) {
                XVel[i] = -VELOCITY;
            }
        }
        for (i = 0; i < playerX.length; i++) {
            playerX[i] = STARTING_X;
            playerY[i] = STARTING_Y;
            playerXVel[i] = -VELOCITY;
            playerYVel[i] = VELOCITY;
        }
        for (i = 0; i < flopX.length; i++) {
            flopX[i] = STARTING_X;
            flopY[i] = STARTING_Y;
            flopXVel[i] = -VELOCITY;
            flopYVel[i] = VELOCITY;
        }

        turnX = STARTING_X;
        turnY = STARTING_Y;
        turnXVel = -VELOCITY;
        turnYVel = VELOCITY;

        riverX = STARTING_X;
        riverY = STARTING_Y;
        riverXVel = VELOCITY;
        riverYVel = VELOCITY;

        resetDealing();
        resetFlopDealing();
        resetTurnDealing();
        resetRiverDealing();
    }

}
