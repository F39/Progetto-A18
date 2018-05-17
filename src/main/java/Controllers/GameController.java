package Controllers;

import GameLogic.Match;
import GameLogic.Mode;
import People.User;
import Utils.Observer;
import org.json.JSONObject;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController extends Observer {

    private List<User> pendingUsers;
    private List<Match> matches;

    GameController() {
        matches = new ArrayList<>();
        pendingUsers = new ArrayList<>();
        //matchesSessionMap = new HashMap<>();
    }

    @Override
    public void update() {

    }

    public void handleEvent(JSONObject message, Session session) {
        String event = message.getString("event");
        try {
            session.getBasicRemote().sendText("You sent a message of type " + event);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(event.equals("newGame")){
            // TODO : get that fuuuuu user
            //pendingUsers.add();
        }
    }

    private void matchMaking(){
        // TODO : define match making algorithm
    }

    private void createNewGame(User p1, User p2){
        Match newMatch = new Match(7, 6, Mode.MultiPlayer, p1, p2);
        newMatch.attach(this);
        matches.add(newMatch);
        pendingUsers.remove(p1);
        pendingUsers.remove(p2);
    }

}
