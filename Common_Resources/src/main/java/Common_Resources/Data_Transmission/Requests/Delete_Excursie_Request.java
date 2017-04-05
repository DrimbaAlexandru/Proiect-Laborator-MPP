package Common_Resources.Data_Transmission.Requests;

import Common_Resources.domain.excursie;

/**
 * Created by Alex on 03.04.2017.
 */
public class Delete_Excursie_Request implements Request {
    private excursie exc;

    public Delete_Excursie_Request(excursie e)
    {
        exc=e;
    }

    public excursie getExcursie() {
        return exc;
    }
}
