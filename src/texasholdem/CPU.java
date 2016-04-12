/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

import java.util.HashMap;
import java.util.Map;

public class CPU extends Person {

    public CPU() {
        chips = 0;
        bet = 0;
        hole = new Card[2];
        hand = 0;
        x = 0;
        y = 0;
        status = 0;
        blind = 0;
        pairValue1 = 0;
        pairValue2 = 0;
        pair1 = 0;
        pair2 = 0;
        highCard = 0;
        highSuit = -1;
        flush = 0;
        straight = 0;
    }

    public CPU(String n, int c, int xPos, int yPos, int b) {
        name = n;
        chips = c;
        bet = 0;
        hole = new Card[2];
        hand = 0;
        x = xPos;
        y = yPos;
        status = 1;
        blind = b;
        pairValue1 = 0;
        pairValue2 = 0;
        pair1 = 0;
        pair2 = 0;
        highCard = 0;
        highSuit = -1;
        flush = 0;
        straight = 0;
    }

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

    @Override
    public boolean call(int round, int currentBet, Card[] board) {
        boolean ret = false;
        if (currentBet > chips) {
            System.out.println("CALL FOLD");
            fold();
            return false;
        }
        if (pair1 > 0 || straight > 0 || flush > 0) {
            ret = true;
            bet(currentBet - bet);
        }
        return ret;
    }

    @Override
    public int decide(int round, int currentBet, Card[] board, int i, int smallBlind, int bigBlind) {
        if (currentBet > chips) {
            System.out.println("TOO FEW CHIPS FOLD");
            fold();
            return 0;
        }
        int ret = 0;
        raise = 0;
        if (i == smallBlind && round == 1) {
            currentBet = 50;
            System.out.println("PLAYING AS SMALL BLIND");
        }
        if (i == bigBlind && round == 1) {
            currentBet = 0;
            System.out.println("PLAYING AS BIG BLIND");
        }
        switch (round) {
            case 1:
                if (preFlop()) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("PREFLOP FOLD");
                    fold();
                }
                break;
            case 2:
                if (playRound(board, round, 50, 100, 50, 3, 4, 0.4)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POST FLOP FOLD");
                    fold();
                }
                break;
            case 3:
                if (playRound(board, round, 50, 100, 50, 4, 5, 0.2)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POST TURN FOLD");
                    fold();
                }
                break;
            case 4:
                if (playRound(board, round, 50, 100, 50, 5, 5, 0.1)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POST RIVER FOLD");
                    fold();
                }
                break;
            default:
                System.out.println("DEFAULT FOLD");
                fold();
                break;
        }
        bet(ret);
        return ret;
    }

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

    public void raise(int b) {
        int r = (int) Math.floor(Math.random() * b + 1);
        if (r <= chips) {
            double x = Math.random();
            if (x < 0.5) {
                raise += r;
                System.out.println("RAISE BY " + r + "!");
            }
        }
    }

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
        return ret;
    }

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
    }
    
    @Override
    public void evaluate(Card[] board) {
        //Finds if the board has a pair on it
        Map<Integer, Integer> m = new HashMap();
        for (Card c : board) {
            if (m.containsKey(c.value)) {
                m.put(c.value, m.get(c.value) + 1);
            } else {
                m.put(c.value, 1);
            }
        }
        int pairB2 = 0, pairValueB2 = 0, pairB3 = 0, pairValueB3 = 0;
        System.out.println("CHECKING FOR DUPLICATES...");
        for (int y : m.keySet()) {
            if (m.get(y) == 2) {
                pairB2 = m.get(y);
                pairValueB2 = y;
                System.out.println("THERE ARE " + pairB2 + " " + pairValueB2 + "s ON THE BOARD");
            }
            if (m.get(y) == 3) {
                pairB3 = m.get(y);
                pairValueB3 = y;
                System.out.println("THERE ARE " + pairB3 + " " + pairValueB3 + "s ON THE BOARD");
            }
        }
        //Evaluates the CPUs final hand
        if (hand == 1) {
            if (pairB2 == 2 && pairValueB2 != pairValue1 && pairValueB2 != pairValue2) {
                hand = 2;
                System.out.println("FINAL HAND IS 2 PAIR");
            } else if (pairB3 == 3 && pairValueB3 != pairValue1 && pairValueB3 != pairValue2) {
                hand = 6;
                System.out.println("FINAL HAND IS A FULL HOUSE");
            } else {
                System.out.println("FINAL HAND IS A PAIR");
            }
        }
        if (hand == 3) {
            if (pairB2 == 2 && pairValueB2 != pairValue1 && pairValueB2 != pairValue2) {
                hand = 6;
                System.out.println("FINAL HAND IS A FULL HOUSE");
            }
        }
        if (straight == 5 && flush == 5) {
            hand = 8;
            System.out.println("FINAL HAND IS A STRAIGHT FLUSH");
            if (true) {
                hand = 9;
                System.out.println("ROYAL FLUSH BRO!!!!!");
            }
        }
    }

    public boolean playRound(Card[] board, int round, int raise2pair, int raiseFlush, int raiseStraight, int ifFlush, int ifStraight, double bluff) {
        boolean ret = false;
        if (checkPair(board, round)) {
            if ((pair1 == 2 && pair2 == 0) || (pair1 == 0 && pair2 == 2) && hand <= 1) {
                System.out.println("PLAYING 1 PAIR IN ROUND " + round);
                hand = 1;
            }
            if ((pair1 == 2 && pair2 == 2) && hand <= 2) {
                System.out.println("PLAYING 2 PAIR IN ROUND " + round);
                hand = 2;
                raise(raise2pair);
            }
            if (((pair1 == 3 && pair2 == 0) || (pair1 == 0 && pair2 == 3)) && hand <= 3) {
                System.out.println("PLAYING 3 OF A KIND IN ROUND " + round);
                hand = 3;
                raise(2 * raise2pair);
            }
            if (((pair1 == 3 && pair2 == 2) || (pair1 == 2 && pair2 == 3)) && hand <= 6) {
                System.out.println("PLAYING FULL HOUSE IN ROUND " + round);
                hand = 6;
                raise(3 * raise2pair);
            }
            if (((pair1 == 4 && pair2 == 0) || (pair1 == 0 && pair2 == 4)) && hand <= 7) {
                System.out.println("PLAYING 4 OF A KIND IN ROUND " + round);
                hand = 7;
                raise(4 * raise2pair);
            }
            ret = true;
        }
        checkStraight(board, round);
        if (straight >= ifStraight) {
            ret = true;
            System.out.println("PLAYING STRAIGHT IN ROUND " + round);
            if (straight >= ifStraight + 1) {
                raise(raiseStraight);
            }
            if (straight >= 5 && hand <= 4) {
                System.out.println("HAND IS A STRAIGHT");
                hand = 4;
            }
        }
        checkFlush(board, round);
        if (flush >= ifFlush) {
            ret = true;
            System.out.println("PLAYING FLUSH IN ROUND " + round);
            if (flush >= ifFlush + 1) {
                raise(raiseFlush);
            }
            if (flush >= 5 && hand <= 5) {
                System.out.println("HAND IS A FLUSH");
                hand = 5;
            }
        }
        double x = Math.random();
        if (x < bluff && ret == false) {
            System.out.println("BLUFF IN ROUND " + round);
            ret = true;
        }
        printInfo();

        return ret;
    }

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

    public boolean checkFlush(Card[] board, int round) {
        boolean ret = false;
        int i = 0;
        if (round < 3) {
            for (Card c : board) {
                if (hole[0].suit == c.suit || hole[1].suit == c.suit) {
                    if (flush == 0) {
                        flush = 2;
                        highSuit = c.suit;
                    } else if (c.suit == highSuit) {
                        flush++;
                    }
                    if (flush >= 3) {
                        ret = true;
                    }
                }
                i++;
                if (i > round) {
                    break;
                }
            }
        } else if (round >= 3) {
            if (hole[0].suit == board[round].suit || hole[1].suit == board[round].suit) {
                if (flush == 0) {
                    flush = 2;
                    highSuit = board[round].suit;
                } else if (board[round].suit == highSuit) {
                    flush++;
                }
                if (flush >= 4) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public boolean checkPair(Card[] board, int round) {
        boolean ret = false;
        int i = 0;
        if (pair1 >= 2 || pair2 >= 2) {
            ret = true;
        }
        if (round < 3) { // on the flop
            for (Card c : board) { // for the 3 cards in the flop
                if (hole[0].value == c.value || hole[1].value == c.value) { //if there is any kind of match
                    if (pair1 >= 2 && c.value == pairValue1) { //looking for 3 or 4 of a kind, already pair in hole
                        pair1++;
                        ret = true;
                    } else if (pair1 == 0) { //no pair in hole, looking for 2 pair
                        if (hole[0].value == c.value) {
                            if (pair1 == 0) {
                                pair1 = 2;
                                pairValue1 = c.value;
                            } else {
                                pair1++;
                            }
                            ret = true;
                        }
                        if (hole[1].value == c.value) {
                            if (pair2 == 0) {
                                pair2 = 2;
                                pairValue2 = c.value;
                            } else {
                                pair2++;
                            }
                            ret = true;
                        }
                    }
                }
                i++;
                if (i > round) {
                    break;
                }
            }
        } else if (round >= 3) {
            System.out.println("PAIR CHECK IN ROUND: " + round);
            System.out.println("CARD VALUE: " + board[round].value);
            if (hole[0].value == board[round].value /*|| hole[1].value == board[round].value*/) { // NULL POINTER SOMETIMES
                if (hole[0].value == pairValue1) {
                    pair1++;
                    ret = true;
                } else {
                    pair1 = 2;
                    pairValue1 = hole[0].value;
                    ret = true;
                }
            }
            if (hole[1].value == board[round].value) {
                if (hole[1].value == pairValue2) {
                    pair2++;
                    ret = true;
                } else {
                    pair2 = 2;
                    pairValue2 = hole[1].value;
                    ret = true;
                }
            }
        }
        return ret;
    }
}
