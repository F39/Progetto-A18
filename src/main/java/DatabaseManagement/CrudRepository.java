package DatabaseManagement;

import java.sql.SQLException;

public interface CrudRepository {

    public void create(User user) throws SQLException;
    public void update(String[] parameters) throws SQLException;
    public void delete(String[] parameters) throws SQLException;


}
