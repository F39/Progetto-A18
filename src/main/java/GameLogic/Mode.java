package GameLogic;

public enum Mode {
    MultiPlayer(0), SinglePlayerLevel1(1), SinglePlayerLevel2(2);

    private final int value;

    Mode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
