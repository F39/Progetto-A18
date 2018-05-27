package Controllers;

import DatabaseManagement.User;
import DatabaseManagement.UserRepository;
import Utils.Command;
import Utils.JsonDecoder;
import Utils.JsonEncoder;
import Utils.ObserverConnection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


// TODO : resolve ambiguity in encoder/decoder classes names
@ServerEndpoint(
        value = "/connect4",
        decoders = JsonDecoder.class,
        encoders = JsonEncoder.class
)
public class ConnectionController extends ObserverConnection {

    private static HashMap<Session, User> peers = new HashMap<>(); // map sessions to relative users
    private HashMap<Session, String> authenticatedUserSession = new HashMap<>(); // map users to relative auth tokens
    private GameController gameController;

    private UserRepository userRepository;

    public ConnectionController() {

        gameController = new GameController();
        gameController.attach(this);

        // TODO : export to config
        String databaseUrl = "jdbc:mysql://localhost:3306/forza4";
        String dbUser = "root";
        String dbPass = "";
        ConnectionSource connectionSource;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl, dbUser, dbPass);
            userRepository = new UserRepository(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @OnOpen
    public void onOpen(Session session) {
        // TODO : Log
        peers.put(session, null);
        // TODO : Log
    }

    @OnClose
    public void onClose(Session session) {
        // TODO : Log
        String tokenToBeInvalidated = authenticatedUserSession.get(session);
        authenticatedUserSession.remove(session);
        try {
            userRepository.updateUserAuthToken(null, peers.get(session).getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        peers.remove(session);
        // TODO : force to game controller a match quit event
        // TODO : Log
    }

    @OnMessage
    public void onMessage(JSONObject message, Session session) {
        try {
            if (checkUserAuthToken(message.getString("token"), session)) {
                // TODO : serialize message to command obj here
                this.gameController.handleEvent(message, peers.get(session));
            } else {
                // TODO : invalidate session and eventually notify game controller
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable e) {
        // TODO : Log
        e.printStackTrace();
    }

    private boolean checkUserAuthToken(String token, Session session) {
        User newAuthUser;
        if (authenticatedUserSession.containsKey(session)) {
            return true;
        } else {
            try {
                if ((newAuthUser = userRepository.getUserByAuthToken(token)) != null) {
                    authenticatedUserSession.put(session, token);
                    peers.put(session, newAuthUser);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private JSONObject serializeCommand(Command command) {
        // TODO : to be tested
        return new JSONObject(command);
    }

    private static Object getKeyFromValue(HashMap hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    @Override
    public void update(Command command) {
        List<User> usersToBeNotified = command.getUsers();
        JSONObject message = serializeCommand(command);
        Session session;
        for (User user : usersToBeNotified) {
            session = (Session) getKeyFromValue(peers, user);
            try {
                if (session != null) {
                    session.getBasicRemote().sendText(message.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
