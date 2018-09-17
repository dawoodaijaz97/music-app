package main.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class User
{
    private int userID;
    private String username;
    private String password;
    private List<Link> links;
    public List<Link> getLinks()
    {
        return links;
    }

    public void setLinks(List<Link> links)
    {
        this.links = links;
    }

    public User()
    {
    }

    public User(int userID, String username, String password)
    {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public User(int userID, String username, String password, List<Link> links)
    {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.links = links;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override public String toString()
    {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", links=" + links +
                '}';
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
