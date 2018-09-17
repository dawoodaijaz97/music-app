package main.resources;
import main.exceptions.InvalidRequestException;
import main.exceptions.NoContentException;
import main.handlers.SecurityHandler;
import main.handlers.SongHandler;
import main.handlers.UserSingleton;
import main.models.Genre;
import main.models.Song;
import main.models.User;

import javax.swing.text.html.parser.Entity;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RSongs
{
    private User user;
    public RSongs(String username)
    {
        System.out.println("Rsong Constructor");
        SecurityHandler.AuthenticateURL(username); //comparing login with username throws Exceptions internally
        this.user = UserSingleton.getUser();
    }

    @GET
    public Response getSongs(@QueryParam("from") String from,@QueryParam("offset") int offset,@QueryParam("range") int range) throws SQLException, ClassNotFoundException
    {
        List<Song> songs1;
        GenericEntity<List<Song>> songs;
        System.out.println("gogole");
        if (from == null)
            songs1 = SongHandler.getSongs(this.user,true);
        else if (from.equals("just_added"))
            songs1 = SongHandler.get_justadded_song(this.user, offset, range);
        else if (from.equals("liked"))
            songs1 = SongHandler.get_liked_songs(this.user, offset, range);
        else if (from.equals("most_viewed"))
            songs1 = SongHandler.get_mostviews_songs(this.user, offset, range);
        else
            throw new InvalidRequestException("Invalid Request Wrong 'From' Type:"+from);

        songs = new GenericEntity<List<Song>>(songs1){};
        System.out.println(songs.getEntity());
        return Response.status(Response.Status.OK)
        .entity(songs)
                .build();
    }
    @GET
    @Path("/{songname}")
    public Response getSong(@PathParam("songname") String songname) throws SQLException, ClassNotFoundException
    {
        return Response.status(Response.Status.OK)
                .entity(SongHandler.getSong(this.user,songname))
                .build();
    }
    @GET
    @Path("/genres")
    public Response getSong_by_genre(@QueryParam("offset") int offset,@QueryParam("range") int range) throws SQLException, ClassNotFoundException
    {
        System.out.println("Getting genres");
        List<Genre> genres1 = SongHandler.getGenres(user,offset,range);
        GenericEntity<List<Genre>> genres = new GenericEntity<List<Genre>>(genres1){};
        return Response.status(Response.Status.OK)
                .entity(genres)
                .build();
    }
    @POST
    public Response createSong(Song song) throws SQLException, ClassNotFoundException
    {
        SecurityHandler.AuthenticateURL(song); //comparing login with Song.userID throws Exceptions internally
        System.out.println(song);
        return Response.status(Response.Status.CREATED)
                .entity(SongHandler.createSong(song))
                .build();

    }

    @DELETE
    @Path("/{songname}")
    public Response deleteSong(@PathParam("songname") String songname) throws SQLException, ClassNotFoundException
    {

        SongHandler.deleteSong(this.user,songname);
        return Response.status(Response.Status.OK).build();
    }
    @POST
    @Path("/{songname}/{playlist_name}")
    public Response add_to_playlist(@PathParam("songname") String songname,@PathParam("playlist_name") String playlist_name ) throws SQLException, ClassNotFoundException
    {

        SongHandler.add_to_playlist(this.user,songname,playlist_name);
        return Response.status(Response.Status.OK).build();

    }
    @PUT
    @Path("/{songname}")
    public Response update_views(@PathParam("songname") String songname) throws SQLException, ClassNotFoundException
    {
        SongHandler.update_view(this.user,songname);
        return Response.status(Response.Status.OK).build();
    }
    @PUT
    @Path("/{songname}/like")
    public Response update_like(@PathParam("songname") String songname) throws SQLException, ClassNotFoundException
    {
        SongHandler.update_like(user,songname);
        return Response.status(200).build();
    }
    @Path("/{songname}/uploads")
    public RUploads getUploads(@PathParam("songname") String songname)
    {
        return new RUploads(songname);
    }
}
