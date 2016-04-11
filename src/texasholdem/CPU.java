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
            System.out.println("PLAYING AS SMALLBLIND");
        }
        if (i == bigBlind && round == 1) {
            currentBet = 0;
            System.out.println("PLAYING AS BIGBLIND");
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
                if (playRound(board, round, 50, 100, 50, 3, 4, 0.2)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POSTFLOP FOLD");
                    fold();
                }
                break;
            case 3:
                if (playRound(board, round, 50, 100, 50, 4, 5, 0.1)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POSTTURN FOLD");
                    fold();
                }
                break;
            case 4:
                if (playRound(board, round, 50, 100, 50, 5, 5, 0.05)) {
                    ret = currentBet + raise;
                    printCards();
                } else {
                    System.out.println("POSTRIVER FOLD");
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
            System.out.println("PLAYING FLUSH IN ROUND 1");
            ret = true;
        }
        //If cards have same value, play the pair
        if (hole[0].value == hole[1].value) {
            pair1 = 2;
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
        //If player has high card, play high card
        if (hole[0].value >= 12 || hole[1].value >= 12) {
            System.out.println("PLAYING HIGH CARD IN ROUND 1");
            if (hole[0].value >= hole[1].value) {
                highCard = hole[0].value;
            } else {
                highCard = hole[1].value;
            }
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

    public boolean playRound(Card[] board, int round, int raise2pair, int raiseFlush, int raiseStraight, int ifFlush, int ifStraight, double bluff) {
        boolean ret = false;
        if (pair1 > 0 || pair2 > 0) {
            ret = true;
            System.out.println("PLAYING PAIR IN ROUND " + round);
        }
        if (checkPair(board, round)) {
            System.out.println("PLAYING PAIR IN ROUND " + round);
            ret = true;
            if (pair1 >= 2 && pair2 >= 2) {
                raise(raise2pair);
            }
        }
        checkFlush(board, round);
        if (flush >= ifFlush) {
            ret = true;
            System.out.println("PLAYING FLUSH IN ROUND " + round);
            if (flush >= ifFlush + 1) {
                raise(raiseFlush);
            }
        }
        checkStraight(board, round);
        if (straight >= ifStraight) {
            ret = true;
            System.out.println("PLAYING STRAIGHT IN ROUND " + round);
            if (straight >= ifStraight + 1) {
                raise(raiseStraight);
            }
        }
        double x = Math.random();
        if (x < bluff && ret == false) {
            System.out.println("BLUFF IN ROUND " + round);
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
            System.out.println("ROUND IN PAIR CHECK: " + round);
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
