package texasholdem;

import java.util.HashMap;
import java.util.Map;

public class CPU extends Person {

    /**
     * Default constructor
     */
    public CPU() {
        chips = 0;
        bet = 0;
        hole = new Card[2];
        hand = 0;
        x = 0;
        y = 0;
        status = 0;
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
     * Alternate constructor (used)
     *
     * @param n name
     * @param c chip count
     * @param xPos x position
     * @param yPos y position
     */
    public CPU(String n, int c, int xPos, int yPos) {
        name = n;
        chips = c;
        bet = 0;
        hole = new Card[2];
        hand = 0;
        x = xPos;
        y = yPos;
        status = 1;
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
     * Allows the CPU to call the current bet
     *
     * @param round which round of betting
     * @param currentBet the bet they have to call
     * @param board not used
     * @return the new call bet
     */
    @Override
    public boolean call(int round, int currentBet, Card[] board) {
        boolean ret = false;
        if (currentBet > chips) {
            System.out.println("CALL FOLD");
            fold();
            return false;
        }
        double r = Math.random();
        double chance = r * currentBet; //Algorithm to decide if it is smart to call the bet based on current hand
        if (pair1 > 0 || straight > 0 || flush > 0 || pair2 > 0) {
            switch (hand) {
                case 0:
                    break;
                case 1:
                    if (chance <= 50) {
                        ret = true;
                    }
                    break;
                case 2:
                    if (chance <= 70) {
                        ret = true;
                    }
                    break;
                case 3:
                    if (chance <= 100) {
                        ret = true;
                    }
                    break;
                case 4:
                    if (chance <= 200) {
                        ret = true;
                    }
                    break;
                default:
                    ret = true;
                    break;
            }
        }
        if (r < 0.6) {
            ret = true;
        }
        if (ret) {
            chips -= (currentBet - bet);
            bet = currentBet;
        }
        return ret;
    }

    /**
     * Controls the CPUs betting cycle
     *
     * @param round current round of betting
     * @param currentBet the current bet
     * @param board the board
     * @param i the CPU index in players[]
     * @param smallBlind index of small blind
     * @param bigBlind index of big blind
     * @return returns the bet
     */
    @Override
    public int decide(int round, int currentBet, Card[] board, int i, int smallBlind, int bigBlind) {
        if (currentBet > chips) { //If the CPU doesn't have enough chips
            System.out.println("TOO FEW CHIPS FOLD");
            fold();
            return 0;
        }
        int ret = 0;
        raise = 0;
        if (i == smallBlind && round == 1) { //If CPU needs to call as small blind
            currentBet = 50;
        }
        if (i == bigBlind && round == 1) { //if CPU needs to call as big blind
            currentBet = 0;
        }
        //Decide based on which round of betting
        switch (round) {
            case 1: //Before the flop
                if (preFlop()) { //The CPU decided to play the hole
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("PREFLOP FOLD");
                    fold();
                }
                break;
            case 2: //After the flop
                if (playRound(board, round, 50, 100, 50, 3, 4, 0.4, 0.5)) { //The CPU decided to stay after the flop
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POST FLOP FOLD");
                    fold();
                }
                break;
            case 3: //After the turn
                if (playRound(board, round, 50, 100, 50, 4, 5, 0.2, 0.6)) { //The CPU decided to stay after the turn
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POST TURN FOLD");
                    fold();
                }
                break;
            case 4: //After the river
                if (playRound(board, round, 50, 100, 50, 5, 5, 0.1, 0.9)) { //The CPU decided to stay til the end
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POST RIVER FOLD");
                    fold();
                }
                break;
            default: //Erro catching
                System.out.println("DEFAULT FOLD");
                fold();
                break;
        }
        bet(ret);
        return ret;
    }

    /**
     * Prints the CPUs hand in the console for debugging
     */
    public void printCards() {
        System.out.println("CARDS:");
        String s = "";
        int i = 0;
        for (i = 0; i < 2; i++) {
            int st = hole[i].suit;
            int v = hole[i].value;
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
     * CPU may decide to raise
     *
     * @param b maximum raise amount
     * @param chance
     */
    public void raise(int b, double chance) {
        int r = (int) Math.floor(Math.random() * b + 1); //Decide how much to raise by
        if (r <= chips) { //If they can afford the raise
            if (r >= 0 && r < 10) { //Makes sure to bet in increments of 10
                r = 10;
            } else if (r >= 10 && r < 20) {
                r = 20;
            } else if (r >= 20 && r < 30) {
                r = 30;
            } else if (r >= 30 && r < 40) {
                r = 40;
            } else if (r >= 40 && r < 50) {
                r = 50;
            } else if (r >= 50 && r < 60) {
                r = 60;
            } else if (r >= 60 && r < 70) {
                r = 70;
            } else if (r >= 70 && r < 80) {
                r = 80;
            } else if (r >= 80 && r < 90) {
                r = 90;
            } else if (r >= 90 && r < 100) {
                r = 100;
            } else if (r >= 100 && r < 100) {
                r = 110;
            } else if (r >= 110 && r < 120) {
                r = 120;
            } else if (r >= 120 && r < 130) {
                r = 130;
            } else if (r >= 130 && r < 140) {
                r = 140;
            } else if (r >= 140 && r < 150) {
                r = 150;
            } else if (r >= 150 && r < 160) {
                r = 160;
            } else if (r >= 160 && r < 170) {
                r = 170;
            } else if (r >= 170 && r < 180) {
                r = 180;
            } else if (r >= 180 && r < 190) {
                r = 190;
            } else if (r >= 190 && r < 200) {
                r = 200;
            }
            double x = Math.random();
            if (x < chance) { //Chance of raise
                raise += r;
                System.out.println("RAISE BY " + r + "!");
            }
        }
    }

    /**
     * Starting logic for the CPU to play a hand
     *
     * @return true if they decide to play, false if they fold
     */
    public boolean preFlop() {
        boolean ret = false;
        //If cards have same suit, play the flush
        if (hole[0].suit == hole[1].suit) {
            flush = 2;
            highSuit = hole[0].suit;
            System.out.println("PLAYING FLUSH IN ROUND 1");
            ret = true;
        }
        //If cards have same value, play the pair
        if (hole[0].value == hole[1].value) {
            pair1 = 2;
            hand = 1;
            pairValue1 = hole[0].value;
            System.out.println("PLAYING PAIR IN ROUND 1");
            ret = true;
        }
        //If cards are sequential, play the straight
        if (hole[0].value == hole[1].value + 1) {
            straight = 2;
            System.out.println("PLAYING STRAIGHT IN ROUND 1");
            ret = true;
        }
        if (hole[0].value == hole[1].value - 1) {
            straight = 2;
            System.out.println("PLAYING STRAIGHT IN ROUND 1");
            ret = true;
        }
        //Find their highest card value
        if (hole[0].value >= hole[1].value) {
            highCard = hole[0].value;
        } else {
            highCard = hole[1].value;
        }
        //If player has high card, play high card
        if (highCard >= 12) {
            System.out.println("PLAYING HIGH CARD IN ROUND 1");
            ret = true;
        }
        //Bluff chance
        double x = Math.random();
        if (x < 0.8 && ret == false) {
            System.out.println("BLUFF IN ROUND 1");
            ret = true;
        }
        printInfo(); //Print CPU evaluated values for debugging
        return ret;
    }

    /**
     * Prints the CPUs current stats
     */
    @Override
    public void printInfo() {
        System.out.println("CPU INFO:");
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

    /**
     * Evaluates the CPUs final hand
     *
     * @param board the board
     * @param round
     */
    @Override
    public void evaluate(Card[] board, int round) {
        //Finds if the board has a pair on it
        Map<Integer, Integer> m = new HashMap();
        int i = 0;
        for (Card c : board) {
            if (m.containsKey(c.value)) {
                m.put(c.value, m.get(c.value) + 1);
            } else {
                m.put(c.value, 1);
            }
            i++;
            if (i > round) {
                break;
            }
        }
        //Variables to store board pairs
        int pairB2 = 0, pairValueB2 = 0, pairB3 = 0, pairValueB3 = 0;
        for (int y : m.keySet()) {
            if (m.get(y) == 2) { //If there is a pair
                pairB2 = m.get(y);
                pairValueB2 = y;
            }
            if (m.get(y) == 3) { //If there is 3 of a kind
                pairB3 = m.get(y);
                pairValueB3 = y;
            }
        }
        //Evaluates the CPUs final hand
        if (hand == 1) { //If the CPU has a pair
            if (pairB2 == 2 && pairValueB2 != pairValue1 && pairValueB2 != pairValue2) { //If the board has a pair
                hand = 2; //CPUs hand becomes 2 pair
                System.out.println("HAND IS 2 PAIR");
            } else if (pairB3 == 3 && pairValueB3 != pairValue1 && pairValueB3 != pairValue2) { //If the board has 3 of a kind
                hand = 6; //CPUs hand becomes full house
                System.out.println("HAND IS A FULL HOUSE");
            } else { //If the board was pairless
                System.out.println("HAND IS A PAIR");
            }
        }
        if (hand == 3) { //If CPU has 3 of a kind
            if (pairB2 == 2 && pairValueB2 != pairValue1 && pairValueB2 != pairValue2) { //If the board has a pair
                hand = 6; //CPUs hand becomes full house
                System.out.println("HAND IS A FULL HOUSE");
            }
        }
        if (straight == 5 && flush == 5) { //If the CPU has a straight and a flush
            hand = 8; //Hand is a straight flush
            System.out.println("HAND IS A STRAIGHT FLUSH");
            if (true) { //If the straight is 10 through Ace
                hand = 9; //Hand is a royal flush
                System.out.println("ROYAL FLUSH BRO!!!!!");
            }
        }
    }

    /**
     * Decision to play every round after the flop
     *
     * @param board the board
     * @param round the round of betting
     * @param raise2pair maximum raise value for having 2 pair
     * @param raiseFlush maximum raise value for having a flush
     * @param raiseStraight .. a straight
     * @param ifFlush the minimum cards in a flush a CPU must have to play
     * @param ifStraight the minimum cards in a straight a CPU must have to play
     * @param bluff bluff chance for this round
     * @return true if the CPU will play, false if they fold
     */
    public boolean playRound(Card[] board, int round, int raise2pair, int raiseFlush, int raiseStraight, int ifFlush, int ifStraight, double bluff, double chance) {
        boolean ret = false; //Initialize return variable to false
        evaluate(board, round);
        if (checkPair(board, round)) { //Check all pair possibilities
            if ((pair1 == 2 && pair2 == 0) || (pair1 == 0 && pair2 == 2) && hand <= 1) { //If CPU has only 1 pair
                System.out.println("PLAYING 1 PAIR IN ROUND " + round);
                hand = 1; //Hand is a pair
            }
            if ((pair1 == 2 && pair2 == 2) && hand <= 2) { //If CPU has 2 pair
                System.out.println("PLAYING 2 PAIR IN ROUND " + round);
                hand = 2; //Hand is 2 pair
                raise(raise2pair, chance); //Raise with a change
            }
            if (((pair1 == 3 && pair2 == 0) || (pair1 == 0 && pair2 == 3)) && hand <= 3) { //IF CPU has 3 of a kind
                System.out.println("PLAYING 3 OF A KIND IN ROUND " + round);
                hand = 3; //Hand is 3 of a kind
                raise(2 * raise2pair, chance); //Raise chance with higher maximum raise value
            }
            if (((pair1 == 3 && pair2 == 2) || (pair1 == 2 && pair2 == 3)) && hand <= 6) { //If the CPU has a full house (pretty sure this can't be reached)
                System.out.println("PLAYING FULL HOUSE IN ROUND " + round);
                hand = 6; //Hand is a full house
                raise(3 * raise2pair, chance); //Raise with a higher ceiling
            }
            if (((pair1 == 4 && pair2 == 0) || (pair1 == 0 && pair2 == 4)) && hand <= 7) { //If the CPU has 4 of a kind
                System.out.println("PLAYING 4 OF A KIND IN ROUND " + round);
                hand = 7; //Hand is 4 of a kind
                raise(4 * raise2pair, chance); //Raise with highest ceiling
            }
            ret = true; //Play the hand
        }
        checkStraight(board, round); //Check all straight possibilites
        if (straight >= ifStraight) { //If the CPU has the required cards in a row for a straight in this round
            ret = true; //play the hand
            System.out.println("PLAYING STRAIGHT IN ROUND " + round);
            if (straight >= ifStraight + 1) { //If the CPU has more than the expected straight values
                raise(raiseStraight, chance); //Possibly raise
            }
            if (straight >= 5 && hand <= 4) { //If the CPU completed the straight
                System.out.println("HAND IS A STRAIGHT");
                hand = 4; //Hand is a straight
            }
        }
        checkFlush(board, round); //Check for a flush
        if (flush >= ifFlush) { //If the CPU has the required cards for a flush in this round
            ret = true; //play the flush
            System.out.println("PLAYING FLUSH IN ROUND " + round);
            if (flush >= ifFlush + 1) { //I the CPU has more than the expected flush values
                raise(raiseFlush, chance); //Possibly raise
            }
            if (flush >= 5 && hand <= 5) { //If the CPU has a flush
                System.out.println("HAND IS A FLUSH");
                hand = 5; //Hand is a flush
            }
        }
        if (flush >= 5 && straight >= 5) {
            System.out.println("ROYAL FLUSH BRO. ROYAL FLUSH. BRO.");
            hand = 9;
            raise(chips, chance);
        }
        double x = Math.random();
        if (x < bluff && ret == false) { //Chance of bluffing
            System.out.println("BLUFF IN ROUND " + round);
            ret = true;
        }
        printInfo(); //Print CPU evaluated values for debugging
        return ret;
    }

    /**
     * Checks for a straight
     *
     * @param board the board
     * @param round the round of betting
     * @return
     */
    public boolean checkStraight(Card[] board, int round) {
        boolean ret = false;
        int i = 0;
        for (Card c : board) {
            //This is gonna be really hard
            i++;
            if (i > round) {
                break;
            }
        }
        return ret;
    }

    /**
     * Checks for a flush
     *
     * @param board the board
     * @param round the round of betting
     * @return
     */
    public boolean checkFlush(Card[] board, int round) {
        boolean ret = false; //Initialize return variable
        int i = 0;
        if (round < 3) { //If the round is the flop
            for (Card c : board) { //For each card in the flop
                if (hole[0].suit == c.suit || hole[1].suit == c.suit) { //If they match the cards in the hole
                    if (flush == 0) { //If the hole didn't match
                        flush = 2; //The flush is set to 2 for the first card that matches
                        highSuit = c.suit; //And the suit is set 
                    } else if (c.suit == highSuit) { //If the flush matches the hole suit
                        flush++;
                    }
                    if (flush >= 3) { //If the flush after the flop has at least 3 cards
                        ret = true; //play the flush
                    }
                }
                i++;
                if (i > round) { //Makes sure the for loop doesn't read null cards
                    break;
                }
            }
        } else if (round >= 3) { //If the round is the turn or the river
            if (hole[0].suit == board[round].suit || hole[1].suit == board[round].suit) { //Check the newly dealt card for a flush
                if (flush == 0) { //If the flush is still 0
                    flush = 2; //Set the flush to 2
                    highSuit = board[round].suit; //and set the suit
                } else if (board[round].suit == highSuit) { //If this continues a flush
                    flush++;
                }
                if (flush >= 4) { //If the flush has 4 or more cards.. even if its the river
                    ret = true; //play the flush
                }
            }
        }
        return ret;
    }

    /**
     * Check all pair possibilities
     *
     * @param board the board
     * @param round the round of betting
     * @return true if there is a pair, false otherwise
     */
    public boolean checkPair(Card[] board, int round) {
        boolean ret = false; //Initialize return variable
        int i = 0;
        if (pair1 >= 2 || pair2 >= 2) { //If there is already a pair
            ret = true; //Play the existing pair
        }
        if (round < 3) { // on the flop
            for (Card c : board) { // for the 3 cards in the flop
                if (hole[0].value == c.value || hole[1].value == c.value) { //if there is any kind of match
                    if (pair1 >= 2 && c.value == pairValue1) { //looking for 3 or 4 of a kind, already pair in hole
                        pair1++;
                        ret = true;
                    } else if (pair1 == 0) { //no pair in hole, looking for 2 pair
                        if (hole[0].value == c.value) { //look for first pair
                            if (pair1 == 0) { //If there is a pair between the hole and the board
                                pair1 = 2;
                                pairValue1 = c.value;
                            } else { //If this makes 3 of a kind
                                pair1++;
                            }
                            ret = true;
                        }
                        if (hole[1].value == c.value) { //Check for second pair
                            if (pair2 == 0) { //If there is a pair between the second hole card and the board
                                pair2 = 2;
                                pairValue2 = c.value;
                            } else {
                                pair2++; //If there is a 3 of a kind with the second hole card
                            }
                            ret = true;
                        }
                    }
                }
                i++;
                if (i > round) { //Makes sure the loop doesn't read null cards
                    break;
                }
            }
        } else if (round >= 3) { //If the round is the turn of the river
            if (hole[0].value == board[round].value) { //If the newly dealt card matches a card in the hole
                if (hole[0].value == pairValue1) { //If it makes 3+ of a kind 
                    pair1++;
                    ret = true;
                } else { //If this makes a new pair
                    pair1 = 2;
                    pairValue1 = hole[0].value;
                    ret = true;
                }
            }
            if (hole[1].value == board[round].value) { //If the newly dealt card matches the second card in the hole
                if (hole[1].value == pairValue2) { //If it makes 3+ or a kind
                    pair2++;
                    ret = true;
                } else { //If this makes a new pair
                    pair2 = 2;
                    pairValue2 = hole[1].value;
                    ret = true;
                }
            }
        }
        return ret;
    }
}
