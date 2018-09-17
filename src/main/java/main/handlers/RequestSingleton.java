package main.handlers;

import javax.ws.rs.container.ContainerRequestContext;

public class RequestSingleton
{
    private  static ContainerRequestContext request;
    private RequestSingleton()
    {

    }

    public static ContainerRequestContext getRequest()
    {
        return request;
    }

    public static void setRequest(ContainerRequestContext request)
    {
        RequestSingleton.request = request;
    }
}
