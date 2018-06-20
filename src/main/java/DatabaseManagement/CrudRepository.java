package DatabaseManagement;

public interface CrudRepository<T> {

    boolean create(T object);

    boolean update(T object);

    boolean delete(T object);

}
