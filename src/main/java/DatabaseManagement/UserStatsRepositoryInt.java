package DatabaseManagement;

public interface UserStatsRepositoryInt extends CrudRepository<UserStats> {

    UserStats getUserStats(int userId);

    Integer getUserWins(int userId);

    Integer getUserTies(int userId);

    Integer getUserDefeats(int userId);

    Integer getUserPoints(int userId);

    Integer getUserGames(int userId);

    UserStats resetUserStats(int userId);

}
