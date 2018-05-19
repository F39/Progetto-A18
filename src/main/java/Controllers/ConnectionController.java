package Controllers;

import DatabaseManagement.UserRepository;
import Utils.JsonDecoder;
import Utils.JsonEncoder;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO : resolve ambiguity in encoder/decoder classes names
@ServerEndpoint(
        value = "/connect4",
        decoders = JsonDecoder.class,
        encoders = JsonEncoder.class
)
public class ConnectionController {

    private static List<Session> peers = new ArrayList<>();
    private HashMap<Session, String> authenticatedUserSession = new HashMap<>();
    private GameController gameController = new GameController();
    private UserRepository userRepository = new UserRepository();

    @OnOpen
    public void onOpen(Session session) {
        // TODO : custom handshake for auth, to be investigated ASAP
        System.out.println("Open Connection session id " + session.getId());
        peers.add(session);
        // TODO : Log
        System.out.println(peers.size());
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close Connection session id " + session.getId());
        peers.remove(session);
        String tokenToBeInvalidated = authenticatedUserSession.get(session);
        authenticatedUserSession.remove(session);
        // TODO : invalidate user token on db
        // TODO : force to game controller a match quit event
        // TODO : Log
        System.out.println(peers.size());
    }

    @OnMessage
    public void onMessage(JSONObject message, Session session) {
        try {
            // TODO : check auth token
            if (checkUserAuthToken(message.getString("token"), session)) {
                this.gameController.handleEvent(message, session);
            } else {
                // TODO : invalidate session
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkUserAuthToken(String token, Session session) {
        if (authenticatedUserSession.containsKey(session)) {
            return true;
        }
        // else check user token on db
        // TODO : maybe we can use an Auth table and a relative repo ?
        else if (userRepository.getUserByAuthToken(token) != null) {
            authenticatedUserSession.put(session, token);
            return true;
        } else {
            return false;
        }
    }

    @OnError
    public void onError(Throwable e) {
        // TODO : Log
        e.printStackTrace();
    }

}
