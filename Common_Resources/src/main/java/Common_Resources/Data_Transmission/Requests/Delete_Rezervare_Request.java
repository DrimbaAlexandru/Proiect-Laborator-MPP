package Common_Resources.Data_Transmission.Requests;

import Common_Resources.domain.rezervare;

/**
 * Created by Alex on 03.04.2017.
 */
public class Delete_Rezervare_Request implements Request {

    private rezervare rez;

    public Delete_Rezervare_Request(rezervare r)
    {
        rez=r;
    }

    public rezervare getRezervare() {
        return rez;
    }
}
