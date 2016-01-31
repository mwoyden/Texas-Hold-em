/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

public class Card {
    
    public boolean dealt;
    public int id;
    public int suit;
    public int value;
    
    public Card() {
        dealt = false;
        id = -1;
        suit = -1;
        value = 0;
    }
    
    public Card(int i, int s, int v) {
        dealt = false;
        id = i;
        suit = s;
        value = v;
    }
}
