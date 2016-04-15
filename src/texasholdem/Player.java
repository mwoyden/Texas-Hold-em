package texasholdem;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player extends Person {

    /**
     * Default constructor (not used)
     */
    public Player() {
        chips = 0;
        bet = 0;
        hole = new Card[2];
        x = 0;
        y = 0;
        status = 0;
    }

    /**
     * Alternate constructor (used)
     *
     * @param n player name
     * @param c chip count
     * @param xPos x position
     * @param yPos y position
     */
    public Player(String n, int c, int xPos, int yPos) {
        name = n;
        chips = c;
        hole = new Card[2];
        x = xPos;
        y = yPos;
        status = 0;
    }

    /**
     * Resets all the hand control variables
     */
    @Override
    public void fold() {
        super.fold();
        pairValue1 = 0;
        pairValue2 = 0;
        pair1 = 0;
        pair2 = 0;
        highCard = 0;
        highSuit = -1;
        flush = 0;
        straight = 0;
    }

    /**
     * Bets
     *
     * @param b chips to bet
     */
    @Override
    public void bet(int b) {
        bet = b;
        chips -= b;
    }

    /**
     * Call
     *
     * @param round
     * @param currentBet
     * @param board
     * @return
     */
    @Override
    public void call(int currentBet) {
        bet(currentBet - bet);
        System.out.println("CURRENT BET IN PLAYER CLASS: " + currentBet);
        System.out.println("PLAYERS CURRENT BET: " + bet);
        if (currentBet == 0) {
            System.out.println("Player checks");
        }
    }

    /**
     * Evaluates hand (see CPU Override)
     * @param board
     * @param round
     */
    @Override
    public void evaluate(Card[] board, int round) { //Finds if the board has a pair on it
        Map<Integer, Integer> m = new HashMap();
        for (Card c : board) {
            if (m.containsKey(c.value)) {
                m.put(c.value, m.get(c.value) + 1);
            } else {
                m.put(c.value, 1);
            }
        }
        //Variables to store board pairs
        int pairB2 = 0, pairValueB2 = 0, pairB3 = 0, pairValueB3 = 0;
        System.out.println("CHECKING FOR DUPLICATES...");
        for (int y : m.keySet()) {
            if (m.get(y) == 2) { //If there is a pair
                pairB2 = m.get(y);
                pairValueB2 = y;
                System.out.println("THERE ARE " + pairB2 + " " + pairValueB2 + "s ON THE BOARD");
            }
            if (m.get(y) == 3) { //If there is 3 of a kind
                pairB3 = m.get(y);
                pairValueB3 = y;
                System.out.println("THERE ARE " + pairB3 + " " + pairValueB3 + "s ON THE BOARD");
            }
        }
        //Find players high card
        if (hole[0].value >= hole[1].value) {
            highCard = hole[0].value;
        } else {
            highCard = hole[1].value;
        }

        checkPair(board);
        //checkStraight(board);
        checkFlush(board);

        if (hand == 1) { //If a pair
            if (pairB2 == 2 && pairValueB2 != pairValue1 && pairValueB2 != pairValue2) { //If the board has a pair
                hand = 2; //Hand becomes 2 pair
                System.out.println("FINAL HAND IS 2 PAIR");
            } else if (pairB3 == 3 && pairValueB3 != pairValue1 && pairValueB3 != pairValue2) { //If the board has 3 of a kind
                hand = 6; //hand becomes full house
                System.out.println("FINAL HAND IS A FULL HOUSE");
            } else { //If the board was pairless
                System.out.println("FINAL HAND IS A PAIR");
            }
        }
        if (hand == 3) { //If 3 of a kind
            if (pairB2 == 2 && pairValueB2 != pairValue1 && pairValueB2 != pairValue2) { //If the board has a pair
                hand = 6; //hand becomes full house
                System.out.println("FINAL HAND IS A FULL HOUSE");
            }
        }
        if (straight == 5 && flush == 5) { //has a straight and a flush
            hand = 8; //Hand is a straight flush
            System.out.println("FINAL HAND IS A STRAIGHT FLUSH");
            if (true) { //If the straight is 10 through Ace
                hand = 9; //Hand is a royal flush
                System.out.println("ROYAL FLUSH BRO!!!!!");
            }
        }
    }

    public void checkPair(Card[] board) {
        if (hole[0].value == hole[1].value) {
            pair1 = 2;
            pairValue1 = hole[0].value;
        }
        for (Card c : board) {
            if (hole[0].value == c.value) {
                if (pair1 >= 2 && c.value == pairValue1) {
                    pair1++;
                } else if (pair1 == 0) {
                    pair1 = 2;
                    pairValue1 = c.value;
                }
            }
            if (hole[1].value == c.value) {
                if (pair2 >= 2 && c.value == pairValue2) {
                    pair2++;
                } else if (pair2 == 0) {
                    pair2 = 2;
                    pairValue2 = c.value;
                }
            }
        }
        if ((pair1 == 2 && pair2 == 0) || (pair1 == 0 && pair2 == 2) && hand <= 1) { //If only 1 pair
            hand = 1; //Hand is a pair
        }
        if ((pair1 == 2 && pair2 == 2) && hand <= 2) { //If 2 pair
            hand = 2; //Hand is 2 pair
        }
        if (((pair1 == 3 && pair2 == 0) || (pair1 == 0 && pair2 == 3)) && hand <= 3) { //If 3 of a kind
            hand = 3; //Hand is 3 of a kind
        }
        if (((pair1 == 3 && pair2 == 2) || (pair1 == 2 && pair2 == 3)) && hand <= 6) { //If full house
            hand = 6; //Hand is a full house
        }
        if (((pair1 == 4 && pair2 == 0) || (pair1 == 0 && pair2 == 4)) && hand <= 7) { //If 4 of a kind
            hand = 7; //Hand is 4 of a kind
        }
    }

    public void checkFlush(Card[] board) {
        int suit1 = 0, suit2 = 0;
        if (hole[0].suit == hole[1].suit) {
            suit1 = 2;
        }
        for (Card c : board) {
            if (hole[0].suit == c.suit) {
                if (suit1 == 0) {
                    suit1 = 2;
                } else {
                    suit1++;
                }
            }
            if (hole[1].suit == c.suit) {
                if (suit2 == 0) {
                    suit2 = 2;
                } else {
                    suit2++;
                }
            }
        }
        if (suit1 >= 5 || suit2 >= 5) {
            flush = 5;
            if (hand < 5) {
                hand = 5;
            }
        }
    }
    
    /**
     * Prints the CPUs current stats
     */
    public void printInfo() {
        System.out.println("PLAYER INFO:");
        System.out.println("pair1 = " + pair1);
        System.out.println("pairValue1 = " + pairValue1);
        System.out.println("pair2 = " + pair2);
        System.out.println("pairValue2 = " + pairValue2);
        System.out.println("flush = " + flush);
        System.out.println("straight = " + straight);
        System.out.println("highCard = " + highCard);
        System.out.println("highSuit = " + highSuit);
        System.out.println("hand = " + hand);
        System.out.println("CHIPS REMAINING: " + chips);
    }
    
}
