package main.handlers;

import main.models.User;

import javax.ws.rs.container.ContainerRequestContext;

public class UserSingleton
{
    private static User user;
    private static ContainerRequestContext request;
    private UserSingleton()
    {
    }
    public static User getUser()
    {
        return user;
    }
    public static void setUser(User user)
    {
        UserSingleton.user = user;
    }



}
