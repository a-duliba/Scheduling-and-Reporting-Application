package helper;

import javafx.collections.ObservableList;
import java.util.Optional;

public interface DAO<T> {

    /**
     *
     * @param id
     * @return
     */
    Optional<T> get(int id);

    ObservableList<T> getAll();

    boolean insert(T t);

    boolean update(T t);

    boolean delete(int id);
}
