package Common_Resources.Data_Transmission.Responses;

import Common_Resources.domain.excursie;

import java.util.List;

/**
 * Created by Alex on 03.04.2017.
 */
public class ExcursieList_Response implements Response {
    private List<excursie> list;
    public ExcursieList_Response(List<excursie> l)
    {
        list=l;
    }

    public List<excursie> getList() {
        return list;
    }
}
