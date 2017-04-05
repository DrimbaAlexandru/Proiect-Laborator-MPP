package GUI;

import Services.*;
import Validare.ExceptieValidare;
import Common_Resources.domain.app_user;
import Common_Resources.domain.excursie;
import Common_Resources.domain.rezervare;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alex on 27.03.2017.
 */
public class Main_window {
    private IServices srv;

    @FXML
    Tab tab_excursii,tab_rezervari;
    @FXML
    TableView tabel_excursii,tabel_excursii_obiectiv,tabel_rezervari;
    @FXML
    TextField txt_nume_obiectiv,txt_nume_client,txt_nr_telefon;
    @FXML
    Spinner nr_h_start,nr_m_start,nr_h_end,nr_m_end,nr_nr_locuri;
    @FXML
    Button btn_cauta_excursii,btn_rezerva,btn_refresh;
    @FXML
    TabPane tab_pane;

    private ObservableList<excursie> lista_excursii = FXCollections.observableArrayList();
    private ObservableList<excursie> lista_excursii_obiectiv = FXCollections.observableArrayList();
    private ObservableList<rezervare> lista_rezervari = FXCollections.observableArrayList();

    private Pane mainLayout;

    public Main_window(IServices _srv) throws IOException
    {
        Updater u=new Updater((Socket_Services)_srv,this);
        u.start();

        srv=_srv;
        FXMLLoader l=new FXMLLoader();
        l.setController(this);
        l.setLocation(getClass().getClassLoader().getResource("fxml_main.fxml"));
        mainLayout=l.load();

        nr_h_start.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        nr_m_start.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        nr_h_end.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        nr_m_end.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        nr_nr_locuri.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100));

        tabel_excursii.setItems(lista_excursii);
        tabel_excursii_obiectiv.setItems(lista_excursii_obiectiv);
        tabel_rezervari.setItems(lista_rezervari);

        TableColumn<excursie,String> col_ID=new TableColumn<>("ID");
        TableColumn<excursie,String> col_obiectiv=new TableColumn<>("Obiectiv turistic");
        TableColumn<excursie,String> col_firma=new TableColumn<>("Firma transport");
        TableColumn<excursie,String> col_ora=new TableColumn<>("Ora plecare");
        TableColumn<excursie,String> col_pret=new TableColumn<>("Pret");
        TableColumn<excursie,String> col_locuri=new TableColumn<>("Locuri disponibile");

        col_ID.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(param.getValue().getId().toString());});
        col_obiectiv.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(param.getValue().getOb_turistic());});
        col_firma.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(param.getValue().getFirma_transport());});
        col_ora.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(to_HM_time(param.getValue().getH(),param.getValue().getM()));});
        col_pret.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(Float.toString(param.getValue().getPret()));});
        col_locuri.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(Integer.toString(srv.get_locuri_libere(param.getValue())));});

        tabel_excursii.getColumns().addAll(col_ID,col_obiectiv,col_firma,col_ora,col_pret,col_locuri);

        TableColumn<excursie,String> col_ID2=new TableColumn<>("ID");
        TableColumn<excursie,String> col_firma2=new TableColumn<>("Firma transport");
        TableColumn<excursie,String> col_ora2=new TableColumn<>("Ora plecare");
        TableColumn<excursie,String> col_pret2=new TableColumn<>("Pret");
        TableColumn<excursie,String> col_locuri2=new TableColumn<>("Locuri disponibile");

        col_ID2.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(param.getValue().getId().toString());});
        col_firma2.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(param.getValue().getFirma_transport());});
        col_ora2.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(to_HM_time(param.getValue().getH(),param.getValue().getM()));});
        col_pret2.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(Float.toString(param.getValue().getPret()));});
        col_locuri2.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(Integer.toString(srv.get_locuri_libere(param.getValue())));});

        tabel_excursii_obiectiv.getColumns().addAll(col_ID2,col_firma2,col_ora2,col_pret2,col_locuri2);

        TableColumn<rezervare,String> col_nume_client=new TableColumn<>("Nume client");
        TableColumn<rezervare,String> col_nr_tel=new TableColumn<>("Nr telefon");
        TableColumn<rezervare,String> col_id_exc=new TableColumn<>("ID excursie");
        TableColumn<rezervare,String> col_nr_locuri_rezervate=new TableColumn<>("Nr locuri");

        col_nume_client.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(param.getValue().getNume_client());});
        col_nr_tel.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(param.getValue().getTelefon());});
        col_id_exc.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(Integer.toString(param.getValue().getId_excursie()));});
        col_nr_locuri_rezervate.setCellValueFactory((param)->
        {return new ReadOnlyObjectWrapper<String>(Integer.toString(param.getValue().getNr_bilete()));});

        tabel_rezervari.getColumns().addAll(col_nume_client,col_nr_tel,col_id_exc,col_nr_locuri_rezervate);

        btn_cauta_excursii.setOnMouseClicked((e)->{handel_cauta_excursii();});
        btn_refresh.setOnMouseClicked((event -> {refresh_view();}));
        btn_rezerva.setOnMouseClicked((e)->{handel_rezerva_bilete();});
    }

    public void run()
    {
        Stage localStage=new Stage();
        Group localroot=new Group();
        Scene localScene=new Scene(localroot, 834,474, Color.color(1,1,1));
        localStage.setResizable(false);
        localStage.setScene(localScene);
        localroot.getChildren().add(mainLayout);
        localStage.show();
        refresh_view();
    }

    private String to_HM_time(Integer H, Integer M)
    {
        String res="";
        if (H > 9)
            res += H.toString();
        else res += "0"+H.toString();
        res+=":";
        if (M > 9)
            res += M.toString();
        else res += "0" + M.toString();
        return res;
    }

    private boolean in_time(int h, int m, int hmin, int mmin, int hmax, int mmax)
    {
        boolean peste=false, sub=false;
        if (h == hmin)
            if (m >= mmin)
                peste = true;
            else ;
        else if (h > hmin) peste = true;
        if (h == hmax)
            if (m <= mmax)
                sub = true;
            else ;
        else if (h < hmax) sub = true;
        return peste && sub;
    }

    private void populate_excursii_table(List<excursie> l)
    {
        lista_excursii.clear();
        for(excursie e : l)
        {
            lista_excursii.add(e);
        }
    }

    private void populate_excursii_obiectiv_table(List<excursie> l)
    {
        lista_excursii_obiectiv.clear();
        for(excursie e : l)
        {
            lista_excursii_obiectiv.add(e);
        }
    }

    private void populate_rezervari_table(List<rezervare> l)
    {
        lista_rezervari.clear();
        for(rezervare e : l)
        {
            lista_rezervari.add(e);
        }
    }

    public void refresh_view()
    {
        populate_excursii_table(srv.get_all_excursie());
        populate_rezervari_table(srv.get_all_rezervare());
        populate_excursii_obiectiv_table(srv.filtrare_excursii((exc) -> { return exc.getOb_turistic().contains(txt_nume_obiectiv.getText()) && in_time(exc.getH(), exc.getM(), (Integer) nr_h_start.getValue(), (Integer)nr_m_start.getValue(), (Integer)nr_h_end.getValue(),(Integer)nr_m_end.getValue()); }));
    }

    private void handel_cauta_excursii()
    {
        try
        {
            refresh_view();
            tab_pane.getSelectionModel().select(tab_rezervari);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message!");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void handel_rezerva_bilete()
    {
        try
        {
            int sel = tabel_excursii_obiectiv.getSelectionModel().getSelectedIndex();
            if (sel < 0)
                throw new Exception("Selectie gresita");
            rezervare r = new rezervare(0, txt_nume_client.getText(), txt_nr_telefon.getText(), lista_excursii_obiectiv.get(sel).getId(), (int)nr_nr_locuri.getValue());
            srv.add_rezervare(r);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message!");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
}
