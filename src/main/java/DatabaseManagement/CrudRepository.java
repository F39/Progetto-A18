package DatabaseManagement;

import java.sql.SQLException;

public interface CrudRepository {

    boolean create(User user) throws SQLException;

    boolean update(User user) throws SQLException;

    boolean delete(User user) throws SQLException;

}
