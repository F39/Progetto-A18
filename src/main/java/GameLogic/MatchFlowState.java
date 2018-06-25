package GameLogic;

public enum MatchFlowState {
    started1(0), started2(9), running(1), paused(2), quitted(3), winner1(4), winner2(5), tie(6), winner(7), looser(8), resumed(10), refused(11);

    private final int value;

    MatchFlowState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
