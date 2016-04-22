package texasholdem;

import java.io.InputStream;
import java.util.Random;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public abstract class Person {

    //Variables for hand control for both CPU and player
    public int chips; //chip found
    public int bet; //current bet given
    public Card[] hole; //Cards in the hole
    public int hand; //0 = junk, 1 = pair, 2 = 2 pair, 3 = 3 of a kind
    //4 = straight, 5 = flush, 6 = full house, 7 = 4 of a kind, 8 = straight flush, 9 = royal flush
    public int pairValue1; //If there's a pair, this is what the pair is 
    public int pairValue2; //If there is a second pair
    public int pair1; //How many are in the pair (2, 3 ,4)
    public int pair2; //How many in second pair
    public int highCard; //Highest card in the hole
    public int highSuit; //Highest suit in the hole
    public int flush; //How many cards are in the CPUs flush
    public int straight; //In the straight
    public int raise; //Amount to raise by
    public int x; //x position
    public int y; //y position
    public String name; //Name 
    public int status; //1 if playing the hand, 0 if folded

    /**
     * Fold by getting new cards and zeroing out variables
     */
    public void fold() {
        bet = 0; //Reset bet
        hole[0] = new Card(1, 1, 1);
        hole[1] = new Card(1, 1, 1);
        hand = 0; //Reset hand
        status = 0; //Remove from current hand
        Random ran = new Random();
        int x = ran.nextInt(4) + 1;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/texasholdem/sounds/cardShove" + x + ".wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
        }
    }

    /**
     * Bets
     *
     * @param b chips to bet by
     */
    public void bet(int b) {
        bet = b;
        chips -= b;
        Random ran = new Random();
        int x = ran.nextInt(6) + 1;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/texasholdem/sounds/chipsStack" + x + ".wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
        }
    }

    /**
     * Call (see CPU Override)
     *
     * @param round
     * @param currentBet
     * @param board
     * @return
     */
    public boolean call(int round, int currentBet, Card[] board) {
        Random ran = new Random();
        int x = ran.nextInt(6) + 1;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/texasholdem/sounds/chipsStack" + x + ".wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
        }
        return false;
    }

    public void call(int currentBet) {
        Random ran = new Random();
        int x = ran.nextInt(6) + 1;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/texasholdem/sounds/chipsStack" + x + ".wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
        }
    }

    /**
     * Decides (see CPU Override)
     *
     * @param round
     * @param currentBet
     * @param board
     * @param i
     * @param smallBlind
     * @param bigBlind
     * @return
     */
    public int decide(int round, int currentBet, Card[] board, int i, int smallBlind, int bigBlind) {
        return 0;
    }

    /**
     * Evaluates hand (see CPU Override)
     *
     * @param board
     * @param round
     */
    public void evaluate(Card[] board, int round) {
    }

    public void printInfo() {

    }
}
