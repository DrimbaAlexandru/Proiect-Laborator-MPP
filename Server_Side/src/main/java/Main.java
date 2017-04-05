
import Server_stuff.Server_Stuff;
import Services.*;
import repository.SQLRepository.SQLite_Connection_Factory;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by Alex on 12.03.2017.
 */

public class Main {


    public static void main(String[] args)
    {
        try{
            Properties props=new Properties();
            props.load(Main.class.getClassLoader().getResourceAsStream("properties_excursie.txt"));
            Connection con= SQLite_Connection_Factory.create_SQL_Connection(props);
            Services srv=new Services(con);
            Server_Stuff server=new Server_Stuff(srv,50505);
            server.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
