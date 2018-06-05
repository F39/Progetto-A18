package Controllers;

import DatabaseManagement.User;
import GameLogic.Match;
import GameLogic.MatchFlowState;
import GameLogic.Mode;
import Utils.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GameController extends ObserverGame implements GameControllerInt {

    private HashMap<Integer, Match> matches;
    private List<ObserverConnection> observers = new ArrayList<>();
    private MatchMaking matchMaker;
    private RestControllerInt restController;

    public GameController() {
        matches = new HashMap<>();
        restController = new RestGameController();
        matchMaker = new MatchMaking(this);
        Thread matchMaking = new Thread(matchMaker);
        matchMaking.start();
    }

    public void attach(ObserverConnection observer) {
        observers.add(observer);
    }

    private void notifyAllObservers(AbstractCommand command) {
        for (ObserverConnection observer : observers) {
            observer.update(command);
        }
    }

    @Override
    public void update(int gameId) {
        Match updatedMatch = matches.get(gameId);
        MatchFlowState matchState = updatedMatch.getMatchFlowState();
        List<User> usersToBeNotified = matches.get(gameId).getPlayers();
        Command newCommand;
        switch (matchState) {
            case started:
                newCommand = new Command(matchState, usersToBeNotified, gameId);
                notifyAllObservers(newCommand);
                break;
            case running:
                int newMove = updatedMatch.getLastMove();
                newCommand = new Command(matchState, usersToBeNotified, gameId, newMove);
                notifyAllObservers(newCommand);
                break;
            case paused:
                newCommand = new Command(matchState, usersToBeNotified, gameId);
                notifyAllObservers(newCommand);
                break;
            case quitted:
                newCommand = new Command(matchState, usersToBeNotified, gameId);
                notifyAllObservers(newCommand);
                break;
            case finished:
                newCommand = new Command(matchState, usersToBeNotified, gameId);
                notifyAllObservers(newCommand);
                break;
            default:
                break;
        }
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

    public void createNewGame(Mode mode, User p1, User p2) {
        Match newMatch = new Match(mode, p1, p2);
        newMatch.attach(this);
        matches.put(newMatch.getGameId(), newMatch);
        newMatch.startGame();
        //TODO : Set logic for gameId
        //TODO : check for AI player
        restController.putMessage(new CommandOut(p1,newMatch.getGameId(), newMatch.getMatchFlowState()));
        restController.putMessage(new CommandOut(p2,newMatch.getGameId(), newMatch.getMatchFlowState()));
    }

    @Override
    public void newGame(CommandNewGame command) {
        int mode = command.getMode().getValue();
        if (mode == Mode.MultiPlayer.getValue()) {
            matchMaker.putPendingUsers(command.getUser());
        } else if (mode == Mode.SinglePlayerLevel1.getValue()) {
            this.createNewGame(Mode.SinglePlayerLevel1, command.getUser(), null);
        } else {
            this.createNewGame(Mode.SinglePlayerLevel2, command.getUser(), null);
        }
    }

    @Override
    public void move(CommandMove command) {
        Match handledMatch = matches.get(command.getGameId());
        handledMatch.setLastMove(command.getMove());
        //TODO : Check for right turn
        User userToNotify = getUserToNotify(handledMatch, command.getUser());
        restController.putMessage(new CommandOut(userToNotify, handledMatch.getGameId(), handledMatch.getMatchFlowState()));
    }

    @Override
    public void pause(CommandPause command) {
        Match handledMatch = matches.get(command.getGameId());
        handledMatch.setMatchFlowState(MatchFlowState.paused);
        //TODO : Check for right turn
        User userToNotify = getUserToNotify(handledMatch, command.getUser());
        restController.putMessage(new CommandOut(userToNotify, handledMatch.getGameId(), handledMatch.getMatchFlowState()));
    }

    @Override
    public void quit(CommandQuit command) {
        // TODO : tutto
    }

    private User getUserToNotify (Match match, User user){
        if(match.getPlayers().get(0) == user){
            return match.getPlayers().get(1);
        }
        return match.getPlayers().get(0);
    }
}
