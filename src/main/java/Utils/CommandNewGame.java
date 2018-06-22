package Utils;

import Controllers.GameControllerInt;
import GameLogic.Mode;

public class CommandNewGame extends AbstractCommand {

    private Mode mode;
    private GameControllerInt gameController;

    public CommandNewGame(){

    }

    public CommandNewGame(CommandNewGame commandNewGame){
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

    @Override
    public void execute() {
        if (mode == Mode.MultiPlayer) {
            gameController.getMatchMaker().putPendingUsers(player);
        } else if (mode == Mode.StrategyNForecasting) {
            gameController.createNewSinglePlayerGame(Mode.StrategyNForecasting, player);
        } else {
            gameController.createNewSinglePlayerGame(Mode.StrategyRandom, player);
        }
    }

}
