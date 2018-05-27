package DatabaseManagement;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class UserRepository implements CrudRepository {

    private Dao<User, Integer> userDao;

    public UserRepository(ConnectionSource connectionSource) throws SQLException {
        this.userDao = DaoManager.createDao(connectionSource, User.class);
    }

    @Override
    public void create(User user) throws SQLException {
        userDao.create(user);
    }

    @Override
    public void update(String[] parameters) throws SQLException {
        for (User u : userDao) {
            if (u.getUsername().equals(parameters[0])) {
                u.setUsername(parameters[1]);
                userDao.update(u);
            }
        }
    }

    @Override
    public void delete(String[] parameters) throws SQLException {
        for (User u : userDao) {
            if (u.getUsername().equals(parameters[0])) {
                userDao.delete(u);
            }
        }
    }

    public void updateUserAuthToken(String token, String username) throws SQLException {
        UpdateBuilder<User, Integer> updateBuilder = userDao.updateBuilder();
        updateBuilder.updateColumnValue(User.AUTH_TOKEN_FIELD_NAME, token);
//        updateBuilder.where().isNull(User.AUTH_TOKEN_FIELD_NAME).and().eq(User.USERNAME_FIELD_NAME, username);
        updateBuilder.where().eq(User.USERNAME_FIELD_NAME, username);
        updateBuilder.update();
    }

    public User getUserByAuthToken(String token) throws SQLException {
        QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
        queryBuilder.where().eq(User.AUTH_TOKEN_FIELD_NAME, token);
        PreparedQuery<User> preparedQuery = queryBuilder.prepare();
        List<User> userList = userDao.query(preparedQuery);
        if (!userList.isEmpty()) {
            return userList.get(0);
        }
        else return null;
    }

    public User checkUserCredential(String username, String password) throws SQLException {
        QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
        queryBuilder.where().eq(User.PASSWORD_FIELD_NAME, password).and().eq(User.USERNAME_FIELD_NAME, username);
        PreparedQuery<User> preparedQuery = queryBuilder.prepare();
        List<User> userList = userDao.query(preparedQuery);
        if (!userList.isEmpty()) {
            return userList.get(0);
        }
        else return null;
    }

}
