package Utils;

import GameLogic.Match;

public class CommandQuit extends CommandMatch {

    public CommandQuit(){

    }

    public CommandQuit(String username, int gameId) {
        super(username);
        this.gameId = gameId;
    }

    public CommandQuit(CommandQuit message) {
        super(message.getUsername());
        this.gameId = message.getGameId();
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public void execute() {
        match.quitGame(player);
    }
}
