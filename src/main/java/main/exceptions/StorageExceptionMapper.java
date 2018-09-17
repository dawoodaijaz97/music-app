package main.exceptions;

import com.google.cloud.storage.StorageException;
import  main.models.Error;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StorageExceptionMapper implements ExceptionMapper<StorageException>
{
    @Override public Response toResponse(StorageException e)
    {
        return Response
                .status(204)
                .type(MediaType.APPLICATION_JSON)
                .entity(new Error(e.getMessage(),0,204,"Storage Exception"))
                .build();
    }
}
