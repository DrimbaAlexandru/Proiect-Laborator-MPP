package Common_Resources.Data_Transmission.Responses;

import Common_Resources.domain.excursie;

/**
 * Created by Alex on 03.04.2017.
 */
public class Excursie_Response implements Response {
    private excursie exc;
    public Excursie_Response(excursie e)
    {
        exc=e;
    }

    public excursie getExcursie() {
        return exc;
    }
}
