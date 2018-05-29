package DatabaseManagement;

import java.sql.SQLException;

public interface UserRepositoryInt extends CrudRepository {
    void updateUserAuthToken(String token, String username) throws SQLException;
    User getUserByAuthToken(String token) throws SQLException;
    User checkUserCredential(String username, String password) throws SQLException;
    void updateUserEmailConfirmed(User user) throws SQLException;
    User getUserByEmailToken(String token) throws SQLException;
}
