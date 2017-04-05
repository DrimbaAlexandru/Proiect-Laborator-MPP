package repository.SQLRepository;

import java.sql.*;
import java.util.Properties;

/**
 * Created by Alex on 08.03.2017.
 */
public class SQLite_Connection_Factory {

    public static Connection create_SQL_Connection(Properties props) throws SQLException,ClassNotFoundException
    {
        Connection connection;
        Class.forName(props.getProperty("className"));
        connection = DriverManager.getConnection(props.getProperty("conDetails"));
        return connection;
    }
}
