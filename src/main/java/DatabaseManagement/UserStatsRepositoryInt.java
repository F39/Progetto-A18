package DatabaseManagement;
/**
 * Interface providing the create, update, delete methods for the UserStats entity of the DB
 */
public interface UserStatsRepositoryInt extends CrudRepository<UserStats> {

    UserStats getUserStats(User user);

    Integer getUserWins(User user);

    Integer getUserTies(User user);

    Integer getUserDefeats(User user);

    Integer getUserPoints(User user);

    Integer getUserGames(User user);

    UserStats resetUserStats(User user);

    boolean addUserWin(User user);

    boolean addUserTie(User user);

    boolean addUserDefeat(User user);

    boolean addUserGame(User user);

    boolean addUserPoints(User user, int points);

}
