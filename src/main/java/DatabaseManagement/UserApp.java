package DatabaseManagement;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class UserApp {
    public static void main(String[] args) throws Exception {

        String databaseUrl = "jdbc:mysql://localhost:3306/forza4";
        String dbUser = "root";
        String dbPass = "";

        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl, dbUser, dbPass);
        UserRepository userRepository = new UserRepository(connectionSource);

        TableUtils.dropTable(connectionSource, User.class, true);
        TableUtils.createTable(connectionSource,User.class);

        User user = new User();
        user.setUsername("test");
        user.setEmail("mail@test.com");
        user.setPassword("superpassword");
        user.setToken("tatata");

        userRepository.create(user);
        userRepository.updateUserAuthToken(null, user.getUsername());

//        User authUser = userRepository.checkUserCredential("test","superpassword");
//        System.out.println(authUser.getEmail());
    }
}
