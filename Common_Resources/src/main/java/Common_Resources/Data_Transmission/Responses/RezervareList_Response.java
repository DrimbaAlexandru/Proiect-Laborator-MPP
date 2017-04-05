package Common_Resources.Data_Transmission.Responses;

import Common_Resources.domain.rezervare;

import java.util.List;

/**
 * Created by Alex on 03.04.2017.
 */
public class RezervareList_Response implements Response {
    private List<rezervare> list;
    public RezervareList_Response(List<rezervare> l)
    {
        list=l;
    }

    public List<rezervare> getList() {
        return list;
    }
}
