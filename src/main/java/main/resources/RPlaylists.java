package main.resources;


import main.handlers.PlaylistHandler;
import main.handlers.SecurityHandler;
import main.handlers.UserSingleton;
import main.models.Playlist;
import main.models.User;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RPlaylists
{
    private User user;
    public RPlaylists(String username)
    {
        SecurityHandler.AuthenticateURL(username); //comparing login with username throws exception internally
        this.user = UserSingleton.getUser();
    }

    @GET
    public Response getPlaylists(@QueryParam("offset") int offset,@QueryParam("range") int range) throws SQLException, ClassNotFoundException
    {

        List<Playlist> playlists1 = PlaylistHandler.getPlaylists(this.user,offset,range);
        System.out.println(playlists1);
        return Response.status(Response.Status.OK)
                .entity(new GenericEntity<List<Playlist>>(playlists1){})
                .build();

    }
    @POST
    public Response createPlaylist(Playlist playlist) throws SQLException, ClassNotFoundException
    {

        SecurityHandler.AuthenticateURL(playlist);//comparing login with Playlist.userID throws Exceptions internally
        return Response.status(Response.Status.CREATED)
                .entity(PlaylistHandler.createPlaylist(playlist))
                .build();
    }
    @Path("/{playlistname}")
    @DELETE
    public Response deletePlaylist(@PathParam("playlistname") String playlistname) throws SQLException, ClassNotFoundException
    {
        PlaylistHandler.delete_playlist(this.user,playlistname);
            return Response.status(200).build();
    }

}
