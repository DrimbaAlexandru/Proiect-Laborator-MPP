import GUI.Login_window;
import Services.*;
import javafx.application.Application;
import javafx.stage.Stage;
import repository.SQLRepository.SQLite_Connection_Factory;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by Alex on 12.03.2017.
 */

public class CMain extends Application {

    public void start(Stage stage)
    {
        try{
            IServices srv=new Socket_Services();
            Login_window lw=new Login_window(srv);
            lw.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}
