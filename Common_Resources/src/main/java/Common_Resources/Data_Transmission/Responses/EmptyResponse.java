package Common_Resources.Data_Transmission.Responses;

/**
 * Created by Alex on 02.04.2017.
 */
public class EmptyResponse implements Response {
    private Response_State state;

    public EmptyResponse(Response_State s){state=s;}

    public Response_State getState() {
        return state;
    }
}
