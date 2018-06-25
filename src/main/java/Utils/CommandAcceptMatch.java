package Utils;

import Controllers.GameControllerInt;
import GameLogic.Player;

public class CommandAcceptMatch extends CommandMatch {
    private GameControllerInt gameController;
    private String opponentUsername;
    private int accepted;

    public CommandAcceptMatch() {

    }

    public void setGameController(GameControllerInt gameController) {
        this.gameController = gameController;
    }


    @Override
    public void execute() {
        Player opponentPlayer = findPlayer(opponentUsername);
        gameController.acceptGame(player, opponentPlayer, gameId, accepted);
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
}
