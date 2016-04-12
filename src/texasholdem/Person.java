/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

public abstract class Person {

    public double chips;
    public int bet;
    public Card[] hole;
    public int hand; //0 = junk, 1 = pair, 2 = 2 pair, 3 = 3 of a kind
    //4 = straight, 5 = flush, 6 = full house, 7 = 4 of a kind, 8 = straight flush, 9 = royal flush
    public int pairValue1;
    public int pairValue2;
    public int pair1;
    public int pair2;
    public int highCard;
    public int highSuit;
    public int flush;
    public int straight;
    public int raise;
    public int x;
    public int y;
    public String name;
    public int blind;
    public int status;

    public void fold() {
        bet = 0;
        hole[0] = new Card();
        hole[1] = new Card();
        hand = 0;
        status = 0;
    }

    public void bet(int b) {
        bet = b;
        chips -= b;
    }

    public boolean call(int round, int currentBet, Card[] board) {
        return false;
    }

    public int decide(int round, int currentBet, Card[] board, int i, int smallBlind, int bigBlind) {
        return 0;
    }
    
    public void evaluate(Card[] board) {
    }
}
