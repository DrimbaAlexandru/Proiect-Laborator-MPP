package Validare;


import Common_Resources.domain.rezervare;

/**
 * Created by Alex on 27.03.2017.
 */
public class Validator_rezervare implements Validator<rezervare>
{
    public void validate(rezervare e)
    {
        if (e.getNr_bilete() <= 0) throw new ExceptieValidare("Numarul de bilete trebuie sa fie pozitiv");
        if (e.getNume_client().length() == 0) throw new ExceptieValidare("Numele nu poate fi vid");
        if (e.getTelefon().length() == 0) throw new ExceptieValidare("Nr. de telefon nu poate fi vid");
    }
}