package Utils;

import Controllers.GameController;
import DatabaseManagement.User;
import GameLogic.MatchFlowState;

public class CommandOut extends AbstractCommand{

    private int gameId;
    private MatchFlowState matchFlowState;
    private int move;

    public CommandOut(String username, int gameId, MatchFlowState matchFlowState, int move) {
        super(username);
        this.gameId = gameId;
        this.matchFlowState = matchFlowState;
        this.move = move;
    }

    public int getGameId() {
        return gameId;
    }

    public MatchFlowState getMatchFlowState() {
        return matchFlowState;
    }

    public int getMove(){ return move; }

    @Override
    public void execute(GameController gameController) {

    }
}
