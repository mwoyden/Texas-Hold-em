/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

public class CPU extends Person {

    public int pair;
    public int flush;
    public int straight;

    public CPU() {
        chips = 0;
        bet = 0;
        hand = new Card[2];
        x = 0;
        y = 0;
        status = 0;
        blind = 0;
        pair = 0;
        flush = 0;
        straight = 0;
    }

    public CPU(String n, int c, int xPos, int yPos, int b) {
        name = n;
        chips = c;
        hand = new Card[2];
        x = xPos;
        y = yPos;
        status = 1;
        blind = b;
        pair = 0;
        flush = 0;
        straight = 0;
    }

    @Override
    public void fold() {
        super.fold();
        pair = 0;
        flush = 0;
        straight = 0;
    }

    @Override
    public int decide(int round, int currentBet, Card[] board) {
        if (currentBet > chips) {
            fold();
            return 0;
        }
        int ret = 0;
        switch (round) {
            case 1:
                if (preFlop()) {
                    ret = currentBet;
                } else {
                    fold();
                }
                break;
            case 2:
                if (postFlop(board, round)) {
                    ret = currentBet;
                } else {
                    fold();
                }
                break;
            case 3:
                if (postTurn(board, round)) {
                    ret = currentBet;
                } else {
                    fold();
                }
                break;
            case 4:
                if (postRiver(board, round)) {
                    ret = currentBet;
                } else {
                    fold();
                }
                break;
            default:
                fold();
                break;
        }
        bet(ret);
        return ret;
    }

    public boolean preFlop() {
        boolean ret = false;
        if (hand[0].suit == hand[1].suit) {
            flush = 2;
            ret = true;
        }
        if (hand[0].value == hand[1].value) {
            pair = 2;
            ret = true;
        }
        if (hand[0].value == hand[1].value + 1) {
            straight = 2;
            ret = true;
        }
        if ((hand[0].value >= 10 && hand[1].value >= 10)) {
            ret = true;
        }
        return ret;
    }

    public boolean postFlop(Card[] board, int round) {
        boolean ret = false;
        if (pair > 0) {
            //raise(x);
            ret = true;
        } else if (checkPair(board, round)) {
            //raise(x);
            ret = true;
        }
        checkFlush(board, round);
        if (flush > 2) {
            ret = true;
            if (flush > 3) {
                //raise(x);
            }
        }
        checkStraight(board, round);
        if (straight > 2) {
            ret = true;
            if (straight > 3) {
                //raise(x);
            }
        }

        return ret;
    }

    public boolean checkStraight(Card[] board, int round) {
        boolean ret = false;
        int i = 0;
        for(Card c : board) {
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
        for (Card c : board) {
            if (hand[0].suit == board[i].suit || hand[1].suit == board[i].suit) {
                flush++;
                ret = true;
            }
            i++;
            if (i > round) {
                break;
            }
        }
        return ret;
    }

    public boolean checkPair(Card[] board, int round) {
        boolean ret = false;
        int i = 0;
        for (Card c : board) {
            System.out.println("CARD VALUE: " + c.value);
            if (hand[0].value == c.value || hand[1].value == c.value) {
                pair = 2;
                ret = true;
            }
            i++;
            if (i > round) {
                break;
            }
        }
        return ret;
    }

    public boolean postTurn(Card[] board, int round) {
        boolean ret = false;

        return ret;
    }

    public boolean postRiver(Card[] board, int round) {
        boolean ret = false;

        return ret;
    }
}
