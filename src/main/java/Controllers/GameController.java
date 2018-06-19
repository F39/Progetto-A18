package Controllers;

import GameLogic.Match;
import GameLogic.MatchFlowState;
import GameLogic.Mode;
import GameLogic.Player;
import Utils.*;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class GameController implements GameControllerInt {

    private HashMap<Integer, Match> matches;
    private MatchMaking matchMaker;
    private List<AbstractCommand> commandsIn = new ArrayList<>();
    private List<AbstractCommand> commandsOut = new ArrayList<>();
    private int progressiveGameId;

    public GameController() {
        matches = new HashMap<>();
        progressiveGameId = 1;

        matchMaker = new MatchMaking(this);
        Thread matchMaking = new Thread(matchMaker);
        matchMaking.start();

        Thread gameControllerThread = new Thread(this);
        gameControllerThread.start();
    }

    public void createNewSinglePlayerGame(Mode mode, Player p1) {
        Match newMatch = new Match(p1, mode, progressiveGameId++);
        matches.put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        //TODO : Set logic for gameId
        //TODO : check for AI player
        commandsOut.add(new CommandOut(p1.getUsername(), newMatch.getGameId(), newMatch.getMatchFlowState(), -2));
//        commandsOut.add(new CommandOut(p2.getUsername(), newMatch.getGameId(), newMatch.getMatchFlowState(), -1));
    }


    public void createNewMultiPlayerGame(Player p1, Player p2) {
        Match newMatch = new Match(p1, p2, progressiveGameId++);
        matches.put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        //TODO : Set logic for gameId
        //TODO : check for AI player
        commandsOut.add(new CommandOut(p1.getUsername(), newMatch.getGameId(), MatchFlowState.started1, -2));
        System.out.println("Primo messaggio messo");
        commandsOut.add(new CommandOut(p2.getUsername(), newMatch.getGameId(), MatchFlowState.started2, -2));
        System.out.println("Secondo messaggio messo");
    }

    @Override
    public void newGame(CommandNewGame command) {
        commandsIn.add(command);
    }

    @Override
    public void move(CommandMove command) {
        commandsIn.add(command);
    }

    @Override
    public void pause(CommandPause command) {
        commandsIn.add(command);
    }

    @Override
    public void quit(CommandQuit command) {
        commandsIn.add(command);
    }

    @Override
    public void deleteCommandOut(AbstractCommand command) {
        commandsOut.remove(command);
    }

    @Override
    public void run() {
        while (true) {
            if (commandsIn.size() > 0) {
                Match handledMatch;
                AbstractCommand toExecute = commandsIn.get(0);
                if (toExecute instanceof CommandNewGame) {
                    ((CommandNewGame) toExecute).setGameController(this);
                    toExecute.execute();
                } else {
                    handledMatch = matches.get(((CommandMatch) toExecute).getGameId());
                    ((CommandMatch) toExecute).setMatch(handledMatch);
                    toExecute.execute();
                    sendNotification(handledMatch);
                }
                commandsIn.remove(0);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(Match match) {
        int lastMove = match.getLastMove();
        if (match.getMatchFlowState().equals(MatchFlowState.paused)) {
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.paused, -1));
            commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.paused, -1));
            return;
        }else if(match.getMatchFlowState().equals(MatchFlowState.resumed)){
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.resumed, -1));
            commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.resumed, -1));
            return;
        }
        commandsOut.add(new CommandOut(match.getPlayers().get(match.getTurn()).getUsername(), match.getGameId(), match.getMatchFlowState(), lastMove));
        //if (lastMove == -1) {
        if (match.getMatchFlowState().equals(MatchFlowState.winner1)) {
                commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.winner, -1));
                commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.looser, -1));
        } else if (match.getMatchFlowState().equals(MatchFlowState.winner2)) {
                commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.looser, -1));
                commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.winner, -1));
        } else if (match.getMatchFlowState().equals(MatchFlowState.tie)){
                commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), match.getMatchFlowState(), -1));
                commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), match.getMatchFlowState(), -1));
            //}
        //} else {
        }
}

    public MatchMaking getMatchMaker() {
        return matchMaker;
    }

    public List<AbstractCommand> getCommandsOut() {
        return commandsOut;
    }

}
