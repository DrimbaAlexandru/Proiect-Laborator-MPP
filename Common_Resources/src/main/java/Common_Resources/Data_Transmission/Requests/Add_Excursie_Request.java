package Common_Resources.Data_Transmission.Requests;

import Common_Resources.domain.excursie;

/**
 * Created by Alex on 03.04.2017.
 */
public class Add_Excursie_Request implements Request{
    private excursie exc;

    public Add_Excursie_Request(excursie exc)
    {
        this.exc=exc;
    }

    public excursie getExcursie() {
        return exc;
    }
}
