package DatabaseManagement;

import Utils.DBException;


public interface UserRepositoryInt extends CrudRepository {
    boolean updateUserAuthToken(String token, String username);
    User getUserByAuthToken(String token);
    User checkUserCredential(String username, String password);
    boolean updateUserEmailConfirmed(User user);
    User getUserByEmailToken(String token);
}
