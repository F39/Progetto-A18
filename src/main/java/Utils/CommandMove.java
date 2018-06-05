package Utils;

import Controllers.GameController;
import DatabaseManagement.User;
import GameLogic.Match;

public class CommandMove extends AbstractCommand {

    private int gameId;
    private int move;

    public CommandMove(){

    }

    public CommandMove(String username, int gameId, int move) {
        super(username);
        this.gameId = gameId;
        this.move = move;

    }

    public int getMove() {
        return move;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public void execute(GameController gameController) {
        Match handledMatch = gameController.getMatches().get(getGameId());
        handledMatch.setLastMove(getMove());
        //TODO : Check for right turn
        User userToNotify = gameController.getUserToNotify(handledMatch, getUser());
        gameController.getRestController().putMessage(new CommandOut(userToNotify.getUsername(), handledMatch.getGameId(), handledMatch.getMatchFlowState()));
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setMove(int move) {
        this.move = move;
    }
}
