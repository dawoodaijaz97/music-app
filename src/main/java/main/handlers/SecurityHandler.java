package main.handlers;

import com.google.api.client.util.Base64;
import main.exceptions.InvalidRequestException;
import main.models.Error;
import main.models.Playlist;
import main.models.Song;
import main.models.User;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

public class SecurityHandler
{
    public static void AuthenticateLogin(ContainerRequestContext request)
    {
        String uri = RequestSingleton.getRequest().getUriInfo().getAbsolutePath().toString();
        String method = RequestSingleton.getRequest().getRequest().getMethod();
        System.out.println(uri);
        System.out.println(method);
        boolean flag = true;

        String app_token = request.getHeaders().getFirst("app_token");
        if(app_token != null)
        {
            app_token = new String(Base64.decodeBase64(app_token));
            if (app_token.equals("panzer123"))
            {
                System.out.println(1);
                try
                {
                    ConnectionSingleton.getConnection();
                } catch (ClassNotFoundException | SQLException e)
                {
                    e.printStackTrace();
                }
                System.out.println(method);
                if (!method.equals("POST") || uri.contains("songs") || uri.contains("playlists"))
                {
                    System.out.println(2);
                    System.out.println(request.getRequest().getMethod());
                    List<String> headers = request.getHeaders().get("Authorization");
                    if (headers != null && headers.size() > 0)
                    {
                        String auth_token = headers.get(0);
                        auth_token = auth_token.replaceFirst("Basic ", " ");
                        String decoded = new String(Base64.decodeBase64(auth_token));
                        System.out.println(decoded);
                        String[] arr = decoded.split(":");
                        String username = arr[0];
                        String password = arr[1];

                        try
                        {
                            User user = UserHandler.getUser(username, 1);
                            if (user != null)
                            {
                                if (username.equals(user.getUsername()) && password.equals(user.getPassword()))
                                {
                                    UserSingleton.setUser(user); //setting the global User
                                    System.out.println("correct info");
                                    return;
                                }
                            }
                            flag = false;

                        } catch (SQLException | ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                        flag = false;
                }
            }
            else
                flag = false;
        }
        else
            flag = false;

        if(!flag)
        {
            System.out.println("incorrect info");
            Response response =  Response.status(Response.Status.FORBIDDEN)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new Error("Username Password not Valid",0,403,"Authorization_Exception"))
                    .build();
            request.abortWith(response);
        }
    }

    public static void AuthenticateURL(String username)
    {
        if(!UserSingleton.getUser().getUsername().equals(username))
            throw new InvalidRequestException("Invalid Request The Users Does Not Match Provided In URL and Login");
    }
    public static void AuthenticateURL(Song song)
    {
        try
        {
            if(!(UserSingleton.getUser().getUserID() == song.getUserID()))
                throw new InvalidRequestException("Invalid Request The Users Does Not Match Provided In URL and Song");
        }catch (NullPointerException e)
        {
            throw new InvalidRequestException("Invalid Request Provide Proper Song Object {'item_name':'item_name','artist_name':'artist_name','song_url':'url','userID':userID}");
        }

    }
    public static void AuthenticateURL(Playlist playlist)
    {
        try
        {
            if(!(UserSingleton.getUser().getUserID() == playlist.getUserID()))
                throw new InvalidRequestException("Invalid Request The Users Does Not Match Provided In URL and Playlist");

        }catch (NullPointerException e)
        {
            throw new InvalidRequestException("Invalid Request Provide Proper Playlist Object {playlist_name:'name',userID:id,(optional)playlist_desc:'desc'}");
        }
    }

}
