package main.resources;

import com.google.cloud.storage.*;
import main.handlers.BlobHandler;
import main.handlers.UserSingleton;
import main.models.User;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.SQLException;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RUploads
{
    private User user;
    private String songname;

    RUploads(String songname)
    {
        System.out.println("setting RUploads resource");
        this.user = UserSingleton.getUser();
        this.songname = songname;
    }


    @GET
    public Response getUpload() throws SQLException, ClassNotFoundException
    {

        return Response.status(200).type(MediaType.TEXT_PLAIN).entity(BlobHandler.getSong(this.user,songname)).type(MediaType.TEXT_PLAIN).build();
    }

    @DELETE
    public Response deleteSong() throws SQLException, ClassNotFoundException
    {
        BlobHandler.deleteSong(this.user,this.songname);
        return Response.status(200).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadSong(@FormDataParam("file")InputStream inputStream, @FormDataParam("file")FormDataContentDisposition data, @Context HttpServletRequest request) throws SQLException, StorageException, ClassNotFoundException
    {

        BlobHandler.addSong(this.user,inputStream,data);
        return Response.status(Response.Status.OK).build();
    }





}
