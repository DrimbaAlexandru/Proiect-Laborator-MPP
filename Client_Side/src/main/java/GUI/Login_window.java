package GUI;

import Services.*;
import Validare.ExceptieValidare;
import Common_Resources.domain.app_user;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Alex on 27.03.2017.
 */
public class Login_window {
    private IServices srv;
    @FXML
    TextField txt_username;
    @FXML
    PasswordField txt_password;
    @FXML
    Button btn_login;

    Stage localStage=new Stage();

    private Pane mainLayout;

    public Login_window(IServices _srv) throws IOException
    {
        srv=_srv;
        FXMLLoader l=new FXMLLoader();
        l.setController(this);
        l.setLocation(getClass().getClassLoader().getResource("fxml_login.fxml"));
        mainLayout=l.load();
        btn_login.setOnMouseClicked((event -> {handel_btn_login();}));

    }

    public void run()
    {

        Group localroot=new Group();
        Scene localScene=new Scene(localroot, 250,200, Color.color(1,1,1));
        localStage.setResizable(false);
        localStage.setScene(localScene);
        localroot.getChildren().add(mainLayout);
        localStage.show();
    }

    @FXML
    private void handel_btn_login()
    {
        app_user u=new app_user(txt_username.getText(),txt_password.getText());
        System.out.println(u.getId());
        try
        {
            srv.validate_user(u);
            Main_window mw=new Main_window(srv);
            mw.run();
            localStage.close();
        }
        catch (Exception e)
        {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message!");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
