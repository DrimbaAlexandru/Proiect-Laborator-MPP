package Server_stuff;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import Common_Resources.Data_Transmission.Object_Transmitter;
import Common_Resources.Data_Transmission.Requests.Invalidate_Request;
import Services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Alex on 28.03.2017.
 */
public class Server_Stuff {
    ServerSocket server=null;
    int port;
    private Services srv;
    private List<Socket> clients=new ArrayList<>();

    public Server_Stuff(Services srv,int port)throws BindException,IOException
    {
        this.srv=srv;
        this.port=port;
    }

    public void unsubscribe(Socket s)
    {
        clients.remove(s);
    }

    public void subscribe(Socket s)
    {
        clients.add(s);
    }

    public void run()
    {
        try{
            server = new ServerSocket(port);

            while(true){
                System.out.println("WAITING");
                Socket client=server.accept();
                System.out.println("Connected");
                ClientProcessor proc=new ClientProcessor(client,srv,this);
                proc.start();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }finally{
            if(server!=null){
                try{
                    server.close();
                }catch(IOException ex){ex.printStackTrace();}
            }
        }
    }

    public synchronized void notify_all_clients()
    {
        for(Socket s : clients)
            Object_Transmitter.send_object(new Invalidate_Request(),s);
    }
}
