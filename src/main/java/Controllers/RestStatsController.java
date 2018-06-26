package Controllers;

import DatabaseManagement.User;
import DatabaseManagement.UserStatsSqlRepository;
import DatabaseManagement.UserStats;
import DatabaseManagement.UserStatsRepositoryInt;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.json.JSONObject;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Rest Servlet to handle the endpoints related to the StatsController logic level
 */
@Singleton
@Path("/stats")
public class RestStatsController {

    private UserStatsRepositoryInt userStatsRepository;

    public RestStatsController() {
        Properties dbConnectionProps;
        ConnectionSource connectionSource;
        try {
            dbConnectionProps = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/Config/db_config.properties");
            dbConnectionProps.load(in);
            in.close();
            String databaseConnectionString = dbConnectionProps.getProperty("databaseURL") + dbConnectionProps.getProperty("databaseHost") + dbConnectionProps.getProperty("databaseName");
            connectionSource = new JdbcConnectionSource(databaseConnectionString, dbConnectionProps.getProperty("databaseUser"), dbConnectionProps.getProperty("databasePassword"));
            userStatsRepository = new UserStatsSqlRepository(connectionSource);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserStats(@HeaderParam("token") String token, @PathParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        User user;
        if ((user = checkAuthToken(token, id)) != null) {
            UserStats userStats = userStatsRepository.getUserStats(user);
            if (userStats != null) {
                return Response.ok(userStats).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/wins")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserWins(@HeaderParam("token") String token, @PathParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        User user;
        if ((user = checkAuthToken(token, id)) != null) {
            Integer wins = userStatsRepository.getUserWins(user);
            if (wins != null) {
                return Response.ok(new JSONObject("{\"wins\":\"" + wins + "\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/defeats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDefeats(@HeaderParam("token") String token, @PathParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        User user;
        if ((user = checkAuthToken(token, id)) != null) {
            Integer defeats = userStatsRepository.getUserDefeats(user);
            if (defeats != null) {
                return Response.ok(new JSONObject("{\"defeats\":\"" + defeats + "\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/ties")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTies(@HeaderParam("token") String token, @PathParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        User user;
        if ((user = checkAuthToken(token, id)) != null) {
            Integer ties = userStatsRepository.getUserTies(user);
            if (ties != null) {
                return Response.ok(new JSONObject("{\"ties\":\"" + ties + "\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPoints(@HeaderParam("token") String token, @PathParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        User user;
        if ((user = checkAuthToken(token, id)) != null) {
            Integer points = userStatsRepository.getUserPoints(user);
            if (points != null) {
                return Response.ok(new JSONObject("{\"points\":\"" + points + "\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/games")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGames(@HeaderParam("token") String token, @PathParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        User user;
        if ((user = checkAuthToken(token, id)) != null) {
            int games = userStatsRepository.getUserGames(user);
            return Response.ok(new JSONObject("{\"games\":\"" + games + "\"}").toString()).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetStats(@HeaderParam("token") String token, @PathParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        User user;
        if ((user = checkAuthToken(token, id)) != null) {
            UserStats newUserStats = userStatsRepository.resetUserStats(user);
            return Response.ok(newUserStats).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    private User checkAuthToken(String token, int userId) {
        User checkUser = UserController.getOnline().get(token).getUser();
        if (checkUser == null) {
            return null;
        }
        if (userId == checkUser.getId()) {
            return checkUser;
        }
        return null;
    }

}
