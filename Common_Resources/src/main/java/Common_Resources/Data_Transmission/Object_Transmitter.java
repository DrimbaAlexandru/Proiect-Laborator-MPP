package Common_Resources.Data_Transmission;

import Common_Resources.Data_Transmission.Requests.Disconnect_request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Alex on 02.04.2017.
 */
public class Object_Transmitter
{
    public static void send_object(Object o,Socket s)
    {

        try {
            ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(o);
            //oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Object receive_object(Socket s)
    {
        try{
            ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
            Object rez=ois.readObject();
            //ois.close();
            return rez;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Disconnect_request();
        }
    }
}