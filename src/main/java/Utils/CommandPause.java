package Utils;

import Controllers.GameController;
import DatabaseManagement.User;
import GameLogic.Match;
import GameLogic.MatchFlowState;

public class CommandPause extends AbstractCommand {

    private int gameId;

    private CommandPause(String username, int gameId) {
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
        Match handledMatch = gameController.getMatches().get(getGameId());
        if(handledMatch.getMatchFlowState() == MatchFlowState.paused){
            handledMatch.resumeGame();
        }else if(handledMatch.getMatchFlowState() == MatchFlowState.started || handledMatch.getMatchFlowState() == MatchFlowState.running ){
            handledMatch.pauseGame();
        }
        //TODO : Check for right turn
        //User userToNotify = gameController.getUserToNotify(handledMatch, getUser());
        //gameController.getRestController().putMessage(new CommandOut(userToNotify.getUsername(), handledMatch.getGameId(), handledMatch.getMatchFlowState()));
    }
}
