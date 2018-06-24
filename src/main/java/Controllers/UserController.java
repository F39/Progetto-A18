package Controllers;

import DatabaseManagement.*;
import GameLogic.Player;
import Logger.Logger;
import Utils.Email;
import Utils.EmailAdapter;
import Utils.OnlineChecker;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.json.JSONObject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.*;

@Singleton
@Path("/user")
public class UserController {

    private UserRepositoryInt userRepository;
    private UserStatsRepositoryInt userStatsRepository;
    private EmailAdapter email;
    private static Map<String, Player> online = new HashMap<>(); // map sessions to relative users
    private OnlineChecker onlineChecker;
    private final static long threshold = 60;
    private Logger logger;

    public UserController() {
        Properties dbConnectionProps;
        ConnectionSource connectionSource;
        try {
            dbConnectionProps = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/Config/db_config.properties");
            dbConnectionProps.load(in);
            in.close();
            String databaseConnectionString = dbConnectionProps.getProperty("databaseURL") + dbConnectionProps.getProperty("databaseHost") + dbConnectionProps.getProperty("databaseName");
            connectionSource = new JdbcConnectionSource(databaseConnectionString, dbConnectionProps.getProperty("databaseUser"), dbConnectionProps.getProperty("databasePassword"));
            userRepository = new UserSqlRepository(connectionSource);
            userStatsRepository = new UserStatsSqlRepository(connectionSource);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        email = new Email();
        onlineChecker = new OnlineChecker();
        Thread onlineCheckerThread = new Thread(onlineChecker);
        onlineCheckerThread.start();

        logger = Logger.getInstance();
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(@HeaderParam("host") String host, User user) {
        String confirmLink = generateAuthToken();
        user.setEmail_token(confirmLink);
        if (addUser(user)) {
            String url = "http://" + host + "/rest/user/confirm/";
            email.sendEmail(user.getEmail(), null, "Confirmation email for connect4", "Press this link to confirm your registration: " + url + confirmLink);
            return Response.status(Status.OK).build();
        }
        logger.log("Signup procedure completed successfully");
        return Response.status(Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/confirm/{token}")
    public InputStream confirmEmail(@PathParam("token") String token) {
        User toConfirm;
        try {
            if((toConfirm = userRepository.getUserByEmailToken(token)) != null ){
                toConfirm.setEmail_confirmed(true);
                userRepository.updateUserEmailConfirmed(toConfirm);
                File f = new File("src/main/resources/WebClient/emailconfirmed.html");
                return new FileInputStream(f);
            }else{
                File f = new File("src/main/resources/WebClient/emailnotconfirmed.html");
                return new FileInputStream(f);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        byte[] salt;
        if((salt = userRepository.getSalt(user.getUsername())) !=null){
            user = encryption(user,salt);
            if ((user = userRepository.checkUserCredential(user.getUsername(), user.getPassword())) != null) {
                String newToken = generateAuthToken();
                userRepository.updateUserAuthToken(newToken, user.getUsername());
                user.setToken(newToken);
                online.put(newToken, new Player(user));
                logger.log(String.format("User %s logged in successfully", user.getUsername()));
                return Response.ok(new JSONObject("{\"token\":\"" + user.getToken() + "\", \"userId\":\"" + user.getId() + "\"}").toString(), MediaType.APPLICATION_JSON).build();
            }
        }

        return Response.status(Status.BAD_REQUEST).entity("Login failed: provided credentials are not valid.").build();
    }

    @POST
    @Path("/loginTCP")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginTCP(User user) {
        byte[] salt;
        if((salt = userRepository.getSalt(user.getUsername())) !=null){
            user = encryption(user,salt);
            if ((user = userRepository.checkUserCredential(user.getUsername(), user.getPassword())) != null) {
                String newToken = generateAuthToken();
                userRepository.updateUserAuthToken(newToken, user.getUsername());
                user.setToken(newToken);
                Player player = new Player(user);
                player.setHasToPoll(false);
                online.put(newToken, player);
                logger.log(String.format("User %s logged in successfully", user.getUsername()));
                return Response.ok(new JSONObject("{\"token\":\"" + user.getToken() + "\", \"userId\":\"" + user.getId() + "\"}").toString(), MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.status(Status.BAD_REQUEST).entity("Login failed: provided credentials are not valid.").build();
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(User user) {
        online.remove(user.getToken());
        userRepository.updateUserAuthToken(null, user.getUsername());
        logger.log(String.format("User %s logged out successfully", user.getUsername()));
        return Response.ok().build();
    }

    private boolean addUser(User user) {
        user = encryption(user);
        if (userRepository.create(user)) {
            UserStats userStats = new UserStats(user);
            return userStatsRepository.create(userStats);
        }
        return false;
    }

    private String generateAuthToken() {
        UUID authToken = UUID.randomUUID();
        return authToken.toString();
    }

    public static Map<String, Player> getOnline() {
        return online;
    }

    public static void removeOfflinePlayers() {
        for (Player player : online.values()) {
            if(player.hasToPoll()){
                if (System.currentTimeMillis() - player.getLastPoll() > threshold * 1000) {
                    online.values().remove(player);
                    Logger.getInstance().log(String.format("Removed offline player %s ", player.getUsername()));
                }
            }
        }
    }

    public User encryption(User user){
        byte[] salt = new byte[16];
        Random random = new Random();
        random.nextBytes(salt);
        encryption(user, salt);
        return user;
    }
    public User encryption(User user, byte[] salt){
        try {
            KeySpec spec = new PBEKeySpec(user.getPassword().toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = null;
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            user.setPassword(enc.encodeToString(hash));
            user.setSalt(enc.encodeToString(salt));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return user;
    }

}
