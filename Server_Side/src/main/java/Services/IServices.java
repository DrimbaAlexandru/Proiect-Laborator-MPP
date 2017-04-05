package Services;

import Validare.ExceptieValidare;
import Common_Resources.domain.app_user;
import Common_Resources.domain.excursie;
import Common_Resources.domain.rezervare;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Alex on 28.03.2017.
 */
public interface IServices {
    excursie add_excursie(excursie e) throws IOException;
    rezervare add_rezervare(rezervare e) throws IOException;
    void update_excursie(excursie e)throws IOException;
    void update_rezervare(rezervare e) throws IOException;
    void delete_excursie(excursie e)throws IOException;
    void delete_rezervare(rezervare e)throws IOException;
    excursie get_excursie_by_id(Integer key);
    rezervare get_rezervare_by_id(Integer key);
    void validate_user(app_user u) throws ExceptieValidare;
    List<excursie> get_all_excursie();
    List<rezervare> get_all_rezervare();
    List<excursie> filtrare_excursii(Predicate<excursie> pred);
    List<rezervare> filtrare_rezervari(Predicate<rezervare> pred);
    int get_locuri_libere(excursie e);
}
