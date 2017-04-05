package Validare;

import Common_Resources.domain.app_user;

/**
 * Created by Alex on 27.03.2017.
 */
public class Validator_user implements Validator<app_user>
{
    public void validate(app_user e)
    {
        if (e.getId().length() < 3) throw new ExceptieValidare("Lungimea user-ului trebuie sa fie de minim 3 caractere");
    }
}
