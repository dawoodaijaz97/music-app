package main.exceptions;


import main.models.Error;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotCreatedException extends RuntimeException implements ExceptionMapper<NotCreatedException>
{
    public NotCreatedException()
    {
    }

    public NotCreatedException(String message)
    {
        super(message);
    }

    @Override public Response toResponse(NotCreatedException e)
    {
        return Response.status(406).entity(new Error(e.getMessage(),1062,406,"NOT_CREATED")).build();
    }
}
