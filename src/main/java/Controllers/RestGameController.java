package Controllers;

import DatabaseManagement.User;
import DatabaseManagement.UserRepository;
import DatabaseManagement.UserRepositoryInt;
import Utils.AbstractCommand;
import Utils.CommandNewGame;
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

    private UserRepositoryInt userRepository;
    @Inject
    private GameControllerInt gameControllerInt;
    private List<AbstractCommand> commandQueue;


    public RestGameController(){
        ConnectionSource connectionSource;
        String databaseUrl = "jdbc:mysql://localhost:3306/forza4";
        String dbUser = "root";
        String dbPass = "arcanine9";
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl, dbUser, dbPass);
            userRepository = new UserRepository(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //gameControllerInt = new GameController();
        commandQueue = new ArrayList<>();
    }

    @POST
    @Path("/newgame")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newGame (@HeaderParam("token") String token, CommandNewGame command){
        if(token.equals(UserController.getOnline().get(token)))
        return Response.ok().build();

    }

    @POST
    @Path("/move")
    @Produces(MediaType.APPLICATION_JSON)
    public Response move (){
        return Response.ok().build();
    }

    @POST
    @Path("/pause")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pause (){
        return Response.ok().build();
    }

    @POST
    @Path("/quit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response quit (){
        return Response.ok().build();
    }


    private boolean checkAuthToken(String token, User user) {
        if()
    }

    @Override
    public void putMessage(AbstractCommand command) {
        commandQueue.add(command);
    }

}
