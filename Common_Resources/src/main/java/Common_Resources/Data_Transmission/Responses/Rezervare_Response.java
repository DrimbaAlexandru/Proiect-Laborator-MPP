package Common_Resources.Data_Transmission.Responses;

import Common_Resources.domain.rezervare;

/**
 * Created by Alex on 03.04.2017.
 */
public class Rezervare_Response implements Response {
    private rezervare rez;
    public Rezervare_Response(rezervare r)
    {
        rez=r;
    }

    public rezervare getRezervare() {
        return rez;
    }
}
