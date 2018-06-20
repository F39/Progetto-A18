package Controllers;

import DatabaseManagement.UserSqlStatsRepository;
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
            userStatsRepository = new UserSqlStatsRepository(connectionSource);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/{userId}/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newGame (@HeaderParam("token") String token, @PathParam("userId") String userId){
        int id = Integer.parseInt(userId);
        if(checkAuthToken(token, id)){
            UserStats userStats = userStatsRepository.getUserStats(id);
            return Response.ok(new JSONObject().toString(), MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    private boolean checkAuthToken(String token, int userId) {
        return false;
    }

}
