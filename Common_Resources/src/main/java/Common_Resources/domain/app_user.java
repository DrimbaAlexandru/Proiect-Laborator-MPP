package Common_Resources.domain;

import java.io.Serializable;

/**
 * Created by Alex on 08.03.2017.
 */
public class app_user extends HasID<String> implements Serializable {

    private int password_hash;
    private int compute_hash(String pass)
    {
        return pass.length();
    }

    public app_user(String _username, int _password_hash)
    {
        super(_username);
        password_hash=_password_hash;
    }

    public app_user(String _username, String _password)
    {
        super(_username);
        password_hash=compute_hash(_password);

    }

    public int getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(int password_hash) {
        this.password_hash = password_hash;
    }
}
