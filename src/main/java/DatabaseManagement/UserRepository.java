package DatabaseManagement;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class UserRepository implements CrudRepository{


    ConnectionSource connectionSource;
    Dao<User, Integer> UserDao;
    User u;


    public UserRepository(ConnectionSource connectionSource) throws SQLException {
        this.connectionSource = connectionSource;
        this.UserDao = DaoManager.createDao(connectionSource, User.class);

    }

    @Override
    public void create(String[] parameters) throws SQLException {

        u = new User();
        u.setUser(parameters[0]);
        u.setMail(parameters[1]);
        u.setPassword(parameters[2]);
        UserDao.create(u);

    }

    @Override
    public void update( String[] parameters) throws SQLException {
        for (User u : UserDao) {
            if (u.getUser().equals(parameters[0])) {
                u.setUser(parameters[1]);
                UserDao.update(u);
            }
        }



    }

    @Override
    public void delete(String[] parameters) throws SQLException {
        for (User u : UserDao) {
            if (u.getUser().equals(parameters[0])) {
                UserDao.delete(u);
            }
        }


    }

    public User getUserByAuthToken(String token) {
        return null; // You shall not pass!
    }

}
