package DatabaseManagement;

public interface UserStatsRepositoryInt extends CrudRepository<UserStats> {

    UserStats getUserStats(User user);

    Integer getUserWins(User user);

    Integer getUserTies(User user);

    Integer getUserDefeats(User user);

    Integer getUserPoints(User user);

    Integer getUserGames(User user);

    UserStats resetUserStats(User user);

}
