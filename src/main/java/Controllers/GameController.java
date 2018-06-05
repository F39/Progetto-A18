package Controllers;

import DatabaseManagement.User;
import GameLogic.Match;
import GameLogic.MatchFlowState;
import GameLogic.Mode;
import GameLogic.Player;
import Utils.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameController extends ObserverGame implements GameControllerInt {

    private HashMap<Integer, Match> matches;
    private List<ObserverConnection> observers = new ArrayList<>();
    private MatchMaking matchMaker;
    private RestControllerInt restController;
    private List<AbstractCommand> commands = new ArrayList<>();
    private int progressiveGameId;

    public GameController(RestGameController restController) {
        matches = new HashMap<>();
        this.restController = restController;
        matchMaker = new MatchMaking(this);
        Thread matchMaking = new Thread(matchMaker);
        matchMaking.start();
        progressiveGameId = 0;
    }

    public void attach(ObserverConnection observer) {
        observers.add(observer);
    }

    private void notifyAllObservers(AbstractCommand command) {
        for (ObserverConnection observer : observers) {
            observer.update(command);
        }
    }

//    @Override
//    public void update(int gameId) {
//        Match updatedMatch = matches.get(gameId);
//        MatchFlowState matchState = updatedMatch.getMatchFlowState();
//        Map<Integer, Player> usersToBeNotified = updatedMatch.getPlayers();
//        CommandOut notify = new CommandOut(usersToBeNotified.get(updatedMatch.getTurn()).getUser().getUsername(), gameId, matchState, -1);
//        restController.putMessage(notify);
//
//        switch (matchState) {
//            case started:
//                newCommand = new Command(matchState, usersToBeNotified, gameId);
//                notifyAllObservers(newCommand);
//                break;
//            case running:
//                int newMove = updatedMatch.getLastMove();
//                newCommand = new Command(matchState, usersToBeNotified, gameId, newMove);
//                notifyAllObservers(newCommand);
//                break;
//            case paused:
//                newCommand = new Command(matchState, usersToBeNotified, gameId);
//                notifyAllObservers(newCommand);
//                break;
//            case quitted:
//                newCommand = new Command(matchState, usersToBeNotified, gameId);
//                notifyAllObservers(newCommand);
//                break;
//            case finished:
//                newCommand = new Command(matchState, usersToBeNotified, gameId);
//                notifyAllObservers(newCommand);
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public void update(int gameId, int move) {
        Match updatedMatch = matches.get(gameId);
        MatchFlowState matchState = updatedMatch.getMatchFlowState();
        Map<Integer, Player> usersToBeNotified = updatedMatch.getPlayers();
        CommandOut notify = new CommandOut(usersToBeNotified.get(updatedMatch.getTurn()).getUser().getUsername(), gameId, matchState, move);
        restController.putMessage(notify);
    }

    // TODO : handle command obj and not json
    void handleEvent(JSONObject message, User user) {
        int gameId;
        String event = message.getString("event");
        JSONObject data = message.getJSONObject("data");
        // TODO : here create command obj
        Match handledMatch = null;
        MatchFlowState newMatchState;
        if (data.has("gameId")) {
            gameId = data.getInt("gameId");
            handledMatch = this.matches.get(gameId);
        }
        // TODO : switch case to handle events types, REALLY there's no another way ??? !!!
        switch (event) {
            case "newGame":
                int mode = data.getInt("mode");
//                if (mode == Mode.MultiPlayer.getValue()) {
//                    pendingUsers.add(user);
//                    handledMatch = this.matchMaking();
//                } else if (mode == Mode.SinglePlayerLevel1.getValue()) {
//                    handledMatch = this.createNewGame(Mode.SinglePlayerLevel1, user, null);
//                } else {
//                    handledMatch = this.createNewGame(Mode.SinglePlayerLevel2, user, null);
//                }
                // TODO : catch the shitty exceptions
                if (handledMatch != null) {
                    handledMatch.startGame();
                }
                break;
            case "move":
                int newMove = data.getInt("column");
                handledMatch.setLastMove(newMove);
                break;
            case "pause":
                handledMatch.setMatchFlowState(MatchFlowState.paused);
                break;
            case "logout":
                handledMatch.setMatchFlowState(MatchFlowState.paused);
                break;
            case "quit":
                handledMatch.setMatchFlowState(MatchFlowState.paused);
                break;
            default:
                break;
        }
    }

    public void createNewMultiPlayerGame(User p1, User p2){
        Match newMatch;
        newMatch = new Match(Mode.MultiPlayer, new Player(p1), new Player(p2), progressiveGameId++);
        newMatch.attach(this);
        this.getMatches().put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        //TODO : Set logic for gameId
        //TODO : check for AI player
        this.getRestController().putMessage(new CommandOut(p1.getUsername(),newMatch.getGameId(), newMatch.getMatchFlowState(), -1));
        this.getRestController().putMessage(new CommandOut(p2.getUsername(),newMatch.getGameId(), newMatch.getMatchFlowState(), -1));
    }

    @Override
    public void newGame(CommandNewGame command) {
        commands.add(command);
    }

    @Override
    public void move(CommandMove command) {
        commands.add(command);
    }

    @Override
    public void pause(CommandPause command) {
        commands.add(command);
    }

    @Override
    public void quit(CommandQuit command) {
        commands.add(command);
    }

    public User getUserToNotify (Match match, User user){
        if(match.getPlayers().get(0).getUser() == user){
            return match.getPlayers().get(1).getUser();
        }
        return match.getPlayers().get(0).getUser();
    }

    @Override
    public void run() {
        while(true){
            if(commands.size() > 0){
                AbstractCommand toExecute = commands.get(0);
                toExecute.execute(this);
                commands.remove(0);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public MatchMaking getMatchMaker() {
        return matchMaker;
    }

    public HashMap<Integer, Match> getMatches() {
        return matches;
    }

    public RestControllerInt getRestController() {
        return restController;
    }

    public int getProgressiveGameId() {
        return progressiveGameId++;
    }
}
