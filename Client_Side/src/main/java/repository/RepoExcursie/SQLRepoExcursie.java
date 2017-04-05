package repository.RepoExcursie;

import Common_Resources.domain.excursie;
import repository.ExtendedCrudRepository;
import repository.SQLRepository.AbstractSQLRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alex on 13.03.2017.
 */
public class SQLRepoExcursie extends AbstractSQLRepository<excursie,Integer> implements IRepoExcursie{

    public SQLRepoExcursie(Connection con)
    {
        super
        (con, "Excursie", true, new String[]{"idE", "ora_plecare", "minut_plecare", "pret", "locuri", "obiectiv_turistic", "firma_transport"},
                (ResultSet rs) -> {
                    try {
                        return new excursie(rs.getInt("idE"),rs.getString("obiectiv_turistic"),rs.getString("firma_transport"),
                                rs.getInt("ora_plecare"),rs.getInt("minut_plecare"),rs.getFloat("pret"),rs.getInt("locuri"));
                    }
                    catch (SQLException e) {
                        return null;
                    }
                },
                (stmt, e) ->  {
                    try{
                        stmt.setInt(1,e.getH());
                        stmt.setInt(2,e.getM());
                        stmt.setFloat(3,e.getPret());
                        stmt.setInt(4,e.getTotal_locuri());
                        stmt.setString(5,e.getOb_turistic());
                        stmt.setString(6,e.getFirma_transport());
                    }
                    catch (SQLException e1) {;}
                    return stmt;
                },
                (stmt, e) ->  {
                    try{
                        stmt.setInt(1,e.getH());
                        stmt.setInt(2,e.getM());
                        stmt.setFloat(3,e.getPret());
                        stmt.setInt(4,e.getTotal_locuri());
                        stmt.setString(5,e.getOb_turistic());
                        stmt.setString(6,e.getFirma_transport());
                        stmt.setInt(7,e.getId());
                    }
                    catch (SQLException e1) {;}
                    return stmt;
                },
                (key) ->{return key.toString();
                }
        );
    }
}
