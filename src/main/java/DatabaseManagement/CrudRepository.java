package DatabaseManagement;

import java.sql.SQLException;

public interface CrudRepository {

    boolean create(User user) throws SQLException;
    boolean update(String[] parameters) throws SQLException;
    boolean delete(String[] parameters) throws SQLException;


}
