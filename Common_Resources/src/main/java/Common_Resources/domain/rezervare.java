package Common_Resources.domain;

import java.io.Serializable;

/**
 * Created by Alex on 08.03.2017.
 */
public class rezervare extends HasID<Integer> implements Serializable {
    private String nume_client;
    private String telefon;
    private int id_excursie;
    private int nr_bilete;

    public rezervare(int ID, String client, String tel, int id_exc, int bilete)
    {
        super(ID);
        nume_client=client;
        telefon=tel;
        id_excursie=id_exc;
        nr_bilete=bilete;
    }

    public int getId_excursie() {
        return id_excursie;
    }
    public int getNr_bilete() {
        return nr_bilete;
    }
    public String getNume_client() {
        return nume_client;
    }
    public String getTelefon() {
        return telefon;
    }

    public void setNume_client(String nume_client) {
        this.nume_client = nume_client;
    }
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
