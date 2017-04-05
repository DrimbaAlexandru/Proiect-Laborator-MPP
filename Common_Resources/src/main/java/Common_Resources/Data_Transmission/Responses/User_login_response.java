package Common_Resources.Data_Transmission.Responses;

/**
 * Created by Alex on 02.04.2017.
 */



public class User_login_response implements Response
{
    private USER_LOGIN_STATE state;
    public User_login_response(USER_LOGIN_STATE _state)
    {
        state=_state;
    }

    public USER_LOGIN_STATE getState() {
        return state;
    }
}