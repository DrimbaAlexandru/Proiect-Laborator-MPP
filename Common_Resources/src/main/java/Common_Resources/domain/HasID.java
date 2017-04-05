package Common_Resources.domain;

import java.io.Serializable;

/**
 * Created by Alex on 08.03.2017.
 */
public class HasID<IdType> implements Serializable {
    private IdType Id;

    public HasID () {};
    public HasID (IdType id) {Id=id;}

    public IdType getId() {
        return Id;
    }

    public void setId(IdType id) {
        Id = id;
    }
}
