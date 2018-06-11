package Controllers;

import GameLogic.Mode;
import GameLogic.Player;
import Utils.*;

import java.util.List;

public interface GameControllerInt extends Runnable{

    void newGame(CommandNewGame command);

    void move(CommandMove command);

    void pause(CommandPause command);

    void quit(CommandQuit command);

    List<AbstractCommand> getCommandsOut();

    void deleteCommandOut(AbstractCommand command);

    MatchMaking getMatchMaker();

    void createNewSinglePlayerGame(Mode mode, Player player);

}
