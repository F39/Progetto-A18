package Controllers;

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
    public Response getUserStats (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            UserStats userStats = userStatsRepository.getUserStats(id);
            if (userStats != null) {
                return Response.ok(userStats).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/wins")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserWins (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            Integer wins = userStatsRepository.getUserWins(id);
            if(wins != null){
                return Response.ok(new JSONObject("{\"wins\":\"" + wins +"\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/defeats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDefeats (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            Integer defeats = userStatsRepository.getUserDefeats(id);
            if (defeats != null){
                return Response.ok(new JSONObject("{\"defeats\":\"" + defeats +"\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/ties")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTies (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            Integer ties = userStatsRepository.getUserTies(id);
            if(ties != null){
                return Response.ok(new JSONObject("{\"ties\":\"" + ties +"\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPoints (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            Integer points = userStatsRepository.getUserPoints(id);
            if(points != null){
                return Response.ok(new JSONObject("{\"points\":\"" + points +"\"}").toString()).build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGames (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            int games = userStatsRepository.getUserGames(id);
            return Response.ok(new JSONObject("{\"games\":\"" + games +"\"}").toString()).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{userId}/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetUserStats (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            UserStats newUserStats = userStatsRepository.resetUserStats(id);
            return Response.ok(newUserStats).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    // TODO : to be implemented
    private boolean checkAuthToken(String token, int userId) {
        return false;
    }

}
