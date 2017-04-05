package repository;

import Common_Resources.domain.HasID;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alex on 08.03.2017.
 */
public interface CRUDRepository<Elem extends HasID<IdType>, IdType>
{
    Elem add(Elem e) throws IOException;
    void replace(Elem e) throws IOException;
    void remove(IdType key) throws IOException;
    Elem getByID(IdType key);
    int get_size();
    List<Elem> getAll();
}