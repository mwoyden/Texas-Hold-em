/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

public abstract class Person {

    public double chips;
    public double bet;
    public Card[] hand;
    public int x;
    public int y;
    public String name;
    public int blind;
    public int status;

    public void fold() {
        bet = 0;
        hand[0] = new Card();
        hand[1] = new Card();
        status = 0;
    }

    public void bet(int b) {
        bet = b;
        chips -= b;
    }
    
    public int decide(int round, int currentBet, Card[] board) {
        return 0;
    }
}
