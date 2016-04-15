package texasholdem;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import javax.imageio.ImageIO;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

import static texasholdem.TexasHoldem.*;


public class GUI extends JPanel implements ActionListener {

    private static final int NUM_CPUS = 4;
    private static final int STARTING_X = 455;
    private static final int STARTING_Y = 100;
    private static final int VELOCITY = 3;
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

    /* CPUS GLOBAL VARIABLES*/
    static int[] cpuX = {STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X};
    static int[] cpuY = {STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y};
    static int[] YVel = {VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY};
    static int[] XVel = {VELOCITY, VELOCITY, -VELOCITY, -VELOCITY, VELOCITY, VELOCITY, -VELOCITY, -VELOCITY, VELOCITY};

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

        path = System.getProperty("user.dir") + "/src/texasholdem/sprites/table6.jpg";
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, 0, 0, /*WIDTH*/ FRAME_WIDTH, /*HEIGHT*/ FRAME_HEIGHT, this);
    }

    public void dealCPU(Graphics g) {

        for (j = 0; j < NUM_CPUS * 2; j++) {
            path = System.getProperty("user.dir") + "/src/texasholdem/sprites/card_back.jpg";
            i = new ImageIcon(path);
            image = i.getImage();
            g.drawImage(image, cpuX[j], cpuY[j], this);
        }

    }

    public void dealPlayer(Graphics g, String card, String card2) {

        //first card
        path = card;

        //path = DECK_MAP[players[2].hole[0].id];
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, playerX[0], playerY[0], this);

        //second card
        //path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card2 + ".jpg";
        path = card2;
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, playerX[1], playerY[1], this);

    }

    public void dealFlop(Graphics g, String card, String card2, String card3) {
        //first card
        path = card;
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, flopX[0], flopY[0], this);

        //second card
        path = card2;
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, flopX[1], flopY[1], this);

        //third card
        path = card3;
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, flopX[2], flopY[2], this);
    }

    public void dealTurn(Graphics g, String card) {
        //first card
        //path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card + ".jpg";
        path = card;
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, turnX, turnY, this);
    }

    public void dealRiver(Graphics g, String card) {
        //first card
        //path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card + ".jpg";
        path = card;
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, riverX, riverY, this);
    }

    public void showExtraCards(Graphics g, String card, String card2, String card3) {
        //third card
        path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card3 + ".jpg";
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, extraX[2], extraY[2], this);

        //second card
        path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card2 + ".jpg";
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, extraX[1], extraY[1], this);

        //first card
        path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card + ".jpg";
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, extraX[0], extraY[0], this);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draws the table
        createTable(g);
        
        if (players[2].status == 1) {
            //deals face up cards for the player
            dealPlayer(g, DECK_MAP[players[2].hole[0].id], DECK_MAP[players[2].hole[1].id]);
        }

        if (cpuDealing) {
            //deals facedown cards for the CPU's
            dealCPU(g);
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
        showExtraCards(g, "card_back", "card_back", "card_back");

        //important-starts the timer
        clock.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //positions for each cpu
        int posX[] = {640, 620, 100, 80};
        int posY[] = {110, 320, 320, 110};
        int posX2[] = {660, 640, 120, 100};
        int posY2[] = {130, 355, 340, 130};

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
        int riverPosX =  483;
        int riverPosY = flopPosY;

        if (cpuDealing) {
            //deals cpu's first card
            for (j = 0; j < NUM_CPUS; j++) {
                if ((cpuX[j] > posX[j]) && (j == 0 || j == 1)) {//if in first cpu position, stop
                    XVel[j] = 0; //makes the image stop at x value.
                }

                if ((cpuX[j] < posX[j]) && (j == 2 || j == 3)) {
                    XVel[j] = 0;
                }

                if (cpuY[j] > posY[j]) {// if in second cpu position, stop
                    YVel[j] = 0;
                }

                cpuX[j] += XVel[j]; // moves the image
                cpuY[j] += YVel[j];
            }

            //deals cpu's second card
            for (j = 4, k = 0; j < NUM_CPUS * 2; j++, k++) {

                if ((cpuX[j] > posX2[k]) && (j == 4 || j == 5)) {//if in first cpu position, stop
                    XVel[j] = 0; //makes the image stop at x value.

                }

                if ((cpuX[j] < posX2[k]) && (j == 6 || j == 7)) {
                    XVel[j] = 0;
                }

                if (cpuY[j] > posY2[k]) {// if in second cpu position, stop
                    YVel[j] = 0;
                }

                cpuX[j] += XVel[j]; // moves the image
                cpuY[j] += YVel[j];

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
        cpu1.setText("Chips: " + String.valueOf((int)players[0].chips));
        cpu2.setText("Chips: " + String.valueOf((int)players[1].chips));
        player.setText("Chips: " + String.valueOf((int)players[2].chips));
        cpu3.setText("Chips: " + String.valueOf((int)players[3].chips));
        cpu4.setText("Chips: " + String.valueOf((int)players[4].chips));
        
        repaint(); //repaints the image every 10 milliseconds

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
     * Resets the position of each card and the velocity. Also resets the card dealing flags
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
            if (i == 2 || i == 3 || i == 6 || i == 7) {
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
        riverXVel = -VELOCITY;
        riverYVel = VELOCITY;
        
        resetDealing();
        resetFlopDealing();
        resetTurnDealing();
        resetRiverDealing();
    }

}
