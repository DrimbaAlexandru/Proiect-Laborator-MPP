package Common_Resources.Data_Transmission.Requests;

import Common_Resources.domain.app_user;

/**
 * Created by Alex on 02.04.2017.
 */
public class User_login_request implements Request
{
    app_user user;

    public User_login_request(app_user u)
    {
        user=u;
    }

    public app_user getUser() {
        return user;
    }
}