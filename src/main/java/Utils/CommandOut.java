package Utils;

import Controllers.GameController;
import DatabaseManagement.User;
import GameLogic.MatchFlowState;

public class CommandOut extends AbstractCommand{

    private int gameId;
    private MatchFlowState matchFlowState;
    private boolean read;

    public CommandOut(String username, int gameId, MatchFlowState matchFlowState) {
        super(username);
        this.gameId = gameId;
        this.matchFlowState = matchFlowState;
    }

    public int getGameId() {
        return gameId;
    }

    public MatchFlowState getMatchFlowState() {
        return matchFlowState;
    }

    @Override
    public void execute(GameController gameController) {
        read = true;
    }
}
