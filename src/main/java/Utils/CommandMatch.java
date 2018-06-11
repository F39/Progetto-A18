package Utils;

import GameLogic.Match;

public abstract class CommandMatch extends AbstractCommand {

    Match match;
    int gameId;

    public CommandMatch(){

    }

    public CommandMatch(String username){
        super(username);
    }

    public void setMatch(Match match){
        this.match = match;
    }

    public int getGameId(){
        return gameId;
    }
}
