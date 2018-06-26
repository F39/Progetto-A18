package GameLogic.ArtificialIntelligence;

import java.util.Random;

/**
 * Artificial intelligence class; it makes a random move for the first level single player game
 */
public class AIStrategyRandom implements AIStrategyInt {

    @Override
    public int move() {
        return new Random().nextInt(7);
    }

}
