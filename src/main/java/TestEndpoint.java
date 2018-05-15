import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/user/{id}")
public class TestEndpoint {

    @OnOpen
    public void onOpen(@PathParam("id") String id){
        System.out.println("Open Connection ... id " + id);
    }

    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public void onMessage(@PathParam("id") String id, Session session, String message){
        System.out.println(message + id);
        try {
            session.getBasicRemote().sendText("You sent " + message);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }

}
