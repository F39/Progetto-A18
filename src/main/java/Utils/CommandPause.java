package Utils;

import DatabaseManagement.User;

public class CommandPause extends AbstractCommand {

    private int gameId;

    private CommandPause(User user, int gameId) {
        super(user);
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
