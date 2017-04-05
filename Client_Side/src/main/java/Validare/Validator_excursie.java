package Validare;

import Common_Resources.domain.excursie;

/**
 * Created by Alex on 27.03.2017.
 */
public class Validator_excursie implements Validator<excursie> {
    public void validate(excursie e)
    {
        if (e.getH() < 0 || e.getH() > 23)  throw new ExceptieValidare("Ora incorecta");
        if (e.getM() < 0 || e.getM() > 59)  throw new ExceptieValidare("Minut incorect");
        if (e.getPret()<=0)                 throw new ExceptieValidare("Pret invalid");
        if (e.getTotal_locuri()<=0)         throw new ExceptieValidare("Numarul total de locuri nu poate fi 0 sau negativ");
    }
}
