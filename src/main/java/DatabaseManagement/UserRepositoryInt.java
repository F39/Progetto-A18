package DatabaseManagement;

/**
 * Interface providing the create, update, delete methods for the User entity of the DB
 */
public interface UserRepositoryInt extends CrudRepository<User> {

    boolean updateUserAuthToken(String token, String username);

    User checkUserCredential(String username, String password);

    boolean updateUserEmailConfirmed(User user);

    User getUserByEmailToken(String token);

    byte[] getSalt(String username);
}
