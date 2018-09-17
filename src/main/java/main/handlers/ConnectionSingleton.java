package main.handlers;

import com.google.appengine.api.utils.SystemProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton
{
    private static Connection connection;
    private ConnectionSingleton()
    {

    }
    public static Connection getConnection() throws SQLException, ClassNotFoundException
    {
        String url;
        if(connection == null)
        {

            if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production)
            {
                url = System.getProperty("cloudsql");
                connection = DriverManager.getConnection(url);

            } else
            {
                url = System.getProperty("cloudsql-local");
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url);
            }
        }
        return connection;
    }


}
