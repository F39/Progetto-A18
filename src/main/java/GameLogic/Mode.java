package GameLogic;

import java.util.HashMap;
import java.util.Map;

public enum Mode {
    MultiPlayer(0), StrategyRandom(1), StrategyNForecasting(2), test(3);

    private final int value;
    private static Map<Integer, Mode> map = new HashMap<>();

    static {
        for (Mode mode : Mode.values()) {
            map.put(mode.value, mode);
        }
    }

    Mode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Mode valueOf(int n) {
        return map.get(n);
    }
}
