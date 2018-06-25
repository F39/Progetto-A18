package Utils;

import Controllers.GameControllerInt;
import GameLogic.Player;

public class CommandAcceptMatch extends CommandMatch {
    private GameControllerInt gameController;
    private String opponentUsername;

    public CommandAcceptMatch() {

    }

    public void setGameController(GameControllerInt gameController) {
        this.gameController = gameController;
    }


    @Override
    public void execute() {
        Player opponentPlayer = findPlayer(opponentUsername);
        gameController.acceptGame(player, opponentPlayer, gameId);
    }
}
