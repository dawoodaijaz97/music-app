package main.resources;

import main.handlers.UserHandler;
import main.models.User;;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;


@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RUsers
{
    @GET
    public Response getUsers() throws SQLException, ClassNotFoundException
    {
        List<User> users = UserHandler.getUsers();
        GenericEntity<List<User>> users2 = new GenericEntity<List<User>>(users){};
        return Response.status(Response.Status.OK)
                .entity(users2)
                .build();
    }


    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) throws SQLException, ClassNotFoundException
    {
        User user = UserHandler.getUser(username,0);
        return Response.status(Response.Status.OK)
                .entity(user)
                .build();
    }
    @POST
    public Response createUser(User user) throws SQLException, ClassNotFoundException
    {
        user = UserHandler.createUser(user); //All Error Handling inside the Function
        return Response.status(Response.Status.CREATED)
                .entity(user)
                .build();
    }
    @Path("/{username}/songs")
    public RSongs getSongs(@PathParam("username") String username)
    {
        System.out.println("getting song");
        return new RSongs(username);
    }
    @Path("/{username}/playlists")
    public RPlaylists getPlaylists(@PathParam("username") String username)
    {
        System.out.println("getting playlist");
        return new RPlaylists(username);
    }


}