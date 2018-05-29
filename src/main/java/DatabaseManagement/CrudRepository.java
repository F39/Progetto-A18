package DatabaseManagement;

import java.sql.SQLException;

public interface CrudRepository {

    void create(User user) throws SQLException;
    void update(String[] parameters) throws SQLException;
    void delete(String[] parameters) throws SQLException;


}
