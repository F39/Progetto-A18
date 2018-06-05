package DatabaseManagement;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;



import java.sql.SQLException;
import java.util.List;


public class UserRepository implements UserRepositoryInt {

    private Dao<User, Integer> userDao;

    public UserRepository(ConnectionSource connectionSource) throws SQLException {
        this.userDao = DaoManager.createDao(connectionSource, User.class);
    }

    @Override
    public boolean create(User user){
        try {
            userDao.create(user);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(String[] parameters){
        for (User u : userDao) {
            if (u.getUsername().equals(parameters[0])) {
                u.setUsername(parameters[1]);
                try {
                    userDao.update(u);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        }
        return false;
    }

    @Override
    public boolean delete(String[] parameters) {
        for (User u : userDao) {
            if (u.getUsername().equals(parameters[0])) {
                try {
                    userDao.delete(u);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateUserAuthToken(String token, String username) {
        try {
            UpdateBuilder<User, Integer> updateBuilder = userDao.updateBuilder();
            updateBuilder.updateColumnValue(User.AUTH_TOKEN_FIELD_NAME, token);
//        updateBuilder.where().isNull(User.AUTH_TOKEN_FIELD_NAME).and().eq(User.USERNAME_FIELD_NAME, username);
            updateBuilder.where().eq(User.USERNAME_FIELD_NAME, username);
            updateBuilder.update();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUserEmailConfirmed(User user){
        try {
            UpdateBuilder<User, Integer> updateBuilder = userDao.updateBuilder();
            updateBuilder.updateColumnValue(User.EMAIL_CONFIRMED_FIELD_NAME, true);
            updateBuilder.where().eq(User.USERNAME_FIELD_NAME, user.getUsername());
            updateBuilder.update();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public User getUserByEmailToken(String token){
        try {
            QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
            queryBuilder.where().eq(User.EMAIL_TOKEN_FIELD_NAME, token);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            List<User> userList = null;
            userList = userDao.query(preparedQuery);
            if (!userList.isEmpty()) {
                return userList.get(0);
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserByAuthToken(String token){
        try {
            QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
            queryBuilder.where().eq(User.AUTH_TOKEN_FIELD_NAME, token);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            List<User> userList = null;
            userList = userDao.query(preparedQuery);
            if (!userList.isEmpty()) {
                return userList.get(0);
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User checkUserCredential(String username, String password){
        try {
            QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
            queryBuilder.where().eq(User.PASSWORD_FIELD_NAME, password).and().eq(User.USERNAME_FIELD_NAME, username).and().eq(User.EMAIL_CONFIRMED_FIELD_NAME, true);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            List<User> userList = null;
            userList = userDao.query(preparedQuery);
            if (!userList.isEmpty()) {
                return userList.get(0);
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
