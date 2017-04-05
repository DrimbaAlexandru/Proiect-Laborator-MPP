package Validare;

/**
 * Created by Alex on 27.03.2017.
 */
public interface Validator<Elem> {
    void validate(Elem e) throws ExceptieValidare;
}
