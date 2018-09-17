package main.models;

public class Playlist extends Item
{
    private int userID;
    private String playlist_desc;
    private String type;
    public Playlist()
    {
    }


    public Playlist(String item_name, int userID, String playlist_desc)
    {
        super(item_name);
        this.userID = userID;
        this.playlist_desc = playlist_desc;
        this.type = "playlist";
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public String getPlaylist_desc()
    {
        return playlist_desc;
    }

    public void setPlaylist_desc(String playlist_desc)
    {
        this.playlist_desc = playlist_desc;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override public String toString()
    {
        return "Playlist{" +
                "userID=" + userID +
                ", playlist_desc='" + playlist_desc + '\'' +
                ", type='" + type + '\'' +
                ", item_name='" + item_name + '\'' +
                '}';
    }
}
