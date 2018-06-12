package GameLogic;

public enum Mode {
    MultiPlayer(0), StrategyRandom(1), StrategyNForecasting(2), test(3);

    private final int value;

    Mode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
