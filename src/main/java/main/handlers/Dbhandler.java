package main.handlers;



import main.models.Error;
import java.sql.*;


public class Dbhandler
{
    /*users table:
     CREATE TABLE users (
        userID INT KEY NOT NULL AUTO_INCREMENT,
        username VARCHAR(45) UNIQUE NOT NULL,
    password VARCHAR(45) NOT NULL);*/

    /* songs table :
    CREATE TABLE songs (
        songname varchar(150) NOT NULL,
    userID INT NOT NULL,
    artist_name varchar(45) NOT NULL,
    song_genre varchar(45) NOT NULL,
    song_url varchar(200) NOT NULL,
    pdate DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    views INT DEFAULT 0 NOT NULL,
    liked BIT(1) DEFAULT 0 NOT NULL,
    FOREIGN KEY (userID)
    REFERENCES users (userID),
    PRIMARY KEY (songname , userid));*/

    /* playlist table:
    CREATE TABLE playlist (
        playlist_name varchar(45) NOT NULL,
    userID INT NOT NULL,
    playlist_desc varchar(150) DEFAULT NULL,
    PRIMARY KEY (playlist_name , userID));
    FOREIGN KEY (userID) REFERENCES users(userID) */

   /*palylist_song table:
    CREATE TABLE playlist_song (
        songname varchar(150) NOT NULL,
    userID INT NOT NULL,
    playlist_name varchar(45) NOT NULL,
    PRIMARY KEY (songname , userID , playlist_name))
    FOREIGN KEY(songname) REFERENCES songs(songname)
    FOREIGN KEY(userID) REFERENCES users(user)
    FOREIGN KEY(playlist_name) REFERENCES playlist(playlist_name);*/


    public static void add_sql_exception(String url,Error error) throws SQLException, ClassNotFoundException
    {
        String query = "insert into sql_exceptions (url, error_msg, error_code, http_status) values (?,?,?,?);";
        preparestatment(query,url,error);
    }
    public static void add_authorization_exception(String url,Error error) throws SQLException, ClassNotFoundException
    {
        String query = "insert into authorization_exception (url, error_msg, error_code, http_status) values (?,?,?,?);";
        preparestatment(query,url,error);

    }
    public static void add_no_content_exception(String url,Error error) throws SQLException, ClassNotFoundException
    {
        String query = "insert into no_content_exceptions (url, error_msg, error_code, http_status) values (?,?,?,?);";
        preparestatment(query,url,error);
    }
    public static void add_storage_exception(String url,Error error) throws SQLException, ClassNotFoundException
    {
        String query = "insert into storage_exceptions(url,error_msg,error_code,http_status) values(?,?,?,?)";
        preparestatment(query,url,error);
    }
    public static void add_invalid_request_exception(String url,Error error) throws SQLException, ClassNotFoundException
    {
        String query = "insert into Invalid_request_exceptions(url,error_msg,error_code,http_status) values(?,?,?,?)";
        preparestatment(query,url,error);
    }
    public static void add_not_created_exception(String url,Error error) throws SQLException, ClassNotFoundException
    {
        String query = "insert into no_content_exceptions(url,error_msg,error_code,http_status) values(?,?,?,?)";
        preparestatment(query,url,error);
    }
    private static void preparestatment(String query,String url,Error error) throws SQLException, ClassNotFoundException
    {
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setString(1,url);
        preparedStatement.setString(2,error.getError_msg());
        preparedStatement.setInt(3,error.getError_code());
        preparedStatement.setInt(4,error.getStatus_code());
        preparedStatement.execute();
    }
}
