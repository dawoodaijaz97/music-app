package main.exceptions;

import main.models.Error;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationException extends RuntimeException implements ExceptionMapper<AuthenticationException>
{
    public AuthenticationException()
    {
    }

    public AuthenticationException(String message)
    {
        super(message);
    }

    @Override public Response toResponse(AuthenticationException e)
    {
        return Response.status(403).type(MediaType.APPLICATION_JSON).entity(new Error(e.getMessage(),0,403,"Authorization_Exception")).build();
    }
}
