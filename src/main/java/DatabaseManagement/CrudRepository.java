package DatabaseManagement;
/**
 * Interface providing the create, update, delete methods for the DB
 */
public interface CrudRepository<T> {

    boolean create(T object);

    boolean update(T object);

    boolean delete(T object);

}
