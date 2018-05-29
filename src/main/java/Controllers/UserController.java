package Controllers;

import DatabaseManagement.User;
import DatabaseManagement.UserRepository;
import DatabaseManagement.UserRepositoryInt;
import Utils.Email;
import Utils.EmailAdapter;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.json.JSONObject;



import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;


@Path("/user")
public class UserController {

    private UserRepositoryInt userRepository;
    private EmailAdapter email;
    private HashMap<String, User> online = new HashMap<>(); // map sessions to relative users


    public UserController() {
        // TODO : export to config
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

        email = new Email();
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(User user) {
        if (addUser(user)) {
            // TODO : Add 2 camps on db. Confirmation code and confirmation boolean.
            String confirmLink = "";
            email.sendEmail(user.getEmail(), null, "Confirmation email for connect4", "Press this link to confirm your registration: " + confirmLink);
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
                online.put(newToken, user);
                return Response.ok(new JSONObject("{\"token\":\"" + user.getToken() + "\"}").toString(), MediaType.APPLICATION_JSON).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(Status.BAD_REQUEST).entity("Login failed: the provided credentials are not valid ones.").build();
    }


    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(User user){
        // TODO : Set token as null
        online.remove(user.getToken());

        try {
            userRepository.updateUserAuthToken(null, user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.ok().build();
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

    private boolean checkAuthToken(User user) {
        User newAuthUser;
//        if (user.get) {
//            return true;
//        } else {
            try {
                if ((newAuthUser = userRepository.getUserByAuthToken(user.getToken())) != null) {
                    //authenticatedUserSession.put(session, token);
                    //peers.put(session, newAuthUser);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
           return false;
        //}
    }
}
