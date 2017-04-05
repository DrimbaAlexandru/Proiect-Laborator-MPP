package Common_Resources.Data_Transmission.Requests;

/**
 * Created by Alex on 03.04.2017.
 */
public class Get_Excursie_By_ID_Request implements Request{
    private Integer ID;

    public Get_Excursie_By_ID_Request(Integer id)
    {
        ID=id;
    }

    public Integer getID() {
        return ID;
    }
}
