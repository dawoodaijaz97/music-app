package main.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;

import  main.models.Error;

@Provider
public class SQLExceptionMapper implements ExceptionMapper<SQLException>
{
    @Override public Response toResponse(SQLException e)
    {
        e.printStackTrace();
        System.out.println(e.getErrorCode());
        System.out.println(e.getMessage());
        return Response.status(500).entity(new Error(e.getMessage(),1062,500,"sql_exception")).build();
    }
}
