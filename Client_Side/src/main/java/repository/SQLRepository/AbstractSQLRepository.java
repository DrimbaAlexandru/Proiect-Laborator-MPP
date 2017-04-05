package repository.SQLRepository;

import Common_Resources.domain.HasID;
import repository.ExtendedCrudRepository;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Alex on 12.03.2017.
 */
public class AbstractSQLRepository<Elem extends HasID<IdType>, IdType> implements ExtendedCrudRepository<Elem,IdType>
{
    private Connection con;
    private String sql_insert,sql_update,sql_delete,sql_select_by_keys,sql_select_all_keys;

    private String table_name;
    private boolean autoincrement;
    private String[] attributes;
    private Function<ResultSet,Elem> ResultSet2Object;
    private BiFunction<PreparedStatement,Elem,PreparedStatement> Prepare4Insert;
    private BiFunction<PreparedStatement,Elem,PreparedStatement> Prepare4Update;
    private Function<IdType,String> key_formatter;

    public AbstractSQLRepository
            (Connection _con, String _table_name, boolean _autoincrement, String[] _attributes,
             Function<ResultSet,Elem> _ResultSet2Object,BiFunction<PreparedStatement,Elem,PreparedStatement> _Prepare4Insert,
             BiFunction<PreparedStatement,Elem,PreparedStatement> _Prepare4Update,Function<IdType,String> _key_formatter) throws RuntimeException
    {
        con=_con;
        table_name=_table_name;
        autoincrement=_autoincrement;
        attributes=_attributes;
        ResultSet2Object=_ResultSet2Object;
        Prepare4Insert=_Prepare4Insert;
        Prepare4Update=_Prepare4Update;
        key_formatter=_key_formatter;

        String ins,upd,del,sel_by_keys,sel_all_keys;
        int i;

        ins="Insert into "+table_name+" (";

        if(autoincrement) i=1;else i=0;
        for(;i<attributes.length-1;i++)
            ins+=attributes[i]+",";
        ins+=attributes[attributes.length-1]+") values(";
        if(autoincrement) i=1;else i=0;
        for(;i<attributes.length-1;i++)
            ins+="?,";
        ins+="?)";

        upd="Update "+table_name+" set ";

        for(i=1;i<attributes.length-1;i++)
            upd+=attributes[i]+"=?,";
        upd+=attributes[attributes.length-1]+"=? where "+attributes[0]+"=?";

        del="Delete from "+table_name+" where "+attributes[0]+"=?";
        sel_by_keys="Select * from "+table_name+" where "+attributes[0]+" in ";
        sel_all_keys="Select "+attributes[0]+ " from "+table_name;

        sql_insert=ins;
        sql_update=upd;
        sql_delete=del;
        sql_select_by_keys=sel_by_keys;
        sql_select_all_keys=sel_all_keys;
    }

    @Override
    public Elem add(Elem e) throws IOException {
        PreparedStatement stmt=null;
        ResultSet rs=null;

        try{
            stmt=con.prepareStatement(sql_insert, Statement.RETURN_GENERATED_KEYS);

            stmt=Prepare4Insert.apply(stmt,e);
            stmt.executeUpdate();
            rs=stmt.getGeneratedKeys();
            rs.next();
            e.setId((IdType) rs.getObject(1));
        }
        catch (SQLException ex)
        {
            throw new IOException(ex.getMessage());
        }
        finally {
            try {
                if(stmt!=null)
                    stmt.close();
                if(rs!=null)
                    rs.close();
            } catch (SQLException e1) {
                throw new IOException(e1.getMessage());
            }

        }
        return e;
    }

    @Override
    public void replace(Elem e) throws IOException {

        PreparedStatement stmt=null;
        try{
            stmt=con.prepareStatement(sql_update);
            stmt=Prepare4Update.apply(stmt,e);
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new IOException(ex.getMessage());
        }
        finally {
            try {
                if(stmt!=null)
                    stmt.close();
            } catch (SQLException e1) {
                throw new IOException(e1.getMessage());
            }

        }
    }

    @Override
    public void remove(IdType key) throws IOException {
        PreparedStatement stmt=null;
        try{
            stmt=con.prepareStatement(sql_delete);
            stmt.setObject(1,key);
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new IOException(ex.getMessage());
        }
        finally {
            try {
                if(stmt!=null)
                    stmt.close();
            } catch (SQLException e1) {
                throw new IOException(e1.getMessage());
            }
        }
    }

    @Override
    public Elem getByID(IdType key) {
        List<IdType> keys=new ArrayList<IdType>();
        keys.add(key);
        try {
            return getByID(keys).get(0);
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    @Override
    public List<IdType> get_All_keys() {
        PreparedStatement stmt=null;
        ResultSet rs=null;
        List<IdType> keys=new ArrayList<>();

        try{
            stmt=con.prepareStatement(sql_select_all_keys);
            rs=stmt.executeQuery();
            while(rs.next())
                keys.add((IdType) rs.getObject(attributes[0]));
        }
        catch (SQLException ex)
        {
            ;
        }
        finally {
            try {
                if(stmt!=null)
                    stmt.close();
                if(rs!=null)
                    rs.close();
            } catch (SQLException e1) {
                ;
            }

        }
        return keys;
    }

    @Override
    public List<Elem> getByID(List<IdType> keys) {
        Statement stmt=null;
        ResultSet rs=null;
        List<Elem> result=new ArrayList<>();
        if(keys.size()==0)
            return result;
        Elem e;

        try{
            String query=sql_select_by_keys+"(";
            int i;
            for(i=0;i<keys.size()-1;i++)
                query=query+key_formatter.apply(keys.get(i))+",";
            query=query+key_formatter.apply(keys.get(keys.size()-1))+")";

            stmt=con.createStatement();
            rs=stmt.executeQuery(query);
            while(rs.next())
            {
                e=ResultSet2Object.apply(rs);
                result.add(e);
            }
        }
        catch (SQLException ex)
        {
            ;
        }
        finally {
            try {
                if(stmt!=null)
                    stmt.close();
                if(rs!=null)
                    rs.close();
            } catch (SQLException e1) {
                ;
            }

        }
        return result;
    }
}
