package Tests;

import DatabaseManagement.User;
import DatabaseManagement.UserSqlRepository;
import DatabaseManagement.UserRepositoryInt;
import DatabaseManagement.UserStatsSqlRepository;
import Utils.CORSFilter;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;

import static org.testng.Assert.*;

public class UserControllerTest {

    private User user;
    private Tomcat tomcat;
    private final String defaultPassword = "superpassword";
    @BeforeClass
    void beforeAll() {
        // create test user
        user = new User();
        user.setUsername("testUser");
        user.setEmail("mail@test.com");
        user.setPassword(defaultPassword);

        // run app server
        try {
            tomcat = new Tomcat();
            String port = "8080";
            tomcat.setPort(Integer.parseInt(port));
            String webAppDirLocation = "src/main/resources";
            Context context = tomcat.addWebapp("", new File(webAppDirLocation).getAbsolutePath());
            ResourceConfig resourceConfig = new ResourceConfig(new UserControllerRestTestResourceLoader().getClasses());
            resourceConfig.register(new CORSFilter());
            ServletContainer servletContainer = new ServletContainer(resourceConfig);
            tomcat.addServlet(context, "user-controller-test-container-servlet", servletContainer);
            context.addServletMapping("/rest/test/*", "user-controller-test-container-servlet");
        } catch (ServletException e) {
            e.printStackTrace();
        }

        UserControllerRestTest userControllerRestTest = new UserControllerRestTest(tomcat);
        Thread userControllerRestThread = new Thread(userControllerRestTest);
        userControllerRestThread.start();

    }

    @AfterClass
    void afterAll() {
        // shut down the app server
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

        // delete test user from db
        Properties dbConnectionProps;
        ConnectionSource connectionSource;
        try {
            dbConnectionProps = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/Config/db_config.properties");
            dbConnectionProps.load(in);
            in.close();
            String databaseConnectionString = dbConnectionProps.getProperty("databaseURL") + dbConnectionProps.getProperty("databaseHost") + dbConnectionProps.getProperty("databaseName");
            connectionSource = new JdbcConnectionSource(databaseConnectionString, dbConnectionProps.getProperty("databaseUser"), dbConnectionProps.getProperty("databasePassword"));
            UserRepositoryInt userRepository = new UserSqlRepository(connectionSource);
            userRepository.delete(user);
            connectionSource.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void testSignUp() {
        String payLoadSignup = String.format("{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}", this.user.getUsername(), this.user.getEmail(), this.user.getPassword());
        try {
            URI address = new URI("http", null, "localhost", 8080, "/rest/test/user/signup", null, null);
            HttpUriRequest request = new HttpPost(address);
            request.setHeader("Content-Type", "application/json");
            StringEntity se = null;
            se = new StringEntity(payLoadSignup);
            ((HttpPost) request).setEntity(se);
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 2)
    public void testLogin() {


        try {
            Properties dbConnectionProps;
            ConnectionSource connectionSource;
            dbConnectionProps = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/Config/db_config.properties");
            dbConnectionProps.load(in);
            in.close();
            String databaseConnectionString = dbConnectionProps.getProperty("databaseURL") + dbConnectionProps.getProperty("databaseHost") + dbConnectionProps.getProperty("databaseName");
            connectionSource = new JdbcConnectionSource(databaseConnectionString, dbConnectionProps.getProperty("databaseUser"), dbConnectionProps.getProperty("databasePassword"));
            UserRepositoryInt userRepository = new UserSqlRepository(connectionSource);

            byte[] salt = userRepository.getSalt(this.user.getUsername());
            User tempUser= encryption(this.user, salt);
            tempUser.setEmail_confirmed(true);
            userRepository.update(tempUser);
            this.user.setPassword(defaultPassword);
            connectionSource.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        String payLoadLogin = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", this.user.getUsername(), this.user.getPassword());
        try {
            URI address = new URI("http", null, "localhost", 8080, "/rest/test/user/login", null, null);
            HttpUriRequest request = new HttpPost(address);
            request.setHeader("Content-Type", "application/json");
            StringEntity se = null;
            se = new StringEntity(payLoadLogin);
            ((HttpPost) request).setEntity(se);
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonResponse = new JSONObject(responseString);
            this.user.setToken(jsonResponse.getString("token"));
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 3)
    public void testLogout() {
        String payLoadLogout = String.format("{\"username\":\"%s\", \"token\":\"%s\"}", this.user.getUsername(), this.user.getToken());
        try {
            URI address = new URI("http", null, "localhost", 8080, "/rest/test/user/logout", null, null);
            HttpUriRequest request = new HttpPost(address);
            request.setHeader("Content-Type", "application/json");
            StringEntity se = null;
            se = new StringEntity(payLoadLogout);
            ((HttpPost) request).setEntity(se);
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
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