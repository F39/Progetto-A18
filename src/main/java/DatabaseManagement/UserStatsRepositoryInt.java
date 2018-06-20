package DatabaseManagement;

public interface UserStatsRepositoryInt extends CrudRepository<UserStats> {

    UserStats getUserStats(int userId);

    int getUserWins(int userId);

    int getUserTies(int userId);

    int getUserDefeats(int userId);

    int getUserPoints(int userId);

}
