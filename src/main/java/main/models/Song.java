package main.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Song extends Item
{
    private String artist_name;
    private String song_genre;
    private String song_url;
    private int userID;
    private int views;
    private boolean liked;

    public Song()
    {
    }

    public Song(String item_name, String artist_name, String song_genre, String song_url, int userID, int views, boolean liked)
    {
        super(item_name);
        this.artist_name = artist_name;
        this.song_genre = song_genre;
        this.song_url = song_url;
        this.userID = userID;
        this.views = views;
        this.liked = liked;
    }

    @Override
    public String toString()
    {
        return "Song{" +
                "artist_name='" + artist_name + '\'' +
                ", song_genre='" + song_genre + '\'' +
                ", song_url='" + song_url + '\'' +
                ", userID=" + userID +
                ", views=" + views +
                ", liked=" + liked +
                ", item_name='" + item_name + '\'' +
                '}';
    }

    public String getArtist_name()
    {
        return artist_name;
    }

    public void setArtist_name(String artist_name)
    {
        this.artist_name = artist_name;
    }

    public String getSong_genre()
    {
        return song_genre;
    }

    public void setSong_genre(String song_genre)
    {
        this.song_genre = song_genre;
    }

    public String getSong_url()
    {
        return song_url;
    }

    public void setSong_url(String song_url)
    {
        this.song_url = song_url;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public int getViews()
    {
        return views;
    }

    public void setViews(int views)
    {
        this.views = views;
    }

    public boolean isLiked()
    {
        return liked;
    }

    public void setLiked(boolean liked)
    {
        this.liked = liked;
    }

}

