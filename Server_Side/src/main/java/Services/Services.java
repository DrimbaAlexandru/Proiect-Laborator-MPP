package Services;

import Common_Resources.domain.*;
import Validare.*;
import repository.RepoExcursie.IRepoExcursie;
import repository.RepoExcursie.SQLRepoExcursie;
import repository.RepoRezervari.IRepoRezervari;
import repository.RepoRezervari.SQLRepoRezervari;
import repository.RepoUsers.IRepoUsers;
import repository.RepoUsers.SQLRepoUsers;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Alex on 27.03.2017.
 */
public class Services implements IServices
{
    private IRepoExcursie repo_excursie;
    private IRepoRezervari repo_rezervari;
    private IRepoUsers repo_useri;

    private Validator<excursie> val_excursie;
    private Validator<rezervare> val_rezervare;
    private Validator<app_user> val_user;

    public Services(Connection _con)
    {
        repo_excursie = new SQLRepoExcursie(_con);
        repo_rezervari = new SQLRepoRezervari(_con);
        repo_useri = new SQLRepoUsers(_con);

        val_excursie = new Validator_excursie();
        val_rezervare = new Validator_rezervare();
        val_user = new Validator_user();
    }

    public excursie add_excursie(excursie e) throws IOException
    {
        val_excursie.validate(e);
        return repo_excursie.add(e);
    }

    public rezervare add_rezervare(rezervare e) throws IOException
    {
        val_rezervare.validate(e);
        if (repo_excursie.getByID(e.getId_excursie()) == null)
        {
            throw new ExceptieValidare("ID-ul excursiei nu e corect");
        }
        if(get_locuri_libere(get_excursie_by_id(e.getId_excursie()))<e.getNr_bilete())
        {
            throw new ExceptieValidare("Nu mai exista suficiente locuri.");
        }
        return repo_rezervari.add(e);
    }

    public void update_excursie(excursie e)throws IOException
    {
        val_excursie.validate(e);
        repo_excursie.replace(e);
    }

    public void update_rezervare(rezervare e) throws IOException
    {
        val_rezervare.validate(e);
        if (repo_excursie.getByID((Integer) e.getId_excursie()) == null)
        {
            throw new ExceptieValidare("ID-ul excursiei nu e corect");
        }
        repo_rezervari.replace(e);
    }

    public void delete_excursie(excursie e)throws IOException
    {
        repo_excursie.remove(e.getId());
    }

    public void delete_rezervare(rezervare e)throws IOException
    {
        repo_rezervari.remove(e.getId());
    }

    public excursie get_excursie_by_id(Integer key)
    {
        return repo_excursie.getByID(key);
    }

    public rezervare get_rezervare_by_id(Integer key)
    {
        return repo_rezervari.getByID(key);
    }

    public void validate_user(app_user u) throws ExceptieValidare
    {
        app_user found = repo_useri.getByID(u.getId());
        if (found == null)
            throw new ExceptieValidare("User-ul nu exista");
        if (!(found.getPassword_hash()==(u.getPassword_hash())))
            throw new ExceptieValidare("Parola e gresita.");
    }

    public List<excursie> get_all_excursie() { return repo_excursie.getAll(); }
    public List<rezervare> get_all_rezervare() { return repo_rezervari.getAll(); }

    public List<excursie> filtrare_excursii(Predicate<excursie> pred)
    {
        return get_all_excursie().stream().filter(pred).collect(Collectors.toList());
    }

    public List<rezervare> filtrare_rezervari(Predicate<rezervare> pred)
    {
        return get_all_rezervare().stream().filter(pred).collect(Collectors.toList());
    }

    public int get_locuri_libere(excursie e)
    {
        int ocupate = 0;
        List<rezervare> rez = get_all_rezervare();
        for (rezervare r : rez)
        {
            if (r.getId_excursie()==(e.getId()))
                ocupate += r.getNr_bilete();
        }
        return e.getTotal_locuri() - ocupate;
    }
}