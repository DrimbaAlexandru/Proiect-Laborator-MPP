package repository.RepoRezervari;


import Common_Resources.domain.rezervare;

import repository.ExtendedCrudRepository;
import repository.SQLRepository.AbstractSQLRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alex on 13.03.2017.
 */
public class SQLRepoRezervari extends AbstractSQLRepository<rezervare,Integer> implements IRepoRezervari{
    public SQLRepoRezervari(Connection con)
    {
        super
            (con, "Rezervari", true, new String[]{"ID", "nume_client", "Telefon", "ID_exc", "nr_bilete"},
                    (ResultSet rs) -> {
                        try {
                            return new rezervare(rs.getInt("ID"),rs.getString("nume_client"),rs.getString("Telefon"),rs.getInt("ID_exc"),rs.getInt("nr_bilete"));
                        }
                        catch (SQLException e) {
                            return null;
                        }
                    },
                    (stmt, e) ->  {
                        try{
                            stmt.setString(1,e.getNume_client());
                            stmt.setString(2,e.getTelefon());
                            stmt.setInt(3,e.getId_excursie());
                            stmt.setInt(4,e.getNr_bilete());
                        }
                        catch (SQLException e1) {;}
                        return stmt;
                    },
                    (stmt, e) ->  {
                        try{
                            stmt.setString(1,e.getNume_client());
                            stmt.setString(2,e.getTelefon());
                            stmt.setInt(3,e.getId_excursie());
                            stmt.setInt(4,e.getNr_bilete());
                            stmt.setInt(5,e.getId());
                        }
                        catch (SQLException e1) {;}
                        return stmt;
                    },
                    (key) ->{return key.toString();}
            );
    }
}
