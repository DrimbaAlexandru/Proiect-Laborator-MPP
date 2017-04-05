package Common_Resources.domain;

import java.io.Serializable;

/**
 * Created by Alex on 08.03.2017.
 */
public class excursie extends HasID<Integer> implements Serializable {
    private String ob_turistic;
    private String firma_transport;
    private int h,m;
    private float pret;
    private int total_locuri;

    public excursie(int ID,String ob_tur,String firma,int ora_plecare, int minut_plecare, float _pret, int locuri)
    {
        super(ID);
        ob_turistic =ob_tur;
        firma_transport =firma;
        h=ora_plecare;
        m=minut_plecare;
        pret=_pret;
        total_locuri=locuri;
    }

    public void setH(int h) {
        this.h = h;
    }
    public void setFirma_transport(String firma_transport) {
        this.firma_transport = firma_transport;
    }
    public void setOb_turistic(String ob_turistic) {
        this.ob_turistic = ob_turistic;
    }
    public void setM(int m) {
        this.m = m;
    }
    public void setPret(float pret) {
        this.pret = pret;
    }
    public void setTotal_locuri(int total_locuri) {
        this.total_locuri = total_locuri;
    }

    public float getPret() {
        return pret;
    }
    public int getH() {
        return h;
    }
    public int getM() {
        return m;
    }
    public String getFirma_transport() {
        return firma_transport;
    }
    public String getOb_turistic() {
        return ob_turistic;
    }
    public int getTotal_locuri() {
        return total_locuri;
    }
}
