import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ServerEndpoint(
        value = "/user",
        decoders = JsonDecoder.class
)
public class UserController extends WebSocketListener {

    private static List<Session> peers = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session){
        // custom handshake for auth, to be investigated asap
        System.out.println("Open Connection session id " + session.getId());
        peers.add(session);
        System.out.println(peers.size());
        // Log
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("Close Connection session id " + session.getId());
        peers.remove(session);
        System.out.println(peers.size());
    }

    @OnMessage
    public void onMessage(Session session, JSONObject message){
        String event;
        try {
            event = message.getString("event");
            handleEvent(event, message, session);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }

    public void handleEvent(String event, JSONObject message, Session session){
        try {
            session.getBasicRemote().sendText("You sent a message of type " + event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
