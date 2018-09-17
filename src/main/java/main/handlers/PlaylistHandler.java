package main.handlers;

import main.exceptions.InvalidRequestException;
import main.exceptions.NoContentException;
import main.exceptions.NotCreatedException;
import main.models.Playlist;
import main.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistHandler
{
    public static List<Playlist> getPlaylists(User user, int offset, int range) throws SQLException, ClassNotFoundException
    {
        List<Playlist> playlists = new ArrayList<>();
        String query;
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        if(offset >= 0 && range >0)
        {
            query  = "SELECT * FROM playlist WHERE userID = ? ORDER BY playlist_name DESC limit ?,?";
            preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,user.getUserID());
            preparedStatement.setInt(2,offset);
            preparedStatement.setInt(3,range);
            resultSet = preparedStatement.executeQuery();
        }
        else if( offset ==0 && range == 0)
        {
            query = "SELECT * FROM playlist WHERE userID = ? ORDER BY playlist_name DESC";
            preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,user.getUserID());
            resultSet = preparedStatement.executeQuery();
        }
        else
            throw new InvalidRequestException("Invalid Request Provide Proper Offset And Range");
        while (resultSet.next())
        {
            String playlist_name = resultSet.getString("playlist_name");
            String playlist_desc = resultSet.getString("playlist_desc");
            playlists.add(new Playlist(playlist_name,user.getUserID(),playlist_desc));
        }
        if(playlists.size() ==0)
            throw new NoContentException("No playlist Found For Username:"+user.getUsername());
        return playlists;
    }
    public static Playlist createPlaylist(Playlist playlist) throws SQLException, ClassNotFoundException
    {
        //playlist is authenticated
        validatePlaylist(playlist); //validating playlist object throws exceptions internally
        try
        {
            String query = "INSERT INTO playlist VALUES (?,?,?)";
            PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, playlist.getItem_name());
            preparedStatement.setInt(2, playlist.getUserID());
            preparedStatement.setString(3, playlist.getPlaylist_desc());
            preparedStatement.execute();
            return playlist;
        } catch (SQLException e)
        {
            if (e.getErrorCode() == 1062)
                throw new NotCreatedException("Not Created The Playlist Already Exists");
            throw e;
        }
    }

    public static void delete_playlist(User user,String playlistname) throws SQLException, ClassNotFoundException
    {
        int x;
        String query = "DELETE FROM playlist WHERE userID = ? and playlist_name = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,user.getUserID());
        preparedStatement.setString(2,playlistname);
        x = preparedStatement.executeUpdate();
        if(x ==0)
            throw new NoContentException("No Playlist Found");
        query = "DELETE FROM playlist_song WHERE playlist_name = ? and userID = ?";
        preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setString(1,playlistname);
        preparedStatement.setInt(2,user.getUserID());
        preparedStatement.executeUpdate();
    }
    static boolean checkPlaylist(int userID, String playlist_name) throws SQLException, ClassNotFoundException
    {
        String query = "SELECT * FROM playlist WHERE userID = ? AND playlist_name = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,userID);
        preparedStatement.setString(2,playlist_name);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
    private static void validatePlaylist(Playlist playlist)
    {
        try
        {
            boolean flag = true;
            if(playlist.getItem_name().length() > 45)
                flag = false;
            if(playlist.getPlaylist_desc() !=null && playlist.getPlaylist_desc().length() > 150)
                flag = false;
            if(playlist.getUserID() == 0)
                flag = false;
            if(!flag)
                throw new InvalidRequestException("Invalid Request Provide Proper Playlist Object {playlist_name.length <=45,userID != 0, playlist_desc.length <=150}");
        }catch (NullPointerException e)
        {
            throw new InvalidRequestException("Invalid Request Provide Proper Playlist Object {playlist_name:'name',userID:id,(optional)playlist_desc:'desc'}");
        }
    }

}
