package Controllers;

import DatabaseManagement.User;
import DatabaseManagement.UserRepository;
import DatabaseManagement.UserRepositoryInt;
import Utils.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Path("/game")
public class RestGameController implements RestControllerInt{

    private GameControllerInt gameControllerInt;
    private List<AbstractCommand> commandQueue;


    public RestGameController(){

        gameControllerInt = new GameController(this);
        Thread gameControllerThread = new Thread(gameControllerInt);
        gameControllerThread.start();

        commandQueue = new ArrayList<>();
    }

    @POST
    @Path("/newgame")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newGame (@HeaderParam("token") String token, CommandNewGame command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.newGame(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/move")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response move (@HeaderParam("token") String token, CommandMove command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.move(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/pause")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pause (@HeaderParam("token") String token, CommandPause command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.pause(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/quit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response quit (@HeaderParam("token") String token, CommandQuit command){
        if(checkAuthToken(token, command.getUsername())){
            gameControllerInt.quit(command);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/poll")
    public Response poll(@HeaderParam("token") String token, String username){
        if(checkAuthToken(token, username)){
            for (AbstractCommand command : commandQueue) {
                if(username.equals(command.getUsername())){
                    return Response.ok(command).build();
                }
            }
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }


    private boolean checkAuthToken(String token, String username) {
        User checkUser = UserController.getOnline().get(token);
        if(checkUser == null){
            return false;
        }
        if(username.equals(checkUser.getUsername())){
            return true;
        }
        return false;
    }

    @Override
    public void putMessage(AbstractCommand command) {
        commandQueue.add(command);
    }

}
