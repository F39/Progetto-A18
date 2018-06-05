package Controllers;

import Utils.CommandMove;
import Utils.CommandNewGame;
import Utils.CommandPause;
import Utils.CommandQuit;

public interface GameControllerInt {

    void newGame(CommandNewGame command);

    void move(CommandMove command);

    void pause(CommandPause command);

    void quit(CommandQuit command);

}
