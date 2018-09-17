package main.filters;

import com.google.api.client.util.Base64;
import main.handlers.*;
import main.models.User;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import main.models.Error;

@Provider
@PreMatching
public class CustomFilter implements ContainerRequestFilter,ContainerResponseFilter
{
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException
    {

        System.out.println("input filter");
        RequestSingleton.setRequest(containerRequestContext);
        SecurityHandler.AuthenticateLogin(containerRequestContext);
        System.out.println("Leaving input filter");

    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException
    {
        System.out.println("Entering response filter");
        String url = containerRequestContext.getUriInfo().getAbsolutePath().toString();
        if(containerResponseContext.hasEntity())
        {
            String body_class = containerResponseContext.getEntityClass().getName();
            System.out.println("class = " + body_class);
            Error error;
            if (body_class.equals("main.models.Error"))
            {

                Object object = containerResponseContext.getEntity();
                if (object instanceof Error)
                {
                    error = (Error) object;
                    System.out.println(error);
                    switch (error.getType())
                    {
                        case "sql_exception":
                            try
                            {
                                Dbhandler.add_sql_exception(url, error);
                            } catch (SQLException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case "Authorization_Exception":
                            try
                            {
                                Dbhandler.add_authorization_exception(url, error);
                            } catch (SQLException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case "no_content_exception":
                            try
                            {
                                Dbhandler.add_no_content_exception(url, error);
                            } catch (SQLException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case "storage_exception":
                            try
                            {
                                Dbhandler.add_storage_exception(url, error);
                            } catch (SQLException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case "Invalid_Request":
                            try
                            {
                                Dbhandler.add_invalid_request_exception(url,error);
                            } catch (SQLException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                        case "NOT_CREATED":
                            try
                            {
                                Dbhandler.add_not_created_exception(url,error);
                            }catch (SQLException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }

                        default:

                    }
                }
            }
        }

    }
}
