package repository;

import Common_Resources.domain.HasID;
import repository.CRUDRepository;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alex on 13.03.2017.
 */

public interface ExtendedCrudRepository<Elem extends HasID<IdType>, IdType> extends CRUDRepository<Elem,IdType>
{
    List<IdType> get_All_keys();
    default int get_size() {return get_All_keys().size();}
    List<Elem> getByID(List<IdType> keys);
    default List<Elem> getAll() {return getByID(get_All_keys());};
}