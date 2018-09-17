package main.exceptions;

import main.models.Error;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidRequestException extends RuntimeException implements ExceptionMapper<InvalidRequestException>
{
    public InvalidRequestException(String message)
    {
        super(message);
    }
    public InvalidRequestException()
    {
    }
    @Override public Response toResponse(InvalidRequestException e)
    {
        System.out.println("Invalid error");
        return Response.status(400).type(MediaType.APPLICATION_JSON).entity(new Error(e.getMessage(),0,401,"Invalid_Request")).build();
    }
}
