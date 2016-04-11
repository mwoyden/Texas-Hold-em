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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import static texasholdem.TexasHoldem.*;

public class GUI extends JPanel implements ActionListener {

    private static final int NUM_CPUS = 4;
    private static final int STARTING_X = 385;
    private static final int STARTING_Y = 10;
    private static final int VELOCITY = 3;
    public static BufferedImage image1;

    Timer clock = new Timer(10, this); //this ActionListener
    Image image;
    String path;
    ImageIcon i;
    String cardName;
    boolean cpuDealing = true;
    static boolean flopDealing = false;
    static boolean turnDealing = false;
    static boolean riverDealing = false;
    int x, y, xv, yv, xpo, ypo;

    /* EXTRA DECKS */
    int[] extraX = {STARTING_X, STARTING_X + 2, STARTING_X + 4};
    int[] extraY = {STARTING_Y, STARTING_Y + 2, STARTING_Y + 4};

    /* CPUS GLOBAL VARIABLES*/
    int[] cpuX = {STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X, STARTING_X};
    int[] cpuY = {STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y, STARTING_Y};
    int[] YVel = {VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY, VELOCITY};
    int[] XVel = {VELOCITY, VELOCITY, -VELOCITY, -VELOCITY, VELOCITY, VELOCITY, -VELOCITY, -VELOCITY, VELOCITY};

    /* PLAYER'S GLOBAL VARIABLES*/
    int[] playerX = {STARTING_X, STARTING_X};
    int[] playerY = {STARTING_Y, STARTING_Y};
    int[] playerXVel = {-VELOCITY, -VELOCITY};
    int[] playerYVel = {VELOCITY, VELOCITY};

    /* FLOP'S GLOBAL VARIABLES*/
    int[] flopX = {STARTING_X, STARTING_X, STARTING_X};
    int[] flopY = {STARTING_Y, STARTING_Y, STARTING_Y};
    int[] flopXVel = {-VELOCITY, -VELOCITY, -VELOCITY};
    int[] flopYVel = {VELOCITY, VELOCITY, VELOCITY};


    /* TURN'S GLOBAL VARIABLES*/
    int turnX = STARTING_X;
    int turnY = STARTING_Y;
    int turnXVel = -VELOCITY;
    int turnYVel = VELOCITY;

    /* RIVER'S GLOBAL VARIABLE*/
    int riverX = STARTING_X;
    int riverY = STARTING_Y;
    int riverXVel = -VELOCITY;
    int riverYVel = VELOCITY;

    int j = 0, k = 0;

    //draws the table
    public void createTable(Graphics g) {
        /* DEFAULT JFRAME SIZE IS 385 X 600*/

        path = System.getProperty("user.dir") + "/src/texasholdem/sprites/table3.jpg";
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, 0, 0, /*WIDTH*/ 600, /*HEIGHT*/ 385, this);
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
        path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card + ".jpg";
        i = new ImageIcon(path);
        image = i.getImage();
        g.drawImage(image, turnX, turnY, this);
    }

    public void dealRiver(Graphics g, String card) {
        //first card
        path = System.getProperty("user.dir") + "/src/texasholdem/sprites/" + card + ".jpg";
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

        //deals facedown cards for the CPU's
        dealCPU(g);

        //deals face up cards for the player
        dealPlayer(g, DECK_MAP[players[2].hole[0].id], DECK_MAP[players[2].hole[1].id]);

        //deals three cards face down for the flop
        dealFlop(g, DECK_MAP[board[0].id], DECK_MAP[board[1].id], DECK_MAP[board[2].id]);

        //deals one card for the turn
        dealTurn(g, "king_spades");

        //deals one card for the river
        dealRiver(g, "ace_spades");

        //shows extra cards on table
        showExtraCards(g, "card_back", "card_back", "card_back");

        //important-starts the timer
        clock.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //positions for each cpu
        int posX[] = {500, 480, 40, 30};
        int posY[] = {60, 250, 250, 60};
        int posX2[] = {520, 500, 60, 50};
        int posY2[] = {80, 270, 270, 80};

        //positions for the player
        int playerPosX = 270;
        int playerPosY = 280;
        int increment = 20;

        //positions for the flop
        int flopPosX = 147;
        int flopPosY = 150;
        int flopIncrement = 60;

        //positions for the turn
        int turnPosX = flopPosX + (flopIncrement * 3);
        int turnPosY = flopPosY;

        //positions for the river
        int riverPosX = 150 + (flopIncrement * 4);
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
            if (riverX < riverPosX) {
                riverXVel = 0;
            }

            if (riverY > riverPosY) {
                riverYVel = 0;
            }

            riverX += riverXVel; //moves image
            riverY += riverYVel;
        }

        repaint(); //repaints the image every 10 milliseconds

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
    /*
    public static void main(String[] args) {
        GUI test = new GUI();

        JFrame jf = new JFrame();
        jf.setTitle("Texas Hold'em");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //adds table and first card
        jf.add(test);
        jf.setSize(600, 400);
        jf.setVisible(true);

        //wait 1.5 seconds to deal the flop face down
        try {
            sleep(1500);
        } catch (InterruptedException ex) {
        }
        setFlopDealing();

        //wait 1.5 seconds to deal the turn
        try {
            sleep(1500);
        } catch (InterruptedException ex) {
        }
        setTurnDealing();

        //wait 1.5 seconds to deal the turn
        try {
            sleep(1500);
        } catch (InterruptedException ex) {
        }
        setRiverDealing();
    }*/
}
