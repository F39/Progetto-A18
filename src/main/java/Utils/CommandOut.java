package Utils;

import DatabaseManagement.User;
import GameLogic.MatchFlowState;

public class CommandOut extends AbstractCommand {

    private int gameId;
    private MatchFlowState matchFlowState;

    public CommandOut(User user, int gameId, MatchFlowState matchFlowState) {
        super(user);
        this.gameId = gameId;
        this.matchFlowState = matchFlowState;
    }

    public int getGameId() {
        return gameId;
    }

    public MatchFlowState getMatchFlowState() {
        return matchFlowState;
    }

}
