package GameLogic;

public enum MatchFlowState {
    started(0), running(1), paused(2), quitted(3), finished(4);

    private final int value;

    MatchFlowState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
