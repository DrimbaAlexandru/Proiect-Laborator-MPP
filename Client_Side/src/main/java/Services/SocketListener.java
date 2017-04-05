package Services;

import Common_Resources.Data_Transmission.Object_Transmitter;
import Common_Resources.Data_Transmission.Requests.Request;
import Common_Resources.Data_Transmission.Responses.Response;
import Common_Resources.Data_Transmission.Responses.Response_State;

import java.net.Socket;

/**
 * Created by Alex on 04.04.2017.
 */
public class SocketListener extends Thread {

    private Socket_Services srv;
    private Socket socket;

    public SocketListener(Socket_Services s)
    {
        srv=s;
        socket=srv.getSocket();
    }

    @Override
    public void run() {

        boolean done=false;
        while(!done)
        {
            if(srv.getSocket().isClosed())
                break;
            Object obj= Object_Transmitter.receive_object(socket);
            if(srv.getSocket().isClosed())
                break;
            if(obj instanceof Request)
                srv.requests.add((Request)obj);
            if(obj instanceof Response)
                srv.responses.add((Response)obj);
        }
    }
}
