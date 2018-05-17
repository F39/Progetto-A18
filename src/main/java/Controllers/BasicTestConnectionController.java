package Controllers;

import Utils.JsonDecoder;
import Utils.JsonEncoder;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;


@ServerEndpoint(
        value = "/basic",
        decoders = JsonDecoder.class
)
public class BasicTestConnectionController {

    private static List<Session> peers;

    public BasicTestConnectionController() {
        peers = new ArrayList<>();
    }

    @OnOpen
    public void onOpen(Session session){
        peers.add(session);
        System.out.println("Open Connection session id " + session.getId());
        System.out.println("Number of active connection : " + peers.size());
    }

    @OnClose
    public void onClose(Session session){
        peers.remove(session);
        System.out.println("Close Connection session id " + session.getId());
        System.out.println("Number of active connection : " + peers.size());
    }

    @OnMessage
    public void onMessage(JSONObject message, Session session){
        try {
            if(message.has("event")){
                session.getBasicRemote().sendText("You sent a message of type " + message.get("event"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }

}
