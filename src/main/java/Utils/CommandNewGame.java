package Utils;

import Controllers.GameController;
import DatabaseManagement.User;
import GameLogic.Match;
import GameLogic.Mode;
import GameLogic.Player;

public class CommandNewGame extends AbstractCommand {

    private Mode mode;

    public CommandNewGame(){

    }

    public CommandNewGame(String username, Mode mode) {
        super(username);
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void execute(GameController gameController) {
        int mode = getMode().getValue();
        if (mode == Mode.MultiPlayer.getValue()) {
            gameController.getMatchMaker().putPendingUsers(getUser());
        } else if (mode == Mode.SinglePlayerLevel1.getValue()) {
            createNewSinglePlayerGame(Mode.SinglePlayerLevel1, getUser(), gameController);
        } else {
            createNewSinglePlayerGame(Mode.SinglePlayerLevel2, getUser(), gameController);
        }
    }

    public void createNewSinglePlayerGame(Mode mode, User p1, GameController gameController) {
        Match newMatch;
        newMatch = new Match(mode, new Player(p1), new Player(mode), gameController.getProgressiveGameId());
        newMatch.attach(gameController);
        gameController.getMatches().put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        //TODO : Set logic for gameId
        //TODO : check for AI player
        //gameController.getRestController().putMessage(new CommandOut(p1.getUsername(),newMatch.getGameId(), newMatch.getMatchFlowState()));
        //gameController.getRestController().putMessage(new CommandOut(p2.getUsername(),newMatch.getGameId(), newMatch.getMatchFlowState()));
    }
}
