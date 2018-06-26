package DatabaseManagement;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Class that implements the user's statistics on the sql DB
 */
public class UserStatsSqlRepository implements UserStatsRepositoryInt {

    Dao<UserStats, Integer> statsDao;

    public UserStatsSqlRepository(ConnectionSource connectionSource) throws SQLException {
        this.statsDao = DaoManager.createDao(connectionSource, UserStats.class);
    }

    @Override
    public UserStats getUserStats(User user) {
        try {
            QueryBuilder<UserStats, Integer> queryBuilder = statsDao.queryBuilder();
            queryBuilder.where().eq(UserStats.USER_ID_FIELD_NAME, user);
            PreparedQuery<UserStats> preparedQuery = queryBuilder.prepare();
            List<UserStats> userStatsList = null;
            userStatsList = statsDao.query(preparedQuery);
            if (!userStatsList.isEmpty()) {
                return userStatsList.get(0);
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getUserWins(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            return userStats.getWins();
        }
        return null;
    }

    @Override
    public Integer getUserTies(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            return userStats.getTies();
        }
        return null;
    }

    @Override
    public Integer getUserDefeats(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            return userStats.getDefeats();
        }
        return null;
    }

    @Override
    public Integer getUserPoints(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            return userStats.getPoints();
        }
        return null;
    }

    @Override
    public Integer getUserGames(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            return userStats.getGames();
        }
        return null;
    }

    @Override
    public UserStats resetUserStats(User user) {
        UserStats userStats = getUserStats(user);
        if(delete(userStats)){
            UserStats newUserStats = new UserStats(user);
            create(newUserStats);
            return newUserStats;
        }
        else return null;
    }

    @Override
    public boolean addUserWin(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            userStats.addWin();
            return update(userStats);
        }
        return false;
    }

    @Override
    public boolean addUserTie(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            userStats.addTie();
            return update(userStats);
        }
        return false;
    }

    @Override
    public boolean addUserDefeat(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            userStats.addDefeat();
            return update(userStats);
        }
        return false;
    }

    @Override
    public boolean addUserGame(User user) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            userStats.addGame();
            return update(userStats);
        }
        return false;
    }

    @Override
    public boolean addUserPoints(User user, int points) {
        UserStats userStats = getUserStats(user);
        if (userStats != null){
            userStats.addPoints(points);
            return update(userStats);
        }
        return false;
    }

    @Override
    public boolean create(UserStats userStats) {
        try {
            statsDao.create(userStats);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(UserStats userStats) {
        try {
            statsDao.update(userStats);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(UserStats userStats) {
        try {
            statsDao.delete(userStats);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
