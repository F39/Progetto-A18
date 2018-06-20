package DatabaseManagement;


public interface UserRepositoryInt extends CrudRepository<User> {
    boolean updateUserAuthToken(String token, String username);
    User checkUserCredential(String username, String password);
    boolean updateUserEmailConfirmed(User user);
    User getUserByEmailToken(String token);
}
