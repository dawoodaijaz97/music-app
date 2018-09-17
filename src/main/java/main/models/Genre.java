package main.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Genre extends Item
{
    private int no_song;

    public Genre()
    {

    }
    public Genre(String item_name, int no_song)
    {
        super(item_name);
        this.no_song = no_song;
    }

    public int getNo_song()
    {
        return no_song;
    }

    public void setNo_song(int no_song)
    {
        this.no_song = no_song;
    }

    @Override public String toString()
    {
        return "Genre{" +
                "no_song=" + no_song +
                ", item_name='" + item_name + '\'' +
                '}';
    }
}
