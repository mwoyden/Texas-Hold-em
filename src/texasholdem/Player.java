/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

public class Player extends Person {

    public Player() {
        chips = 0;
        bet = 0;
        hand = new Card[2];
        x = 0;
        y = 0;
        blind = 0;
        status = 0;
    }

    public Player(String n, int c, int xPos, int yPos, int b) {
        name = n;
        chips = c;
        hand = new Card[2];
        x = xPos;
        y = yPos;
        blind = b;
        status = 1;
    }
}
