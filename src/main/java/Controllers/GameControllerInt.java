package Controllers;

import GameLogic.Mode;
import GameLogic.Player;
import Utils.*;

import java.util.List;

/**
 * User Controller facade
 */
public interface GameControllerInt extends Runnable {

    void newGame(CommandNewGame command);

    void move(CommandMove command);

    void pause(CommandPause command);

    void quit(CommandQuit command);

    List<AbstractCommand> getCommandsOut();

    void deleteCommandOut(AbstractCommand command);

    MatchMaking getMatchMaker();

    void createNewSinglePlayerGame(Mode mode, Player player);

    void createNewMultiPlayerGame(Player p1, Player p2, Mode mode);

    void createNewMultiPlayerGameWithFriend(Player p1, Player p2, Mode mode);

    void accept(CommandAcceptMatch command);

    void acceptGame(Player p1, Player p2, int gameId, int accepted);
}
