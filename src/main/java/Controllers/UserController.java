package Controllers;

import DatabaseManagement.User;
import DatabaseManagement.UserRepository;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.SQLException;
import java.util.UUID;

@Path("/user")
public class UserController {

    private UserRepository userRepository;

    public UserController() {
        ConnectionSource connectionSource;
        // TODO : export to config
        String databaseUrl = "jdbc:mysql://localhost:3306/forza4";
        String dbUser = "root";
        String dbPass = "delta";
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl, dbUser, dbPass);
            userRepository = new UserRepository(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(User user) {
        System.out.println(user.getUsername());
        if (addUser(user)) {
            return Response.status(Status.OK).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        try {
            if ((user = userRepository.checkUserCredential(user.getUsername(), user.getPassword())) != null) {
                String newToken = generateAuthToken();
                userRepository.updateUserAuthToken(newToken, user.getUsername());
                user.setToken(newToken);
                return Response.ok(new JSONObject("{\"token\":\"" + user.getToken() + "\"}").toString(), MediaType.APPLICATION_JSON).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(Status.BAD_REQUEST).entity("Login failed: the provided credentials are not valid ones.").build();
    }

    private boolean addUser(User user) {
        try {
            userRepository.create(user);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateAuthToken() {
        UUID authToken = UUID.randomUUID();
        return authToken.toString();
    }

}
