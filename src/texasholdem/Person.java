/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

public abstract class Person {
    
    public double chips = 0;
    public double bet = 0;
    public Card[] hand;
    public int x;
    public int y;
    
    public void fold() {
        bet = 0;
        hand[0] = null;
        hand[0].dealt = false;
        hand[1] = null;
        hand[1].dealt = false;
    }    
}
