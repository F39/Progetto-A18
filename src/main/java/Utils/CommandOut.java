package Utils;

import GameLogic.MatchFlowState;
import GameLogic.Mode;
import GameLogic.Player;

public class CommandOut extends AbstractCommand {

    private int gameId;
    private MatchFlowState matchFlowState;
    private int move;
    private String opponentUsername;
    private Mode mode;

    public CommandOut(String username, int gameId, MatchFlowState matchFlowState, int move) {
        this(username, gameId, move);
        this.matchFlowState = matchFlowState;
    }

    public CommandOut(String username, int gameId, MatchFlowState matchFlowState, int move, String opponentUsername) {
        this(username, gameId, matchFlowState, move);
        this.opponentUsername = opponentUsername;
    }

    public CommandOut(String username, int gameId, Mode mode, int move, String opponentUsername) {
        this(username, gameId, move);
        this.mode = mode;
        this.opponentUsername = opponentUsername;
    }

    public CommandOut(String username, int gameId, int move) {
        super(username);
        this.gameId = gameId;
        this.move = move;
    }

    public int getGameId() {
        return gameId;
    }

    public MatchFlowState getMatchFlowState() {
        return matchFlowState;
    }

    public int getMove() {
        return move;
    }

    @Override
    public void execute() {

    }
}
