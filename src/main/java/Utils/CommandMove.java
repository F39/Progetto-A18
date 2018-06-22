package Utils;

public class CommandMove extends CommandMatch {

    private int move;

    public CommandMove(){

    }

    public CommandMove(String username, int gameId, int move) {
        super(username);
        this.gameId = gameId;
        this.move = move;
    }

    public CommandMove(CommandMove message) {
        super(message.getUsername());
        this.gameId = message.getGameId();
        this.move = message.getMove();
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    @Override
    public void execute() {
        match.move(move);
    }

}
