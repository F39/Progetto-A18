package DatabaseManagement;

public interface CrudRepository {

    boolean create(User user);
    boolean update(String[] parameters);
    boolean delete(String[] parameters);


}
