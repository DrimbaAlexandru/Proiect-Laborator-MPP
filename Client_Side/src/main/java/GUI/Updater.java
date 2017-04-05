package GUI;

import Common_Resources.Data_Transmission.Requests.Invalidate_Request;
import Common_Resources.Data_Transmission.Requests.Request;
import Services.Socket_Services;

/**
 * Created by Alex on 04.04.2017.
 */
public class Updater extends Thread {
    private Socket_Services srv;
    private Main_window win;

    public Updater(Socket_Services s, Main_window w)
    {
        srv=s;
        win=w;
    }
    public void run()
    {
        try {

            while (true) {
                if(srv.getSocket().isClosed())
                    break;
                Request r = srv.requests.take();
                if(r instanceof Invalidate_Request)
                    win.refresh_view();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
