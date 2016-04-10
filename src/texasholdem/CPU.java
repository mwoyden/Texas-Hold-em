/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

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
        pair2= 0;
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
        pair2= 0;
        flush = 0;
        straight = 0;
    }

    @Override
    public void fold() {
        super.fold();
        pairValue1 = 0;
        pairValue2 = 0;
        pair1 = 0;
        pair2= 0;
        flush = 0;
        straight = 0;
    }

    @Override
    public boolean call(int round, int currentBet, Card[] board) {
        boolean ret = false;
        if (currentBet > chips) {
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
    public int decide(int round, int currentBet, Card[] board) {
        if (currentBet > chips) {
            fold();
            return 0;
        }
        int ret = 0;
        raise = 0;
        switch (round) {
            case 1:
                if (preFlop()) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    fold();
                }
                break;
            case 2:
                if (postFlop(board, round)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    fold();
                }
                break;
            case 3:
                if (postTurn(board, round)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    fold();
                }
                break;
            case 4:
                if (postRiver(board, round)) {
                    ret = currentBet + raise;
                    printCards();
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
            System.out.println("PLAYING FLUSH IN ROUND 1");
            ret = true;
        }
        //If cards have same value, play the pair
        if (hole[0].value == hole[1].value) {
            pair1 = 2;
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
        //If player has high card, play high card
        if (hole[0].value >= 12 || hole[1].value >= 12) {
            System.out.println("PLAYING HIGH CARD IN ROUND 1");
            ret = true;
        }
        //Bluff chance
        double x = Math.random();
        if (x < 0.1 && ret == false) {
            System.out.println("BLUFF IN ROUND 1");
            ret = true;
        }
        return ret;
    }

    public boolean postFlop(Card[] board, int round) {
        boolean ret = false;
        if (pair1 > 0) {
            ret = true;
            System.out.println("PLAYING PAIR IN ROUND 2");
        } else if (checkPair(board, round)) {
            System.out.println("PLAYING PAIR IN ROUND 2");
            ret = true;
            raise(50);
        }
        checkFlush(board, round);
        if (flush >= 3) {
            ret = true;
            System.out.println("PLAYING FLUSH IN ROUND 2");
            if (flush >= 4) {
                raise(50);
            }
        }
        checkStraight(board, round);
        if (straight >= 3) {
            ret = true;
            System.out.println("PLAYING STRAIGHT IN ROUND 2");
            if (straight >= 4) {
                raise(50);
            }
        }
        double x = Math.random();
        if (x < 0.2 && ret == false) {
            System.out.println("BLUFF IN ROUND 2");
            ret = true;
        }

        return ret;
    }

    public boolean postTurn(Card[] board, int round) {
        boolean ret = false;
        if (pair1 > 0) {
            ret = true;
            System.out.println("PLAYING PAIR IN ROUND 3");
        } else if (checkPair(board, round)) {
            System.out.println("PLAYING PAIR IN ROUND 3");
            ret = true;
            raise(50);
        }
        checkFlush(board, round);
        if (flush >= 4) {
            ret = true;
            System.out.println("PLAYING FLUSH IN ROUND 3");
            if (flush >= 5) {
                raise(50);
            }
        }
        checkStraight(board, round);
        if (straight >= 4) {
            ret = true;
            System.out.println("PLAYING STRAIGHT IN ROUND 3");
            if (straight >= 5) {
                raise(50);
            }
        }
        double x = Math.random();
        if (x < 0.1 && ret == false) {
            System.out.println("BLUFF IN ROUND 3");
            ret = true;
        }

        return ret;
    }

    public boolean postRiver(Card[] board, int round) {
        boolean ret = false;
        if (pair1 > 0) {
            ret = true;
            System.out.println("PLAYING PAIR IN ROUND 4");
        } else if (checkPair(board, round)) {
            System.out.println("PLAYING PAIR IN ROUND 4");
            ret = true;
            raise(50);
        }
        checkFlush(board, round);
        if (flush >= 5) {
            ret = true;
            System.out.println("PLAYING FLUSH IN ROUND 4");
            raise(50);
        }
        checkStraight(board, round);
        if (straight >= 5) {
            ret = true;
            System.out.println("PLAYING STRAIGHT IN ROUND 4");
            raise(100);
        }
        double x = Math.random();
        if (x < 0.05 && ret == false) {
            System.out.println("BLUFF IN ROUND 3");
            ret = true;
        }

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
                    flush++;
                    ret = true;
                }
                i++;
                if (i > round) {
                    break;
                }
            }
        } else if (round >= 3) {
            if (hole[0].suit == board[round].suit || hole[1].suit == board[round].suit) {
                flush++;
                ret = true;
            }
        }
        return ret;
    }

    public boolean checkPair(Card[] board, int round) {
        boolean ret = false;
        int i = 0;
        if (round < 3) {
            for (Card c : board) {
                if (hole[0].value == c.value || hole[1].value == c.value) {
                    pair1++;
                    ret = true;
                }
                i++;
                if (i > round) {
                    break;
                }
            }
        } else if (round >= 3) {
            if (hole[0].value == board[round].value || hole[1].value == board[round].value) {
                pair1++;
                ret = true;
            }
        }
        return ret;
    }
}
