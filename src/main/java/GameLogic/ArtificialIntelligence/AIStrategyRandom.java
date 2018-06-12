package GameLogic.ArtificialIntelligence;

import java.util.Random;

public class AIStrategyRandom implements AIStrategyInt {

    @Override
    public int move() {
        return new Random().nextInt(7);
    }

}
