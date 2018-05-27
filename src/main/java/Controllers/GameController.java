package Controllers;

import DatabaseManagement.User;
import GameLogic.Match;
import GameLogic.MatchFlowState;
import GameLogic.Mode;
import Utils.Command;
import Utils.ObserverGame;
import Utils.ObserverConnection;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class GameController extends ObserverGame {

    private List<User> pendingUsers;
    private HashMap<Integer, Match> matches;
    private List<ObserverConnection> observers = new ArrayList<>();

    GameController() {
        matches = new HashMap<>();
        pendingUsers = new ArrayList<>();
    }

    public void attach(ObserverConnection observer) {
        observers.add(observer);
    }

    private void notifyAllObservers(Command command) {
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
                if (mode == Mode.MultiPlayer.getValue()) {
                    pendingUsers.add(user);
                    handledMatch = this.matchMaking();
                } else if (mode == Mode.SinglePlayerLevel1.getValue()) {
                    handledMatch = this.createNewGame(Mode.SinglePlayerLevel1, user, null);
                } else {
                    handledMatch = this.createNewGame(Mode.SinglePlayerLevel2, user, null);
                }
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

    private Match matchMaking() {
        if (pendingUsers.size() > 2) {
            while (true) {
                int indexP1, indexP2;
                // TODO : define a better match-making algorithm
                indexP1 = ThreadLocalRandom.current().nextInt(0, pendingUsers.size() + 1);
                indexP2 = ThreadLocalRandom.current().nextInt(0, pendingUsers.size() + 1);
                if (indexP1 != indexP2) {
                    return this.createNewGame(Mode.MultiPlayer, pendingUsers.get(indexP1), pendingUsers.get(indexP2));
                }
            }
        }
        return null;
    }

    private Match createNewGame(Mode mode, User p1, User p2) {
        Match newMatch = new Match(mode, p1, p2);
        newMatch.attach(this);
        matches.put(newMatch.getGameId(), newMatch);
        pendingUsers.remove(p1);
        pendingUsers.remove(p2);
        return newMatch;
    }

}
