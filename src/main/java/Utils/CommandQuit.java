package Utils;

import DatabaseManagement.User;

public class CommandQuit extends AbstractCommand {

    private int gameId;

    public CommandQuit(User user, int gameId) {
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
