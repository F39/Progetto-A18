package Utils;

import DatabaseManagement.User;

public class CommandMove extends AbstractCommand {

    private int gameId;
    private int move;

    public CommandMove(User user, int gameId, int move) {
        super(user);
        this.gameId = gameId;
        this.move = move;

    }

    public int getMove() {
        return move;
    }

    public int getGameId() {
        return gameId;
    }
}
