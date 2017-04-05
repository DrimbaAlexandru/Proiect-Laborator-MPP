package Services;

import Common_Resources.Data_Transmission.Object_Transmitter;
import Common_Resources.Data_Transmission.Requests.*;
import Common_Resources.Data_Transmission.Responses.USER_LOGIN_STATE;
import Common_Resources.Data_Transmission.Responses.*;
import Validare.*;
import Common_Resources.domain.app_user;
import Common_Resources.domain.excursie;
import Common_Resources.domain.rezervare;
import repository.RepoExcursie.IRepoExcursie;
import repository.RepoExcursie.SQLRepoExcursie;
import repository.RepoRezervari.IRepoRezervari;
import repository.RepoRezervari.SQLRepoRezervari;
import repository.RepoUsers.IRepoUsers;
import repository.RepoUsers.SQLRepoUsers;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;



/**
 * Created by Alex on 28.03.2017.
 */

public class Socket_Services implements IServices
{
    private InetAddress localHost;
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public BlockingQueue<Request> requests=new ArrayBlockingQueue<Request>(100);
    public BlockingQueue<Response> responses=new ArrayBlockingQueue<Response>(100);


    private Validator<excursie> val_excursie;
    private Validator<rezervare> val_rezervare;
    private Validator<app_user> val_user;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Object_Transmitter.send_object(new Disconnect_request(),socket);
        socket.close();
        System.out.println("GATA");
    }

    public Socket_Services(/*Connection _con*/)
    {
        //repo_excursie = new SQLRepoExcursie(_con);
        //repo_rezervari = new SQLRepoRezervari(_con);
        //repo_useri = new SQLRepoUsers(_con);

        try {
            localHost = InetAddress.getLocalHost();
            socket = new Socket(localHost, 50505);
        }
        catch (Exception e)
        {
            localHost=null;
            socket=null;
        }

        val_excursie = new Validator_excursie();
        val_rezervare = new Validator_rezervare();
        val_user = new Validator_user();

        SocketListener sl=new SocketListener(this);
        sl.start();

    }

    public excursie add_excursie(excursie e) throws IOException
    {
        val_excursie.validate(e);

        Response res=send_request_receive_response(new Add_Excursie_Request(e));
        if(res instanceof Excursie_Response) {
            Excursie_Response exres = (Excursie_Response) res;
            return exres.getExcursie();
        }
        throw new IOException("Something happened ");
        /*
        return repo_excursie.add(e);*/
    }

    public rezervare add_rezervare(rezervare e) throws IOException
    {
        val_rezervare.validate(e);
        if (get_excursie_by_id(e.getId_excursie()) == null)
        {
            throw new ExceptieValidare("ID-ul excursiei nu e corect");
        }
        if(get_locuri_libere(get_excursie_by_id(e.getId_excursie()))<e.getNr_bilete())
        {
            throw new ExceptieValidare("Nu mai exista suficiente locuri.");
        }

        Response res=send_request_receive_response(new Add_Rezervare_Request(e));
        if(res instanceof Rezervare_Response) {
            Rezervare_Response exres = (Rezervare_Response) res;
            return exres.getRezervare();
        }
        throw new IOException("Something happened ");

        //return repo_rezervari.add(e);
    }

    public void update_excursie(excursie e)throws IOException
    {
        val_excursie.validate(e);
        Response res=send_request_receive_response(new Update_Excursie_Request(e));
        if(res instanceof EmptyResponse) {
            EmptyResponse exres = (EmptyResponse) res;
            if(exres.getState()!=Response_State.OK)
                throw new IOException("Something happened ");
            return;
        }
        throw new IOException("Something happened ");

        //repo_excursie.replace(e);
    }

    public void update_rezervare(rezervare e) throws IOException
    {
        val_rezervare.validate(e);
        if (get_excursie_by_id(e.getId_excursie()) == null)
        {
            throw new ExceptieValidare("ID-ul excursiei nu e corect");
        }
        if(get_locuri_libere(get_excursie_by_id(e.getId_excursie()))<e.getNr_bilete())
        {
            throw new ExceptieValidare("Nu mai exista suficiente locuri.");
        }

        Response res=send_request_receive_response(new Update_Rezervare_Request(e));
        if(res instanceof EmptyResponse) {
            EmptyResponse exres = (EmptyResponse) res;
            if(exres.getState()!=Response_State.OK)
                throw new IOException("Something happened ");
            return;
        }
        throw new IOException("Something happened ");

        //repo_rezervari.replace(e);
    }

    public void delete_excursie(excursie e)throws IOException
    {
        //repo_excursie.remove(e.getId());
        Response res=send_request_receive_response(new Delete_Excursie_Request(e));
        if(res instanceof EmptyResponse) {
            EmptyResponse exres = (EmptyResponse) res;
            if(exres.getState()!=Response_State.OK)
                throw new IOException("Something happened ");
            return;
        }
        throw new IOException("Something happened ");
    }

    public void delete_rezervare(rezervare e)throws IOException
    {
        //repo_rezervari.remove(e.getId());
        Response res=send_request_receive_response(new Delete_Rezervare_Request(e));
        if(res instanceof EmptyResponse) {
            EmptyResponse exres = (EmptyResponse) res;
            if(exres.getState()!=Response_State.OK)
                throw new IOException("Something happened ");
            return;
        }
        throw new IOException("Something happened ");
    }

    public excursie get_excursie_by_id(Integer key)
    {
       //return repo_excursie.getByID(key);
        try {
            Response res = send_request_receive_response(new Get_Excursie_By_ID_Request(key));
            if (res instanceof Excursie_Response) {
                Excursie_Response exres = (Excursie_Response) res;
                return exres.getExcursie();
            }
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public rezervare get_rezervare_by_id(Integer key)
    {
        //return repo_rezervari.getByID(key);
        try {
            Response res = send_request_receive_response(new Get_Rezervare_By_ID_Request(key));
            if (res instanceof Rezervare_Response) {
                Rezervare_Response exres = (Rezervare_Response) res;
                return exres.getRezervare();
            }
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public void validate_user(app_user u) throws ExceptieValidare
    {
        try {
            Response response = send_request_receive_response(new User_login_request(u));
            if(response instanceof User_login_response)
            {
                if(((User_login_response) response).getState()== USER_LOGIN_STATE.NOT_EXISTS)
                    throw new ExceptieValidare("User-ul nu exista");
                if(((User_login_response) response).getState()==USER_LOGIN_STATE.WRONG_PASSWORD)
                    throw new ExceptieValidare("Parola e gresita");
            }
        }
        catch (IOException e)
        {
            throw new ExceptieValidare("Nu s-a putut valida");
        }

    }

    private void send_request(Request req) throws IOException
    {
        Object_Transmitter.send_object(req,socket);
    }

    private Response send_request_receive_response(Request req) throws IOException
    {
        Response res=null;
        Object_Transmitter.send_object(req,socket);
        Object response= null;
        try {
            response = responses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if(response instanceof Response)
            res=(Response)response;
        else
            res=null;
        return res;
    }

    public List<excursie> get_all_excursie()
    {
        //return repo_excursie.getAll();
        List<excursie> l=new ArrayList<>();
        try {
            Response res = send_request_receive_response(new Get_All_Excursie_Request());
            if (res instanceof ExcursieList_Response) {
                ExcursieList_Response exres = (ExcursieList_Response) res;
                return exres.getList();
            }
            return l;
        }
        catch (IOException e)
        {
            return l;
        }
    }

    public List<rezervare> get_all_rezervare()
    {
        //return repo_rezervari.getAll();
        List<rezervare> l=new ArrayList<>();
        try {
            Response res = send_request_receive_response(new Get_All_Rezervare_Request());
            if (res instanceof RezervareList_Response) {
                RezervareList_Response exres = (RezervareList_Response) res;
                return exres.getList();
            }
            return l;
        }
        catch (IOException e)
        {
            return l;
        }
    }

    public List<excursie> filtrare_excursii(Predicate<excursie> pred)
    {
        return get_all_excursie().stream().filter(pred).collect(Collectors.toList());
    }

    public List<rezervare> filtrare_rezervari(Predicate<rezervare> pred)
    {
        return get_all_rezervare().stream().filter(pred).collect(Collectors.toList());
    }

    public int get_locuri_libere(excursie e)
    {
        int ocupate = 0;
        List<rezervare> rez = get_all_rezervare();
        for (rezervare r : rez)
        {
            if (r.getId_excursie()==(e.getId()))
                ocupate += r.getNr_bilete();
        }
        return e.getTotal_locuri() - ocupate;
    }
}