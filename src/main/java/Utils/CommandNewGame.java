package Utils;

import Controllers.GameControllerInt;
import Controllers.UserController;
import GameLogic.Mode;
import GameLogic.Player;

public class CommandNewGame extends AbstractCommand {

    private Mode mode;
    private GameControllerInt gameController;
    private String opponentUsername;

    public CommandNewGame() {

    }

    public CommandNewGame(CommandNewGame commandNewGame) {
        super(commandNewGame.getUsername());
        this.mode = commandNewGame.mode;
    }

    public CommandNewGame(String username, Mode mode) {
        super(username);
        this.mode = mode;
    }

    public void setGameController(GameControllerInt gameController) {
        this.gameController = gameController;
    }

    public Mode getMode() {
        return mode;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    @Override
    public void execute() {
        if(opponentUsername != null){
            Player opponentPlayer = findPlayer(opponentUsername);
            gameController.createNewMultiPlayerGameWithFriend(player, opponentPlayer, mode);
        } else if (mode == Mode.MultiPlayer || mode == Mode.MultiPlayerTurbo) {
            gameController.getMatchMaker().putPendingUsers(player, mode);
        } else if (mode == Mode.StrategyNForecasting) {
            gameController.createNewSinglePlayerGame(Mode.StrategyNForecasting, player);
        } else {
            gameController.createNewSinglePlayerGame(Mode.StrategyRandom, player);
        }
    }

}
