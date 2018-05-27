package Utils;

import DatabaseManagement.User;
import GameLogic.MatchFlowState;

import java.util.List;

public class Command {

    private MatchFlowState state;
    private List<User> users;
    private int gameId;
    private int move;

    public Command(MatchFlowState state, List<User> users, int gameId) {
        this.move = -1;
        this.state = state;
        this.users = users;
        this.gameId = gameId;
    }

    public Command(MatchFlowState state, List<User> users, int gameId, int move) {
        this(state, users, gameId);
        this.move = move;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public MatchFlowState getState() {
        return state;
    }

    public void setState(MatchFlowState state) {
        this.state = state;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
