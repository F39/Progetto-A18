package Utils;

import GameLogic.Match;

public class CommandQuit extends CommandMatch {

    public CommandQuit(String username, int gameId) {
        super(username);
        this.gameId = gameId;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public void execute() {
        match.quitGame(player);
    }
}
