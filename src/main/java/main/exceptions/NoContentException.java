package main.exceptions;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import main.models.Error;

@Provider
public class NoContentException extends RuntimeException implements ExceptionMapper<NoContentException>
{
    public NoContentException()
    {
    }

    public NoContentException(String message)
    {
        super(message);
    }

    @Override public Response toResponse(NoContentException e)
    {
        return  Response.status(400)
                .type(MediaType.APPLICATION_JSON)
                .entity(new Error(e.getMessage(),0,400,"no_content_exception"))
                .build();
    }
}
