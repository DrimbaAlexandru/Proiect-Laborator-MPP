package repository.RepoUsers;

import Common_Resources.domain.app_user;
import repository.ExtendedCrudRepository;
import repository.SQLRepository.AbstractSQLRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alex on 13.03.2017.
 */
public class SQLRepoUsers extends AbstractSQLRepository<app_user,String> implements IRepoUsers
{
    public SQLRepoUsers(Connection con)
    {
        super
            (con, "Users", false, new String[]{"username", "password_hash"},
                    (ResultSet rs) -> {
                        try {
                            return new app_user(rs.getString("username"),rs.getInt("password_hash"));
                        }
                        catch (SQLException e) {
                            return null;
                        }
                    },
                    (stmt, e) ->  {
                        try{
                            stmt.setString(1,e.getId());
                            stmt.setInt(2,e.getPassword_hash());
                        }
                        catch (SQLException e1) {;}
                        return stmt;
                    },
                    (stmt, e) ->  {
                        try{
                            stmt.setString(1,e.getId());
                            stmt.setInt(2,e.getPassword_hash());
                        }
                        catch (SQLException e1) {;}
                        return stmt;
                    },
                    (key) ->{return "'"+key.toString()+"'";}
            );
    }
}
