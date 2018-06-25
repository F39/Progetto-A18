package Controllers;

import DatabaseManagement.UserStatsRepositoryInt;
import DatabaseManagement.UserStatsSqlRepository;
import GameLogic.Match;
import GameLogic.MatchFlowState;
import GameLogic.Mode;
import GameLogic.Player;
import Logger.Logger;
import Utils.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Singleton
public class GameController implements GameControllerInt {

    private HashMap<Integer, Match> matches;
    private MatchMaking matchMaker;
    private List<AbstractCommand> commandsIn = new ArrayList<>();
    private List<AbstractCommand> commandsOut = new ArrayList<>();
    private int progressiveGameId;
    private UserStatsRepositoryInt userStatsRepository;
    private Logger logger;

    public GameController() {
        logger = Logger.getInstance();
        matches = new HashMap<>();
        progressiveGameId = 1;

        matchMaker = new MatchMaking(this);
        Thread matchMaking = new Thread(matchMaker);
        matchMaking.start();

        logger.log("Starting game controller thread");
        Thread gameControllerThread = new Thread(this);
        gameControllerThread.start();
        logger.log("Game controller thread successfully started");

        Properties dbConnectionProps;
        ConnectionSource connectionSource;
        try {
            dbConnectionProps = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/Config/db_config.properties");
            dbConnectionProps.load(in);
            in.close();
            String databaseConnectionString = dbConnectionProps.getProperty("databaseURL") + dbConnectionProps.getProperty("databaseHost") + dbConnectionProps.getProperty("databaseName");
            connectionSource = new JdbcConnectionSource(databaseConnectionString, dbConnectionProps.getProperty("databaseUser"), dbConnectionProps.getProperty("databasePassword"));
            userStatsRepository = new UserStatsSqlRepository(connectionSource);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    public void createNewSinglePlayerGame(Mode mode, Player p1) {
        logger.log("Create new single player game");
        Match newMatch = new Match(p1, mode, progressiveGameId++);
        matches.put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        CommandOut commandOut = new CommandOut(p1.getUsername(), newMatch.getGameId(), MatchFlowState.started1, -2);
        commandsOut.add(commandOut);
        userStatsRepository.addUserGame(p1.getUser());
    }

    public void createNewMultiPlayerGame(Player p1, Player p2, Mode mode) {
        logger.log("Create new multi player game");
        Match newMatch = new Match(p1, p2, progressiveGameId++, mode);
        matches.put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        commandsOut.add(new CommandOut(p1.getUsername(), newMatch.getGameId(), MatchFlowState.started1, -2));
        userStatsRepository.addUserGame(p1.getUser());
        commandsOut.add(new CommandOut(p2.getUsername(), newMatch.getGameId(), MatchFlowState.started2, -2));
        userStatsRepository.addUserGame(p2.getUser());
    }

    public void createNewMultiPlayerGameWithFriend(Player p1, Player p2, Mode mode) {
        logger.log("Create new multi player game with friend");
        Match newMatch = new Match(p2, p1, progressiveGameId++, mode);
        matches.put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        //commandsOut.add(new CommandOut(p1.getUsername(), newMatch.getGameId(), MatchFlowState.invite1, -2, p2.getUsername()));
        //userStatsRepository.addUserGame(p1.getUser());
        commandsOut.add(new CommandOut(p2.getUsername(), newMatch.getGameId(), mode, -2, p1.getUsername()));
        //userStatsRepository.addUserGame(p2.getUser());
    }

    public void acceptGame(Player p1, Player p2, int gameId, int accepted) {
        if(accepted != 0){
            commandsOut.add(new CommandOut(p1.getUsername(), gameId, MatchFlowState.started1, -2));
            userStatsRepository.addUserGame(p1.getUser());
            commandsOut.add(new CommandOut(p2.getUsername(), gameId, MatchFlowState.started2, -2));
            userStatsRepository.addUserGame(p2.getUser());
        }else{
            commandsOut.add(new CommandOut(p2.getUsername(), gameId, MatchFlowState.refused, -2));
            matches.remove(gameId);
        }
    }

    @Override
    public void accept(CommandAcceptMatch command) {
        logger.log("Received accept match command");
        commandsIn.add(command);
    }


    @Override
    public void newGame(CommandNewGame command) {
        logger.log("Received new game command");
        commandsIn.add(command);
    }

    @Override
    public void move(CommandMove command) {
        logger.log("Received new move command");
        commandsIn.add(command);
    }

    @Override
    public void pause(CommandPause command) {
        logger.log("Received new pause command");
        commandsIn.add(command);
    }

    @Override
    public void quit(CommandQuit command) {
        logger.log("Received new quit command");
            commandsIn.add(command);
    }

    @Override
    public synchronized void deleteCommandOut(AbstractCommand command) {
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
                    logger.log("New game command successfully executed");
                } else if (toExecute instanceof  CommandAcceptMatch) {
                    ((CommandAcceptMatch) toExecute).setGameController(this);
                    toExecute.execute();
                    logger.log("New game command successfully executed");
                } else {
                    handledMatch = matches.get(((CommandMatch) toExecute).getGameId());
                    ((CommandMatch) toExecute).setMatch(handledMatch);
                    toExecute.execute();
                    logger.log("Match command successfully executed");
                    logger.log("Sending notification to clients");
                    if (handledMatch.getAiStrategyInt() == null) {
                        sendNotification(handledMatch);
                    } else {
                        sendNotificationSinglePlayer(handledMatch);
                    }
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

    private void sendNotificationSinglePlayer(Match match) {
        int lastMove = match.getLastMove();
        if (match.getMatchFlowState().equals(MatchFlowState.paused)) {
            logger.log(String.format("Waiting for game resume, match id %s", match.getGameId()));
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.paused, -1));
            return;
        } else if (match.getMatchFlowState().equals(MatchFlowState.resumed)) {
            logger.log(String.format("Match %s resumed.", match.getGameId()));
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.resumed, -1));
            return;
        }
        if (match.getMatchFlowState() != MatchFlowState.winner1) {
            commandsOut.add(new CommandOut(match.getPlayers().get(match.getTurn()).getUsername(), match.getGameId(), match.getMatchFlowState(), lastMove));
        }
        if (match.getMatchFlowState().equals(MatchFlowState.winner1)) {
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.winner, -1));
            userStatsRepository.addUserWin(match.getPlayers().get(1).getUser());
            matches.remove(match.getGameId());
            System.out.println(matches.size());
        } else if (match.getMatchFlowState().equals(MatchFlowState.winner2)) {
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.looser, -1));
            userStatsRepository.addUserDefeat(match.getPlayers().get(1).getUser());
            matches.remove(match.getGameId());
        } else if (match.getMatchFlowState().equals(MatchFlowState.tie)) {
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), match.getMatchFlowState(), -1));
            userStatsRepository.addUserTie(match.getPlayers().get(1).getUser());
            matches.remove(match.getGameId());
        }
    }

    private void sendNotification(Match match) {
        int lastMove = match.getLastMove();
        if (match.getMatchFlowState().equals(MatchFlowState.paused)) {
            logger.log(String.format("Match %s paused.", match.getGameId()));
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.paused, -1));
            commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.paused, -1));
            return;
        } else if (match.getMatchFlowState().equals(MatchFlowState.resumed)) {
            logger.log(String.format("Match %s resumed.", match.getGameId()));
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.resumed, -1));
            commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.resumed, -1));
            return;
        }
        commandsOut.add(new CommandOut(match.getPlayers().get(match.getTurn()).getUsername(), match.getGameId(), match.getMatchFlowState(), lastMove));
        if (match.getMatchFlowState().equals(MatchFlowState.winner1)) {
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.winner, -1));
            commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.looser, -1));
            userStatsRepository.addUserWin(match.getPlayers().get(1).getUser());
            userStatsRepository.addUserDefeat(match.getPlayers().get(2).getUser());
            matches.remove(match.getGameId());
        } else if (match.getMatchFlowState().equals(MatchFlowState.winner2)) {
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), MatchFlowState.looser, -1));
            commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), MatchFlowState.winner, -1));
            userStatsRepository.addUserDefeat(match.getPlayers().get(1).getUser());
            userStatsRepository.addUserWin(match.getPlayers().get(2).getUser());
            matches.remove(match.getGameId());
        } else if (match.getMatchFlowState().equals(MatchFlowState.tie)) {
            commandsOut.add(new CommandOut(match.getPlayers().get(1).getUsername(), match.getGameId(), match.getMatchFlowState(), -1));
            commandsOut.add(new CommandOut(match.getPlayers().get(2).getUsername(), match.getGameId(), match.getMatchFlowState(), -1));
            userStatsRepository.addUserTie(match.getPlayers().get(1).getUser());
            userStatsRepository.addUserTie(match.getPlayers().get(2).getUser());
            matches.remove(match.getGameId());
        }
    }

    public MatchMaking getMatchMaker() {
        return matchMaker;
    }

    public synchronized List<AbstractCommand> getCommandsOut() {
        return commandsOut;
    }

}
