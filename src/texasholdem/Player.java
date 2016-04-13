
package texasholdem;

public class Player extends Person {

    /**
     * Default constructor (not used)
     */
    public Player() {
        chips = 0;
        bet = 0;
        hole = new Card[2];
        x = 0;
        y = 0;
        status = 0;
    }

    /**
     * Alternate constructor (used)
     * @param n player name
     * @param c chip count
     * @param xPos x position
     * @param yPos y position
     */
    public Player(String n, int c, int xPos, int yPos) {
        name = n;
        chips = c;
        hole = new Card[2];
        x = xPos;
        y = yPos;
        status = 1;
    }
}
