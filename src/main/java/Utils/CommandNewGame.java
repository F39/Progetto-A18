package Utils;

import DatabaseManagement.User;
import GameLogic.Mode;

public class CommandNewGame extends AbstractCommand {

    private Mode mode;

    public CommandNewGame(User user, Mode mode) {
        super(user);
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
