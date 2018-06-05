package Utils;

import Controllers.GameController;
import DatabaseManagement.User;

public class CommandQuit extends AbstractCommand {

    private int gameId;

    public CommandQuit(String username, int gameId) {
        super(username);
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public void execute(GameController gameController) {
        //TODO : definire logica di quit
    }
}
