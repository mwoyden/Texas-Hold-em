/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

public class Dealer extends Person{
    
    public Dealer() {
        chips = Double.POSITIVE_INFINITY;
        hand = new Card[2];
        x = 100;
        y = 100;
    }
    
}
