package main.handlers;

import main.exceptions.InvalidRequestException;
import main.exceptions.NoContentException;
import main.exceptions.NotCreatedException;
import main.models.Genre;
import main.models.Song;
import main.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongHandler
{

    public static List<Song> getSongs(User user, boolean flag) throws SQLException, ClassNotFoundException
    {
        List<Song> songs;
        String query = "SELECT * FROM songs WHERE userID = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,user.getUserID());
        ResultSet resultSet = preparedStatement.executeQuery();
        songs = getSongs(resultSet,user.getUserID(),user.getUsername(),flag); //throws Exception internally if songs = null
        return songs;
    }
    public static Song getSong(User user,String songname) throws SQLException, ClassNotFoundException
    {
        Song song = null;
        String query = "SELECT * FROM songs WHERE userID = ? AND songname = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,user.getUserID());
        preparedStatement.setString(2,songname);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
        {
            String artist_name = resultSet.getString("artist_name");
            String song_genre = resultSet.getString("song_genre");
            String song_url = resultSet.getString("song_url");
            int views = resultSet.getInt("views");
            boolean  liked = resultSet.getBoolean("liked");
            song = new Song(songname,artist_name,song_genre,song_url,user.getUserID(),views,liked);
        }
        if(song == null)
            throw new NoContentException("No song "+songname+" found for user "+user.getUsername());
        return song;

    }
    public static Song createSong(Song song) throws SQLException, ClassNotFoundException
    {
        //song is authenticated
        validateSongObject(song); //checks if song object is valid throws exception internally
        try
        {
            System.out.println(song.getItem_name());
            String query = "INSERT INTO songs (songname, artist_name, song_genre, song_url,userID) VALUES (?,?,?,?,?);";
            PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, song.getItem_name());
            preparedStatement.setString(2, song.getArtist_name());
            preparedStatement.setString(3, song.getSong_genre());
            preparedStatement.setString(4, song.getSong_url());
            preparedStatement.setInt(5, song.getUserID());
            preparedStatement.execute();
            System.out.println("song created");
            return song;
        } catch (SQLException e)
        {
            if (e.getErrorCode() == 1062)
                throw new NotCreatedException("Not Created Song Already Exists");
            else
                throw e;
        }
    }



    public static void deleteSong(User user,String songname) throws SQLException, ClassNotFoundException
    {
        String query = "DELETE FROM songs WHERE userID = ? AND songname = ?";
        PreparedStatement preparedStatement =  ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,user.getUserID());
        preparedStatement.setString(2,songname);
        int flag =  preparedStatement.executeUpdate();
        if(flag <= 0)
            throw new NoContentException("No Song "+songname+" Deleted For User "+user.getUsername());
        query = "DELETE FROM playlist_song WHERE  userID = ? AND songname = ?";
        preparedStatement  = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,user.getUserID());
        preparedStatement.setString(2,songname);
        preparedStatement.execute();
        BlobHandler.deleteSong(user,songname); //delete song from cloud storage throws exceptions internally
    }
    public static List<Song> get_justadded_song(User user,int offset,int range) throws SQLException, ClassNotFoundException
    {
        System.out.println("offset = "+offset);
        System.out.println("Range = "+range);
        List<Song> songs;
        String query;
        ResultSet resultSet;
        if(offset ==0 && range == 0)
        {
            query = "SELECT * FROM songs WHERE userID = ? ORDER BY p_date DESC";
            resultSet = getResultset(query,user.getUserID());
            songs = getSongs(resultSet,user.getUserID(),user.getUsername(),true); //throws Exception internally if songs = null
        }
        else if(offset >=0 && range >0)
        {
            query = "SELECT * FROM songs WHERE userID = ? ORDER BY p_date DESC limit ?,?";
            resultSet = getResultset(query,user.getUserID(),offset,range);
            songs = getSongs(resultSet,user.getUserID(),user.getUsername(),true); //throws Exception internally if songs = null
        }
        else
            throw new InvalidRequestException("Invalid Request Provide Proper Offset and Range");
        return songs;
    }
    public static List<Song> get_liked_songs(User user,int offset,int range) throws SQLException, ClassNotFoundException
    {
        List<Song> songs;
        ResultSet resultSet ;
        String query;
        if(offset == 0  && range ==0)
        {
            query = "SELECT * FROM songs WHERE userID = ? AND liked = TRUE ORDER BY songname";
            resultSet = getResultset(query,user.getUserID());
            songs = getSongs(resultSet,user.getUserID(), user.getUsername(),true); //throws Exception internally if songs = null
        }
        else if(offset >=0 && range > 0)
        {
            query = "SELECT * FROM songs WHERE userID = ? AND liked = TRUE ORDER BY songname DESC limit ?,?";
            resultSet = getResultset(query,user.getUserID(),offset,range);
            songs = getSongs(resultSet,user.getUserID(),user.getUsername(),true); //throws Exception internally if songs = null
        }
        else
            throw new InvalidRequestException("Invalid Request Provide Proper Offset and Range");
        return songs;
    }
    public static List<Song> get_mostviews_songs(User user,int offset,int range) throws SQLException, ClassNotFoundException
    {
        List<Song> songs;
        String query;
        ResultSet resultSet;
        if(offset == 0 && range == 0)
        {
            query = "SELECT * FROM songs WHERE userID = ? ORDER BY VIEWS DESC";
            resultSet = getResultset(query,user.getUserID());
            songs = getSongs(resultSet,user.getUserID(),user.getUsername(),true); //throws Exception internally if songs = null
        }
        else if(offset >=0 && range > 0)
        {
            query = "SELECT * FROM songs WHERE userID = ? ORDER BY views DESC limit ?,?";
            resultSet = getResultset(query,user.getUserID(),offset,range);
            songs = getSongs(resultSet,user.getUserID(),user.getUsername(),true); //throws Exception internally if songs = null
        }
        else
            throw new InvalidRequestException("Invalid Request Provide Proper Offset and Range");
        return songs;
    }
    public static List<Genre> getGenres(User user,int offset, int range) throws SQLException, ClassNotFoundException
    {
        List<Genre> genres = new ArrayList<>() ;
        String query;
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        if(offset ==0 && range == 0)
        {
            query ="SELECT song_genre,COUNT(songname) AS songs FROM songs WHERE userID =? GROUP  BY song_genre ORDER BY song_genre DESC";
            preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,user.getUserID());
        }
        else if(offset >= 0 && range > 0)
        {
            query = "SELECT song_genre,COUNT(songname) AS songs FROM songs WHERE userID =? GROUP  BY song_genre ORDER BY song_genre DESC limit ?,?";
            preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,user.getUserID());
            preparedStatement.setInt(2,offset);
            preparedStatement.setInt(3,range);
        }
        else
            throw new InvalidRequestException("Invalid Request Provide Proper Offset And Range");

        resultSet =  preparedStatement.executeQuery();
        while (resultSet.next())
        {
            String genre = resultSet.getString("song_genre");
            int songs = resultSet.getInt("songs");
            genres.add(new Genre(genre,songs));
        }
        if (genres.size() == 0)
            throw new NoContentException("No Genres Found");
        return genres;
    }
    public static void update_view(User user,String songname) throws SQLException, ClassNotFoundException
    {
        String query = "UPDATE songs SET views = views +1 WHERE  songname = ? and userID = ? ";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setString(1,songname);
        preparedStatement.setInt(2,user.getUserID());
         if(preparedStatement.executeUpdate() ==0)
             throw new NoContentException("No Song Found");
    }
    public static void update_like(User user,String songname) throws SQLException, ClassNotFoundException
    {
        String query = "SELECT  * FROM songs WHERE userID = ? and songname = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,user.getUserID());
        preparedStatement.setString(2,songname);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            boolean like = resultSet.getBoolean("liked");
            query = "UPDATE songs SET  liked = ? WHERE userID = ? and songname = ?";
            preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
            preparedStatement.setBoolean(1,!like);
            preparedStatement.setInt(2,user.getUserID());
            preparedStatement.setString(3,songname);
            preparedStatement.executeUpdate();
        }
        else
            throw new NoContentException("No Song:"+songname+" Found For Username:"+user.getUsername());

    }
    public static void add_to_playlist(User user,String songname, String playlist_name) throws SQLException, ClassNotFoundException
    {
        if(checkSong(user.getUserID(),songname) && PlaylistHandler.checkPlaylist(user.getUserID(),playlist_name))
        {
            try
            {
                String query = "insert into playlist_song (songname, userID, playlist_name) values (?,?,?);";
                PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
                preparedStatement.setString(1,songname);
                preparedStatement.setInt(2,user.getUserID());
                preparedStatement.setString(3,playlist_name);
                preparedStatement.execute();
            }catch (SQLException e)
            {
                if(e.getErrorCode() == 1062)
                    throw new NotCreatedException("Not Created The Song Already Exists In Playlist");
            }
        }
        else
            throw new NoContentException("Song Or Playlist Does Not Exists");
    }
    private static List<Song> getSongs(ResultSet resultSet,int userID,String username,boolean flag) throws SQLException
    {
        List<Song> songs = new ArrayList<>();
        while (resultSet.next())
        {
            String songname = resultSet.getString("songname");
            String artist_name = resultSet.getString("artist_name");
            String song_genre = resultSet.getString("song_genre");
            String song_url = resultSet.getString("song_url");
            int views = resultSet.getInt("views");
            boolean  liked = resultSet.getBoolean("liked");
            songs.add(new Song(songname,artist_name,song_genre,song_url,userID,views,liked));
        }
        if(songs.size() == 0 && flag)
            throw new NoContentException("No songs Found for user "+username);
        return songs;
    }
    private static boolean checkSong(int userID, String songname) throws SQLException, ClassNotFoundException
    {
        String query = "SELECT * FROM songs WHERE userID = ? AND songname = ?";
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,userID);
        preparedStatement.setString(2,songname);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private static ResultSet getResultset(String query,int userID,int offset,int range) throws SQLException, ClassNotFoundException
    {
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,userID);
        preparedStatement.setInt(2,offset);
        preparedStatement.setInt(3,range);
        return preparedStatement.executeQuery();
    }
    private static ResultSet getResultset(String query,int userID) throws SQLException, ClassNotFoundException
    {
        PreparedStatement preparedStatement = ConnectionSingleton.getConnection().prepareStatement(query);
        preparedStatement.setInt(1,userID);
        return  preparedStatement.executeQuery();
    }
    private static void validateSongObject(Song song)
    {
        System.out.println("validating");
        boolean flag = true;

        try
        {
            if(song.getItem_name().length() >150)
                flag = false;
            if(song.getUserID() ==0)
                flag = false;
            if(song.getArtist_name().length() > 45)
                flag = false;
            if(song.getSong_genre().length() > 45)
                flag = false;
            if(song.getSong_url().length() >200)
                flag = false;
            if(!flag)
                throw new InvalidRequestException("Invalid Request Provide Proper Song Object {Item_name.length <= 150,userID != 0,artist_name.length <= 45,song_genre.length <=45,song_url.length <=200}");


        }catch (NullPointerException e)
        {
            throw new InvalidRequestException("Invalid Request Provide Proper Song Object {'item_name':'item_name','artist_name':'artist_name','song_url':'url','userID':userID}");
        }

    }
}
