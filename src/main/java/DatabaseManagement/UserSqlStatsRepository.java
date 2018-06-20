package DatabaseManagement;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class UserSqlStatsRepository implements UserStatsRepositoryInt {

    Dao<UserStats, String> statsDao;

    public UserSqlStatsRepository(ConnectionSource connectionSource) throws SQLException {
        this.statsDao = DaoManager.createDao(connectionSource, UserStats.class);
    }

    @Override
    public UserStats getUserStats(int userId) {
        return null;
    }

    @Override
    public int getUserWins(int userId) {
        return 0;
    }

    @Override
    public int getUserTies(int userId) {
        return 0;
    }

    @Override
    public int getUserDefeats(int userId) {
        return 0;
    }

    @Override
    public int getUserPoints(int userId) {
        return 0;
    }

    @Override
    public boolean create(UserStats object) {
        return false;
    }

    @Override
    public boolean update(UserStats object) {
        return false;
    }

    @Override
    public boolean delete(UserStats object) {
        return false;
    }
}
