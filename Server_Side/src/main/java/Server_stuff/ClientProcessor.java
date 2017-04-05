package Server_stuff;

import Common_Resources.Data_Transmission.Object_Transmitter;
import Common_Resources.Data_Transmission.Requests.*;
import Common_Resources.Data_Transmission.Responses.*;
import static Common_Resources.Data_Transmission.Responses.USER_LOGIN_STATE.*;
import Common_Resources.domain.*;
import Validare.ExceptieValidare;
import Common_Resources.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import Services.*;




/**
 * Created by Alex on 28.03.2017.
 */


public class ClientProcessor extends Thread {

    private Socket socket;
    private Services srv;
    private Server_Stuff server;
    public ClientProcessor(Socket client,Services s,Server_Stuff _server)
    {
        socket=client;
        srv=s;
        server=_server;
    }

    private synchronized boolean handle_User_login_request(Request request)
    {
        Response ans=new User_login_response(NOT_EXISTS);
        app_user u = ((User_login_request) request).getUser();
        System.out.println(u.getPassword_hash());
        try{
            System.out.println(u.getId());
            srv.validate_user(u);
            ans=new User_login_response(EXISTS);
            server.subscribe(socket);
        }
        catch (ExceptieValidare ex) {
            if (ex.getMessage().equals("User-ul nu exista"))
                ans = new User_login_response(NOT_EXISTS);
            else
                ans = new User_login_response(WRONG_PASSWORD);
        }
        catch (Exception x){}
        try
        {   Object_Transmitter.send_object(ans,socket); }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Add_Excursie_Request(Add_Excursie_Request request)
    {
        excursie e=request.getExcursie();
        try
        {
            e=srv.add_excursie(e);
            server.notify_all_clients();
        }
        catch (IOException ex)
        {
            e=null;
        }

        try
        {   Object_Transmitter.send_object(new Excursie_Response(e),socket); }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Add_Rezervare_Request(Add_Rezervare_Request request)
    {
        rezervare e=request.getRezervare();
        try
        {
            e=srv.add_rezervare(e);
            server.notify_all_clients();
        }
        catch (IOException ex)
        {
            e=null;
        }

        try
        {   Object_Transmitter.send_object(new Rezervare_Response(e),socket); }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Delete_Rezervare_Request(Delete_Rezervare_Request request)
    {
        rezervare e=request.getRezervare();
        boolean ok=true;
        try {
            srv.delete_rezervare(e);
            server.notify_all_clients();
        }
        catch (IOException ex) {
            ok=false;
        }

        try {
            if(ok)
                Object_Transmitter.send_object(new EmptyResponse(Response_State.OK),socket);
            else
                Object_Transmitter.send_object(new EmptyResponse(Response_State.ERROR),socket);
        }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Delete_Excursie_Request(Delete_Excursie_Request request)
    {
        excursie e=request.getExcursie();
        boolean ok=true;
        try {
            srv.delete_excursie(e);
            server.notify_all_clients();
        }
        catch (IOException ex) {
            ok=false;
        }

        try {
            if(ok)
                Object_Transmitter.send_object(new EmptyResponse(Response_State.OK),socket);
            else
                Object_Transmitter.send_object(new EmptyResponse(Response_State.ERROR),socket);
        }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Get_Excursie_By_ID_Request(Get_Excursie_By_ID_Request request)
    {
        Integer key=request.getID();
        excursie e=srv.get_excursie_by_id(key);
        try {
            Object_Transmitter.send_object(new Excursie_Response(e),socket);
        }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Get_Rezervare_By_ID_Request(Get_Rezervare_By_ID_Request request)
    {
        Integer key=request.getID();
        rezervare e=srv.get_rezervare_by_id(key);
        try {
            Object_Transmitter.send_object(new Rezervare_Response(e),socket);
        }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Update_Excursie_Request(Update_Excursie_Request request)
    {
        excursie e=request.getExcursie();
        boolean ok=true;
        try {
            srv.update_excursie(e);
            server.notify_all_clients();
        }
        catch (IOException ex) {
            ok=false;
        }

        try {
            if(ok)
                Object_Transmitter.send_object(new EmptyResponse(Response_State.OK),socket);
            else
                Object_Transmitter.send_object(new EmptyResponse(Response_State.ERROR),socket);
        }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Update_Rezervare_Request(Update_Rezervare_Request request)
    {
        rezervare e=request.getRezervare();
        boolean ok=true;
        try {
            srv.update_rezervare(e);
            server.notify_all_clients();
        }
        catch (IOException ex) {
            ok=false;
        }

        try {
            if(ok)
                Object_Transmitter.send_object(new EmptyResponse(Response_State.OK),socket);
            else
                Object_Transmitter.send_object(new EmptyResponse(Response_State.ERROR),socket);
        }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Get_All_Excursie_Request(Get_All_Excursie_Request request)
    {
        List<excursie> resp=null;
        resp=srv.get_all_excursie();

        try
        {   Object_Transmitter.send_object(new ExcursieList_Response(resp),socket); }
        catch (Exception x){return false;}
        return true;
    }

    private synchronized boolean handle_Get_All_Rezervare_Request(Get_All_Rezervare_Request request)
    {
        List<rezervare> resp=null;
        resp=srv.get_all_rezervare();

        try
        {   Object_Transmitter.send_object(new RezervareList_Response(resp),socket); }
        catch (Exception x){return false;}
        return true;
    }

    @Override
    public void run()
    {
        boolean connected=true;
        while(connected)
        {
            Object request = Object_Transmitter.receive_object(socket);
            if (!(request instanceof Request))
                continue;

            if(request instanceof Disconnect_request)
                connected=false;
            else if(request instanceof User_login_request)
                connected=handle_User_login_request((Request) request);
            else if (request instanceof Add_Excursie_Request)
                connected=handle_Add_Excursie_Request((Add_Excursie_Request)request);
            else if (request instanceof Add_Rezervare_Request)
                connected=handle_Add_Rezervare_Request((Add_Rezervare_Request) request);
            else if (request instanceof Update_Excursie_Request)
                connected=handle_Update_Excursie_Request((Update_Excursie_Request)request);
            else if (request instanceof Update_Rezervare_Request)
                connected=handle_Update_Rezervare_Request((Update_Rezervare_Request) request);
            else if (request instanceof Delete_Excursie_Request)
                connected=handle_Delete_Excursie_Request((Delete_Excursie_Request)request);
            else if (request instanceof Delete_Rezervare_Request)
                connected=handle_Delete_Rezervare_Request((Delete_Rezervare_Request)request);
            else if (request instanceof Get_Excursie_By_ID_Request)
                connected=handle_Get_Excursie_By_ID_Request((Get_Excursie_By_ID_Request)request);
            else if (request instanceof Get_Rezervare_By_ID_Request)
                connected=handle_Get_Rezervare_By_ID_Request((Get_Rezervare_By_ID_Request)request);
            else if (request instanceof Get_All_Excursie_Request)
                connected=handle_Get_All_Excursie_Request((Get_All_Excursie_Request)request);
            else if (request instanceof Get_All_Rezervare_Request)
                connected=handle_Get_All_Rezervare_Request((Get_All_Rezervare_Request)request);
            else
            {
                System.out.println(request.getClass().toGenericString());
            }
        }
        try {
            server.unsubscribe(socket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}