package Controllers;

import DatabaseManagement.User;
import DatabaseManagement.UserRepository;
import DatabaseManagement.UserRepositoryInt;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;


@Path("/game")
public class RestGameController {

    private UserRepositoryInt userRepository;

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
    }

    @POST
    @Path("/newgame")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newGame (){
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


    private boolean checkAuthToken(User user) {
        User dbUser;
        try {
            dbUser = userRepository.getUserByAuthToken(user.getToken());
            if (dbUser != null && dbUser.getUsername() == user.getUsername()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
